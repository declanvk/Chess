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

	private static final int SEARCH_DEPTH = 5;
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
			int value = negaMax(position, SEARCH_DEPTH);
			movesWithValues.add(
					new Pair<Integer, Integer>(move, color[position.getActiveColor()] * value));
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

	private static int negaMax(ChessBoard position, int depth) {
		int best = Integer.MIN_VALUE;

		if (depth <= 0) {
			return position.evaluate();
		}
		for (Integer move : MoveGeneration.getMoves(position, false)) {
			position.move(move);
			int value = -negaMax(position, depth - 1);
			position.unmove(move);
			if (value > best) {
				best = value;
			}
		}

		return best;
	}

	private static int alphaBeta(ChessBoard position, int depth, int alpha, int beta) {
		boolean hasPV = false;
		if (depth <= 0) {
			return position.evaluate();
		}
		for (Integer move : MoveGeneration.getMoves(position, false)) {
			position.move(move);
			int value = 0;
			if (hasPV) {
				value = -alphaBeta(position, depth - 1, -alpha - 1, -alpha);
				if (value > alpha && value < beta) {
					value = -alphaBeta(position, depth - 1, -beta, -alpha);
				}
			} else {
				value = -alphaBeta(position, depth - 1, -beta, -alpha);
			}
			position.unmove(move);
			if (value >= beta) {
				return beta;
			}
			if (value > alpha) {
				alpha = value;
			}
		}

		return alpha;
	}

	public static void main(String[] args) {
		ChessBoard position =
				ChessBoard.ChessBoardFactory.fromFEN("8/1b4p1/8/8/8/8/1Q6/8 w - - 0 1");
		for (Integer m : MoveGeneration.getMoves(position, false)) {
			System.out.println(Move.from(m));
			position.move(m);
			System.out.printf("TOTAL: %d\n\n",
					(color[position.getActiveColor()]) * negaMax(position, 1));
			position.unmove(m);
		}
	}

}
