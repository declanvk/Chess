package core;

public class Move {

	private final int	start, end;
	private final int	flags;
	private final int	piece;

	public Move(int piece, int start, int end, int flags) {
		this.piece = piece;
		this.start = start;
		this.end = end;
		this.flags = flags;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public int getFlags() {
		return flags;
	}

	public int getPiece() {
		return piece;
	}

}
