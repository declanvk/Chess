package engine;

import java.util.ArrayList;

import core.ChessBoard;

public class Perft {

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
		ChessBoard position = ChessBoard.ChessBoardFactory.startingBoard();
		for(int depth = 0; depth <= 6; depth++) {
			System.out.printf("%10d\n", perft(position, depth));
		}
		
		//Results:
		// 1
		// 20
		// 400
		// 8902
		// 197281
		// 4865351
		// 119048441
	}

}
