package engine;

import java.util.ArrayList;

import core.ChessBoard;

/**
 * Testing class used to check the correctness of the move generation
 * algorithms. For the given depths, a perft function should return the
 * following results. Format: "depth: nodes"
 * 
 * 0:1 1:20 2:400 3:8902 4:197281 5:4865351 6:119048441
 * 
 * @author declan
 *
 */
public class Perft {

	/**
	 * Counts the number of legal positions in the search tree given an initial
	 * position and max search depth
	 * 
	 * @param position
	 *            the position to count nodes for
	 * @param depth
	 *            the maximum depth at which to count nodes
	 * @return the number of legal positions in the search tree given an initial
	 *         position and max search depth
	 */
	public static long perft(ChessBoard position, int depth) {
		int moveCount, i;
		long nodeCount = 0;

		if (depth == 0) {
			return 1;
		}

		ArrayList<Integer> moves = MoveGeneration.getMoves(position, false);
		moveCount = moves.size();
		for (i = 0; i < moveCount; i++) {
			position.move(moves.get(i));
			nodeCount += perft(position, depth - 1);
			position.unmove(moves.get(i));
		}

		return nodeCount;
	}

	public static void main(String[] args) {
		long[] expectedValue = { 1, 20, 400, 8902, 197281, 4865351, 119048441 };
		ChessBoard position = ChessBoard.ChessBoardFactory.startingBoard();
		for (int depth = 0; depth <= 6; depth++) {
			long value = perft(position, depth);

			System.out.printf("%10d:%10d\n", expectedValue[depth], value);
		}
	}

}
