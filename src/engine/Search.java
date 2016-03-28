package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

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

	private static final Timer timer = new Timer();
	private static final int SEARCH_DEPTH = 5;
	private static final int[] color = { 1, -1 };

	private boolean continueSearch;
	private long delay;

	public Search(long time) {
		delay = time;
		continueSearch = true;
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

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				continueSearch = false;
			}
		}, delay);

		for (int searchDepth = 1; searchDepth < SEARCH_DEPTH; searchDepth++) {
			for (Integer move : moves) {
				position.move(move);
				int value = negaMax(position, table, searchDepth, Integer.MIN_VALUE,
						Integer.MAX_VALUE, color[ChessColor.opposite(position.getActiveColor())]);

				movesWithValues.add(new Pair<Integer, Integer>(move, value));
				position.unmove(move);
			}
		}

		Collections.sort(movesWithValues, new Comparator<Pair<Integer, Integer>>() {

			@Override
			public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
				return Integer.compare(o1.second(), o2.second());
			}

		});

		return Move.from(movesWithValues.get(0).first());
	}

	private static int negaMax(ChessBoard position, TranspositionTable table, int depth, int alpha,
			int beta, int color) {
		int alphaOriginal = alpha;

		Transposition entry = table.get(position.getZobristKey());
		if (entry != null && entry.depth <= depth) {
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
			return color * position.evaluate();
		}

		int bestValue = Integer.MIN_VALUE;
		int bestMove = Move.NULL_MOVE;
		for (Integer move : MoveGeneration.getMoves(position, false)) {
			position.move(move);
			int value = -negaMax(position, table, depth - 1, -beta, -alpha, -color);
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
