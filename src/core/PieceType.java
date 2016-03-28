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
	PAWN(100, "P"),

	/**
	 * The knight
	 */
	KNIGHT(325, "N"),

	/**
	 * The bishop
	 */
	BISHOP(325, "B"),

	/**
	 * The rook
	 */
	ROOK(500, "R"),

	/**
	 * The queen
	 */
	QUEEN(975, "Q"),

	/**
	 * The king
	 */
	KING(20000, "K");

	/**
	 * The serialized value representing no promotion
	 */
	public final static int NULL_PROMOTION = 6;

	private final int score;
	private final String repr;

	PieceType(int val, String repr) {
		this.score = val;
		this.repr = repr;
	}

	/**
	 * Returns the score value associated with the piece type
	 * 
	 * @return the score value associated with the piece type
	 */
	public int score() {
		return this.score;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return repr;
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
	 *            the type value to check the validity of
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
	 *            the type value to check the promotion validity of
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
	 *            the type value to construct a PieceType from
	 * @return the PieceType associated with the given serialized value
	 */
	public static PieceType from(int type) {
		assert isValid(type);

		return PieceType.values()[type];
	}

	public static PieceType from(char repr) {
		if (repr == 'p' || repr == 'P') {
			return PieceType.PAWN;
		} else if (repr == 'n' || repr == 'N') {
			return PieceType.KNIGHT;
		} else if (repr == 'b' || repr == 'B') {
			return PieceType.BISHOP;
		} else if (repr == 'r' || repr == 'R') {
			return PieceType.ROOK;
		} else if (repr == 'q' || repr == 'Q') {
			return PieceType.QUEEN;
		} else if (repr == 'k' || repr == 'K') {
			return PieceType.KING;
		} else {
			assert false;
			return null;
		}
	}

	/**
	 * Returns the score value of the given serialized value
	 * 
	 * @param type
	 *            the type value to get the score value of
	 * @return the score value of the given serialized value
	 */
	public static int getScore(int type) {
		assert isValid(type);

		return scores[type];
	}

}
