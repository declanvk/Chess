package game;

public class Move {

	public enum MoveFlags {
		QUIET, CAPTURE, PROMOTION, CASTLE, EN_PASSANT, DOUBLE_PAWN_PUSH
	}

	private final Position		start, end;
	private final MoveFlags		flag;
	private final ChessPiece	piece;

	public Move(ChessPiece piece, Position start, Position end, MoveFlags flags) {
		this.piece = piece;
		this.start = start;
		this.end = end;
		this.flag = flags;
	}

	public Position getStart() {
		return start;
	}

	public Position getEnd() {
		return end;
	}

	public MoveFlags getFlag() {
		return flag;
	}

	public ChessPiece getPiece() {
		return piece;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((flag == null) ? 0 : flag.hashCode());
		result = prime * result + ((piece == null) ? 0 : piece.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		Move other = (Move) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (flag != other.flag)
			return false;
		if (piece == null) {
			if (other.piece != null)
				return false;
		} else if (!piece.equals(other.piece))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Move [start=" + start.algebraicPosition() + ", end=" + end.algebraicPosition()
				+ ", flag=" + flag + ", piece=" + piece + "]";
	}

}
