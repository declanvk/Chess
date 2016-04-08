package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import core.ChessBoard;
import core.ChessColor;
import core.Move;
import core.Position;
import engine.TranspositionTable.Transposition;
import engine.TranspositionTable.TranspositionType;
import util.Pair;

/**
 * Contains all the methods used to search the game tree and return the optimal
 * Move
 * 
 * @author declan
 *
 */
public class Search {

	private static final int TABLE_KEY_SIZE = 20;

	private static final int DRAW = 0;
	private static final int CHECKMATE = 20000;

	private static final ScheduledExecutorService timer =
			Executors.newSingleThreadScheduledExecutor();

	private boolean continueSearch;
	private long delay;

	private final int[][][] historyTable;

	/**
	 * Constructs a new Search with specified time limit
	 * 
	 * @param time
	 *            the amount of time in milliseconds that the search has
	 */
	public Search(long time) {
		delay = time;
		continueSearch = true;
		historyTable = new int[ChessColor
				.values().length][Position.NUM_TOTAL_VALUES][Position.NUM_TOTAL_VALUES];
	}

	/**
	 * Given the initial position, determines the best Move for the currently
	 * active color
	 * 
	 * @param position
	 *            the position to start the search from
	 * @return the best Move for the currently active color
	 */
	public Move execute(ChessBoard position) {
		ArrayList<Integer> moves = MoveGeneration.getMoves(position, false);
		ArrayList<Pair<Integer, Integer>> movesWithValues = new ArrayList<Pair<Integer, Integer>>();
		TranspositionTable table = new TranspositionTable(TABLE_KEY_SIZE);

		// SearchLogger searchLog = new SearchLogger(delay,
		// position.getZobristKey().getKey());

		this.continueSearch = true;
		ScheduledFuture<?> task = timer.schedule(new Runnable() {

			@Override
			public void run() {
				continueSearch = false;
			}

		}, delay, TimeUnit.MILLISECONDS);

		for (int searchDepth = 1; continueSearch; searchDepth++) {
			// searchLog.logIterativeDeepeningLevel(searchDepth);
			movesWithValues.clear();

			MoveList moveList = new MoveList(moves, position, table, false);
			for (Integer move : moveList) {
				position.move(move);
				// searchLog.logNewSearchLevel(0, move);

				int value =
						pvs(position, table, searchDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
				movesWithValues.add(new Pair<Integer, Integer>(move, value));

				// searchLog.logSearchLevelReturn(0, move, value);
				position.unmove(move);
			}

			// searchLog.logIterativeDeepeningBestMove(searchDepth,
			// table.get(position.getZobristKey()) != null ?
			// table.get(position.getZobristKey()).bestMove : Move.NULL_MOVE);

			Collections.sort(movesWithValues, new Comparator<Pair<Integer, Integer>>() {

				@Override
				public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
					return Integer.compare(o1.second(), o2.second());
				}

			});

			table.set(position.getZobristKey(),
					new Transposition(position.getZobristKey().getKey(),
							movesWithValues.get(0).first(), movesWithValues.get(0).second(), 0,
							TranspositionType.EXACT.value()));

			System.err.println("   PV: " + getPVString(position, table));
		}

		// searchLog.close();
		task.cancel(true);

		return Move.from(movesWithValues.get(0).first());
	}

	private String getPVString(ChessBoard position, TranspositionTable table) {
		if (table.get(position.getZobristKey()) != null) {
			Transposition pv = table.get(position.getZobristKey());
			if (pv.key == position.getZobristKey().getKey() && pv.bestMove != Move.NULL_MOVE
					&& Move.isValid(pv.bestMove)) {
				int bestMove = pv.bestMove;
				String result = "";
				position.move(bestMove);
				result = getPVString(position, table);
				position.unmove(bestMove);

				return ChessNotation.algebraic(bestMove)
						+ ((!result.equals("")) ? " > " + result : "");
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	private int pvs(ChessBoard position, TranspositionTable table, int depth, int alpha, int beta,
			int ply) {
		int alphaOriginal = alpha;

		Transposition entry = table.get(position.getZobristKey());
		if (entry != null && entry.depth >= depth) {
			// log.logTranspositionHit(ply, entry);

			if (entry.type == TranspositionType.EXACT.value()) {
				return entry.value;
			} else if (entry.type == TranspositionType.LOWER.value()) {
				alpha = Math.max(alpha, entry.value);
			} else if (entry.type == TranspositionType.UPPER.value()) {
				beta = Math.min(beta, entry.value);
			}

			if (alpha >= beta) {
				return entry.value;
			}
		}

		if (depth <= 0) {
			int eval = quiescent(position, table, alpha, beta);

			// log.logTerminal(ply, Integer.toString(eval),
			// position.getZobristKey().getKey());

			return eval;
		} else if (position.isRepetition() || position.hasInsufficientMaterial()
				|| position.getHalfTurnClock() >= 100) {
			// log.logTerminal(ply, "DRAW", position.getZobristKey().getKey());

			return DRAW;
		}

		MoveList moves =
				new MoveList(MoveGeneration.getMoves(position, false), position, table, false);
		if (moves.size() == 0) {
			if (position.isCheck()) {
				return -CHECKMATE + ply;
			} else {
				return DRAW;
			}
		}

		boolean madeFirstMove = false;
		int bestMove = Move.NULL_MOVE;
		int bestScore = Integer.MIN_VALUE;
		for (Integer move : moves) {
			if (!madeFirstMove) {
				position.move(move);
				bestScore = -pvs(position, table, depth - 1, -beta, -alpha, ply + 1);
				position.unmove(move);

				if (bestScore > alpha) {
					if (bestScore >= beta) {
						bestMove = move;
						break;
					}

					alpha = bestScore;
				}
				madeFirstMove = true;
			} else {
				position.move(move);
				int score = -pvs(position, table, depth - 1, -alpha - 1, -alpha, ply + 1);
				if (alpha < score && score < beta) {
					score = -pvs(position, table, depth - 1, -beta, -alpha, ply + 1);
					if (score > alpha) {
						alpha = score;
					}
				}
				position.unmove(move);

				if (score > bestScore) {
					bestScore = score;
					bestMove = move;
					if (score >= beta) {
						break;
					}
				}
			}
		}

		TranspositionType type;
		if (bestScore <= alphaOriginal) {
			type = TranspositionType.UPPER;
			bestMove = Move.NULL_MOVE;
		} else if (bestScore >= beta) {
			type = TranspositionType.LOWER;
			bestMove = Move.NULL_MOVE;
		} else {
			type = TranspositionType.EXACT;
		}
		entry = new Transposition(position.getZobristKey().getKey(), bestMove, bestScore, depth,
				type.value());
		table.set(position.getZobristKey(), entry);

		return bestScore;
	}

	private static int quiescent(ChessBoard position, TranspositionTable table, int alpha,
			int beta) {
		int standingPat = position.evaluate();
		if (standingPat >= beta) {
			return beta;
		} else if (alpha < standingPat) {
			alpha = standingPat;
		}

		MoveList moves =
				new MoveList(MoveGeneration.getMoves(position, true), position, table, true);
		for (Integer move : moves) {
			position.move(move);
			int value = -quiescent(position, table, -beta, -alpha);
			position.unmove(move);

			if (value >= beta) {
				return beta;
			} else if (value > alpha) {
				alpha = value;
			}
		}

		return alpha;
	}

}
