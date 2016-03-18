package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import core.ChessBoard;
import core.Move;
import util.Pair;

/**
 * Contains all the methods used to search the game tree and return the optimal
 * Move
 * 
 * @author declan
 *
 */
public class Search {

	private static final int CHECKMATE = -1;
	private static final int DRAW = 0;

	private static final int SEARCH_DEPTH = 4;
	private static final int[] color = { 1, -1 };

	/**
	 * Given the initial position, determines the best Move for the currently
	 * active color
	 * 
	 * @param position
	 *            the position to start the search from
	 * @return the best Move for the currently active color
	 */
	public static Move search(ChessBoard position) {
		ArrayList<Integer> moves = MoveGeneration.getMoves(position, false);
		ArrayList<Pair<Integer, Integer>> movesWithValues = new ArrayList<Pair<Integer, Integer>>();
		for (Integer move : moves) {
			position.move(move);
			movesWithValues.add(new Pair<Integer, Integer>(move,
					color[position.getActiveColor()]
							* search(position, SEARCH_DEPTH - 1, Integer.MIN_VALUE,
									Integer.MAX_VALUE, color[position.getActiveColor()])));
			position.unmove(move);
		}

		Collections.sort(movesWithValues, new Comparator<Pair<Integer, Integer>>() {

			@Override
			public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
				return Integer.compare(o1.second(), o2.second());
			}

		});

		return Move.from(movesWithValues.get(0).first());
	}

	private static int search(ChessBoard position, int depth, int alpha, int beta, int color) {
		if (depth == 0) {
			return quiesce(position, alpha, beta);
		}

		int best = Integer.MIN_VALUE;
		for (Integer move : MoveGeneration.getMoves(position, false)) {
			position.move(move);
			int value = -search(position, depth - 1, -beta, -alpha, -color);
			position.unmove(move);
			best = Math.max(best, value);
			alpha = Math.max(alpha, value);
			if (alpha >= beta) {
				break;
			}
		}

		return best;
	}

	private static int quiesce(ChessBoard position, int alpha, int beta) {
		int stand_pat = position.evaluate();
		if (stand_pat >= beta) {
			return beta;
		} else if (alpha < stand_pat) {
			alpha = stand_pat;
		}

		for (Integer move : MoveGeneration.getMoves(position, false)) {
			position.move(move);
			int value = -quiesce(position, -beta, -alpha);
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
