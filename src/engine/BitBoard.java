package engine;

import java.util.Arrays;
import game.Move;
import game.Color;
import game.PieceType;

public class BitBoard {
	
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
		boards[Color.WHITE.getID()] = Arrays.copyOf(startingConfiguration[Color.WHITE.getID()], startingConfiguration[Color.WHITE.getID()].length);
		boards[Color.BLACK.getID()] = Arrays.copyOf(startingConfiguration[Color.BLACK.getID()], startingConfiguration[Color.BLACK.getID()].length);
	}
	
	private BitBoard(long[][] boards) {
		this.boards = boards;
	}
	
	public BitBoard applyMove(Move move) {
		BitBoard newBoard = this.clone();
		
		//TODO apply move
		//Move should be a 
		
		return newBoard;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(boards);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BitBoard other = (BitBoard) obj;
		if (!Arrays.deepEquals(boards, other.boards))
			return false;
		return true;
	}

	@Override
	public BitBoard clone() {
		long[][] newBoard = new long[2][6];
		boards[Color.WHITE.getID()] = Arrays.copyOf(this.boards[Color.WHITE.getID()], 6);
		boards[Color.BLACK.getID()] = Arrays.copyOf(this.boards[Color.BLACK.getID()], 6);
		return new BitBoard(newBoard);
	}
}
