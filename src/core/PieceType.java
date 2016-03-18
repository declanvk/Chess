package core;

/**
 * PieceType represents all the different types of pieces in the game of chess.
 * 
 * @author declan
 *
 */
public enum PieceType {
	/**
	 * The pawn
	 */
	PAWN(100),

	/**
	 * The knight
	 */
	KNIGHT(325),

	/**
	 * The bishop
	 */
	BISHOP(325),

	/**
	 * The rook
	 */
	ROOK(500),

	/**
	 * The queen
	 */
	QUEEN(975),

	/**
	 * The king
	 */
	KING(Integer.MAX_VALUE);

	/**
	 * The serialized value representing no promotion
	 */
	public final static int NULL_PROMOTION = 6;

	private final int score;

	PieceType(int val) {
		this.score = val;
	}

	/**
	 * Returns the score value associated with the piece type
	 * 
	 * @return the score value associated with the piece type
	 */
	public int score() {
		return this.score;
	}

	/**
	 * Returns the serialized value of the piece type
	 * 
	 * @return the serialized value of the piece type
	 */
	public int value() {
		return this.ordinal();
	}

	private static final int[] scores = { 100, 325, 325, 500, 975, Integer.MAX_VALUE };

	/**
	 * Returns true if the given value could be a serialized piece type
	 * 
	 * @param type
	 * @return true if the given value could be a serialized piece type
	 */
	public static boolean isValid(int type) {
		return PAWN.value() <= type && type <= KING.value();
	}

	/**
	 * Returns true if the given value could be a serialized piece type and a
	 * valid promotion type
	 * 
	 * @param type
	 * @return true if the given value could be a serialized piece type and a
	 *         valid promotion type
	 */
	public static boolean isValidPromotion(int type) {
		return KNIGHT.value() <= type && type <= QUEEN.value() || type == NULL_PROMOTION;
	}

	/**
	 * Returns the PieceType associated with the given serialized value
	 * 
	 * @param type
	 * @return the PieceType associated with the given serialized value
	 */
	public static PieceType from(int type) {
		assert isValid(type);

		return PieceType.values()[type];
	}

	/**
	 * Returns the score value of the given serialized value
	 * 
	 * @param type
	 * @return the score value of the given serialized value
	 */
	public static int getScore(int type) {
		assert isValid(type);

		return scores[type];
	}

}
