package game;

public class ChessPiece {
	
	private final ChessColor color;
	private final Piece type;
	
	public ChessPiece(ChessColor color, Piece type) {
		this.color = color;
		this.type = type;
	}

	public String getUnicode() {
		return color == ChessColor.WHITE ? type.getWhiteUnicode() : type.getBlackUnicode();
	}

	public ChessColor getColor() {
		return color;
	}

	public Piece getType() {
		return type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ChessPiece other = (ChessPiece) obj;
		if (color != other.color)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChessPiece [color=" + color + ", type=" + type + "]";
	}
}
