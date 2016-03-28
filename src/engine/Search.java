package engine;

import java.io.File;
import java.io.IOException;
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

	private static final int DRAW = 0;
	private static final int CHECKMATE = 200000;

	private static final ScheduledExecutorService timer =
			Executors.newSingleThreadScheduledExecutor();
	private static final int MAX_SEARCH_DEPTH = 4;
	private static final int[] color = { 1, -1 };

	private boolean continueSearch;
	private long delay;

	public Search(long time) {
		delay = time;
		continueSearch = true;
	}

	private SearchLogger createLogger(ChessBoard position) {
		int count = 0;
		File out;
		do {
			out = new File("log/search" + count + ".log");
			count++;
		} while (out.exists());

		try {
			out.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new SearchLogger(out, delay, position.getZobristKey().getKey());
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
		TranspositionTable table = new TranspositionTable(16);

		SearchLogger searchLog = createLogger(position);

		this.continueSearch = true;
		ScheduledFuture<?> task = timer.schedule(new Runnable() {

			@Override
			public void run() {
				continueSearch = false;
			}

		}, delay, TimeUnit.MILLISECONDS);

		for (int searchDepth = 1; continueSearch && searchDepth < MAX_SEARCH_DEPTH; searchDepth++) {
			searchLog.logIterativeDeepeningLevel(searchDepth);

			for (Integer move : moves) {
				position.move(move);
				searchLog.logNewSearchLevel(0, move);

				int value = negaMax(position, table, searchDepth, Integer.MIN_VALUE,
						Integer.MAX_VALUE, color[ChessColor.opposite(position.getActiveColor())], 1,
						searchLog);
				movesWithValues.add(new Pair<Integer, Integer>(move, value));

				searchLog.logSearchLevelReturn(0, move, value);
				position.unmove(move);
			}
		}

		Collections.sort(movesWithValues, new Comparator<Pair<Integer, Integer>>() {

			@Override
			public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
				return Integer.compare(o1.second(), o2.second());
			}

		});

		searchLog.close();
		task.cancel(true);

		return Move.from(movesWithValues.get(0).first());
	}

	private int negaMax(ChessBoard position, TranspositionTable table, int depth, int alpha,
			int beta, int color, int ply, SearchLogger log) {

		int alphaOriginal = alpha;

		Transposition entry = table.get(position.getZobristKey());
		if (entry != null && entry.depth >= depth) {
			log.logTranspositionHit(ply, entry);

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
			int eval = color * position.evaluate();

			log.logTerminal(ply, Integer.toString(eval), position.getZobristKey().getKey(), depth);

			return eval;
		} else if (position.hasInsufficientMaterial() || position.getHalfTurnClock() >= 100) {
			log.logTerminal(ply, "DRAW", position.getZobristKey().getKey(), depth);

			return DRAW;
		}

		int bestValue = Integer.MIN_VALUE;
		int bestMove = Move.NULL_MOVE;
		MoveList moves = new MoveList(MoveGeneration.getMoves(position, false), position, table);
		for (Integer move : moves) {

			position.move(move);
			log.logNewSearchLevel(ply, move);

			int value = -negaMax(position, table, depth - 1, -beta, -alpha, -color, ply + 1, log);

			log.logSearchLevelReturn(ply, move, value);
			position.unmove(move);
			if (value > bestValue) {
				bestValue = value;
				bestMove = move;
			}
			alpha = Math.max(alpha, value);
			if (alpha >= beta) {
				break;
			}
		}

		TranspositionType type;
		if (bestValue <= alphaOriginal) {
			type = TranspositionType.UPPER;
		} else if (bestValue >= beta) {
			type = TranspositionType.LOWER;
		} else {
			type = TranspositionType.EXACT;
		}
		entry = new Transposition(position.getZobristKey().getKey(), bestMove, bestValue, depth,
				type.value());
		table.set(position.getZobristKey(), entry);

		return bestValue;
	}

	private static int quiescent(ChessBoard position, int alpha, int beta) {
		int standingPat = position.evaluate();
		if (standingPat >= beta) {
			return beta;
		} else if (alpha < standingPat) {
			alpha = standingPat;
		}

		for (Integer move : MoveGeneration.getMoves(position, true)) {
			position.move(move);
			int value = -quiescent(position, -beta, -alpha);
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
