package core;

/**
 * ChessPiece represent any color or type of chess piece, also storing
 * information about how to draw each piece.
 * 
 * @author declan
 *
 */
public enum ChessPiece {

	/**
	 * The white pawn
	 */
	WHITE_PAWN(ChessColor.WHITE, PieceType.PAWN, "\u2659"),

	/**
	 * The white knight
	 */
	WHITE_KNIGHT(ChessColor.WHITE, PieceType.KNIGHT, "\u2658"),

	/**
	 * The white bishop
	 */
	WHITE_BISHOP(ChessColor.WHITE, PieceType.BISHOP, "\u2657"),

	/**
	 * The white rook
	 */
	WHITE_ROOK(ChessColor.WHITE, PieceType.ROOK, "\u2656"),

	/**
	 * The white queen
	 */
	WHITE_QUEEN(ChessColor.WHITE, PieceType.QUEEN, "\u2655"),

	/**
	 * The white king
	 */
	WHITE_KING(ChessColor.WHITE, PieceType.KING, "\u2654"),

	/**
	 * The black pawn
	 */
	BLACK_PAWN(ChessColor.BLACK, PieceType.PAWN, "\u265F"),

	/**
	 * The black knight
	 */
	BLACK_KNIGHT(ChessColor.BLACK, PieceType.KNIGHT, "\u265E"),

	/**
	 * The black bishop
	 */
	BLACK_BISHOP(ChessColor.BLACK, PieceType.BISHOP, "\u265D"),

	/**
	 * The black rook
	 */
	BLACK_ROOK(ChessColor.BLACK, PieceType.ROOK, "\u265C"),

	/**
	 * The black queen
	 */
	BLACK_QUEEN(ChessColor.BLACK, PieceType.QUEEN, "\u265B"),

	/**
	 * The black king
	 */
	BLACK_KING(ChessColor.BLACK, PieceType.KING, "\u265A");

	private final ChessColor color;
	private final PieceType type;
	private final String unicode;
	private final int value;

	ChessPiece(ChessColor color, PieceType type, String uni) {
		this.color = color;
		this.type = type;
		this.unicode = uni;
		this.value = this.ordinal();
	}

	/**
	 * Returns the Unicode representation of the ChessPiece, used for drawing
	 * 
	 * @return the Unicode representation of the ChessPiece
	 */
	public String getUnicode() {
		return unicode;
	}

	/**
	 * Returns the color associated with the ChessPiece
	 * 
	 * @return the color associated with the ChessPiece
	 */
	public ChessColor getColor() {
		return color;
	}

	/**
	 * Returns the PieceType associated with the ChessPiece
	 * 
	 * @return the PieceType associated with the ChessPiece
	 */
	public PieceType getType() {
		return type;
	}

	/**
	 * Returns the score value of the ChessPiece
	 * 
	 * @return the score value of the ChessPiece
	 */
	public int score() {
		return type.score();
	}

	/**
	 * Returns the serialized value of the ChessPiece
	 * 
	 * @return the serialized value of the ChessPiece
	 */
	public int value() {
		return value;
	}

	/**
	 * The serialized value representing no piece
	 */
	public static final int NULL_PIECE = 12;

	/**
	 * The amount of bits needed to represent the full range of serialized
	 * ChessPiece values
	 */
	public static final int BIT_WIDTH = 4;

	/**
	 * Returns true if the given value is a valid serialized ChessPiece
	 * 
	 * @param piece
	 *            the piece value to check the validity of
	 * @return true if the given value is a valid serialized ChessPiece
	 */
	public static boolean isValid(int piece) {
		return WHITE_PAWN.value() <= piece && piece <= BLACK_KING.value();
	}

	/**
	 * Returns the ChessPiece associated with the given value, if it is valid
	 * 
	 * @param piece
	 *            the value to construct a ChessPiece from
	 * @return the ChessPiece associated with the given value
	 */
	public static ChessPiece from(int piece) {
		if (!isValid(piece) && piece != NULL_PIECE) {
			throw new IllegalArgumentException(
					"Piece is not valid or equal to the null piece value");
		}

		return (piece != NULL_PIECE) ? ChessPiece.values()[piece] : null;
	}

	/**
	 * Returns the ChessPiece associated with the given serialized color and
	 * type
	 * 
	 * @param color
	 *            the color value to construct a ChessPiece from
	 * @param type
	 *            the type value to construct a ChessPiece from
	 * @return the ChessPiece associated with the given serialized color and
	 *         type
	 */
	public static ChessPiece from(int color, int type) {
		return ChessPiece.values()[(6 * color) + type];
	}

	/**
	 * Returns the serialized form of the ChessPiece associated with the given
	 * serialized color and type
	 * 
	 * @param color
	 *            the color value to construct a serialized ChessPiece from
	 * @param type
	 *            the type value to construct a serialized ChessPiece from
	 * @return the serialized form of the ChessPiece associated with the given
	 *         serialized color and type
	 */
	public static int fromRaw(int color, int type) {
		return (6 * color) + type;
	}

	/**
	 * Returns the ChessPiece associated with the given ChessColor and PieceType
	 * 
	 * @param color
	 *            the ChessColor to construct a ChessPiece from
	 * @param type
	 *            the PieceType to construct a ChessPiece from
	 * @return the ChessPiece associated with the given ChessColor and PieceType
	 */
	public static ChessPiece from(ChessColor color, PieceType type) {
		return ChessPiece.from(color.value(), type.value());
	}

	/**
	 * Returns the serialized form of the ChessColor associated with the
	 * serialized ChessPiece
	 * 
	 * @param piece
	 *            the piece value to get the color of
	 * @return the serialized form of the ChessColor associated with the
	 *         serialized ChessPiece
	 */
	public static int getColor(int piece) {
		if (!isValid(piece) && piece != NULL_PIECE) {
			throw new IllegalArgumentException("Piece is not valid");
		}

		return piece < 6 ? 0 : 1;
	}

	/**
	 * Returns the serialized form of the PieceType associated with the
	 * serialized ChessPiece
	 * 
	 * @param piece
	 *            the piece value to get the type of
	 * @return the serialized form of the PieceType associated with the
	 *         serialized ChessPiece
	 */
	public static int getPieceType(int piece) {
		if (!isValid(piece) && piece != NULL_PIECE) {
			throw new IllegalArgumentException("Piece is not valid");
		}

		return piece % 6;
	}

	/**
	 * Returns the score value associated with the serialized ChessPiece
	 * 
	 * @param piece
	 *            the piece value to get the score value of
	 * @return the score value associated with the serialized ChessPiece
	 */
	public static int getScore(int piece) {
		int type = getPieceType(piece);

		return PieceType.getScore(type);
	}
}
