package game;

import java.awt.Point;

public class Move {
	
	private final Color color;
	private final PieceType piece;
	private final Point start, end;
	private final boolean killing;
	
	public Move(Color color, PieceType piece, Point start, Point end, boolean killing) {
		this.color = color;
		this.piece = piece;
		this.start = start;
		this.end = end;
		this.killing = killing;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @return the piece
	 */
	public PieceType getPiece() {
		return piece;
	}

	/**
	 * @return the start
	 */
	public Point getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public Point getEnd() {
		return end;
	}

	/**
	 * @return the killing
	 */
	public boolean isKilling() {
		return killing;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + (killing ? 1231 : 1237);
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
		if (color != other.color)
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (killing != other.killing)
			return false;
		if (piece != other.piece)
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

}
