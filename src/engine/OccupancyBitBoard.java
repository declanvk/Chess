package engine;

import java.util.Arrays;

import game.ChessColor;
import game.Piece;

public class OccupancyBitBoard {
	
	private static final long[][] startingConfiguration = {
	{//White
		0x000000000000FF00L, //Pawns
		0x0000000000000042L, //Knights
		0x0000000000000024L, //Bishops
		0x0000000000000081L, //Rooks
		0x0000000000000008L, //Queen
		0x0000000000000010L //King
	},
	{//Black
		0x00FF000000000000L, //Pawns
		0x4200000000000000L, //Knights
		0x2400000000000000L, //Bishops
		0x8100000000000000L, //Rooks
		0x1000000000000000L, //Queen
		0x0800000000000000L //King
	}};
	
	private final long[][] boards;

	public OccupancyBitBoard() {
		this.boards = new long[2][6];
		boards[ChessColor.WHITE.getID()] = Arrays.copyOf(startingConfiguration[ChessColor.WHITE.getID()], startingConfiguration[ChessColor.WHITE.getID()].length);
		boards[ChessColor.BLACK.getID()] = Arrays.copyOf(startingConfiguration[ChessColor.BLACK.getID()], startingConfiguration[ChessColor.BLACK.getID()].length);
	}
	
	private OccupancyBitBoard(long[][] boards) {
		this.boards = boards;
	}
	
	public long getBoard(Piece type, ChessColor color) {
		return boards[color.getID()][type.getID()];
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
		OccupancyBitBoard other = (OccupancyBitBoard) obj;
		if (!Arrays.deepEquals(boards, other.boards))
			return false;
		return true;
	}

	@Override
	public OccupancyBitBoard clone() {
		long[][] newBoard = new long[2][6];
		boards[ChessColor.WHITE.getID()] = Arrays.copyOf(this.boards[ChessColor.WHITE.getID()], 6);
		boards[ChessColor.BLACK.getID()] = Arrays.copyOf(this.boards[ChessColor.BLACK.getID()], 6);
		return new OccupancyBitBoard(newBoard);
	}
}
