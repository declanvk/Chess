package engine;

import java.util.Arrays;

public class BitBoard {
	
	private static final int WHITE = 0;
	private static final int BLACK = 1;
	
	private static final int PAWN = 0;
	private static final int ROOK = 1;
	private static final int KNIGHT = 2;
	private static final int BISHOP = 3;
	private static final int QUEEN = 4;
	private static final int KING = 5;
	
	private static final long[][] startingConfiguration = {
	{//White
		0x000000000000FF00L, //Pawns
		0x0000000000000081L, //Rooks
		0x0000000000000042L, //Knights
		0x0000000000000024L, //Bishops
		0x0000000000000008L, //Queen
		0x0000000000000010L //King
	},
	{//Black
		0x00FF000000000000L, //Pawns
		0x8100000000000000L, //Rooks
		0x4200000000000000L, //Knights
		0x2400000000000000L, //Bishops
		0x1000000000000000L, //Queen
		0x0800000000000000L //King
	}};
	
	private final long[][] boards;

	public BitBoard() {
		this.boards = new long[2][6];
		boards[WHITE] = Arrays.copyOf(startingConfiguration[WHITE], startingConfiguration[WHITE].length);
		boards[BLACK] = Arrays.copyOf(startingConfiguration[BLACK], startingConfiguration[BLACK].length);
	}

}
