package engine;

import core.ChessBoard;
import core.Move;

public class Search {

	private static final int SEARCH_DEPTH = 7;

	public static Move search(ChessBoard position) {
		int bestMove = Move.NULL_MOVE;
		int bestMoveScore = Integer.MIN_VALUE;

		for (Integer move : MoveGeneration.getMoves(position, false)) {
			position.move(move);
			int value = search(position, SEARCH_DEPTH - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
			position.unmove(move);
			if (value > bestMoveScore) {
				bestMove = move;
				bestMoveScore = value;
			}
		}

		return Move.from(bestMove);
	}

	private static int search(ChessBoard position, int depth, int alpha, int beta) {
		if (depth == 0) {
			return position.evaluate();
		}

		int best = Integer.MIN_VALUE;
		for (Integer move : MoveGeneration.getMoves(position, false)) {
			position.move(move);
			int value = -search(position, depth - 1, -beta, -alpha);
			position.unmove(move);
			best = Math.max(best, value);
			alpha = Math.max(alpha, value);
			if (alpha >= beta) {
				break;
			}
		}
		return best;
	}

}
