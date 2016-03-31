package core;

/**
 * Represents a move in chess
 * 
 * @author declan
 *
 */
public class Move {

	/**
	 * Represents a type of move in chess
	 * 
	 * @author declan
	 *
	 */
	public static enum Flags {
		/**
		 * A move that doesn't capture
		 */
		QUIET,

		/**
		 * A move that captures another piece
		 */
		CAPTURE,

		/**
		 * A move that castles
		 */
		CASTLE,

		/**
		 * A pawn promotion
		 */
		PROMOTION,

		/**
		 * An en passant move
		 */
		EN_PASSANT,

		/**
		 * A double pawn move
		 */
		DOUBLE_PAWN_PUSH;

		/**
		 * Returns the serialized form of the Flag
		 * 
		 * @return the serialized form of the Flag
		 */
		public int value() {
			return this.ordinal();
		}

		/**
		 * Returns true if the given value is a valid serialized Flag
		 * 
		 * @param flag
		 *            the flag value to check the validity of
		 * @return true if the given value is a valid serialized Flag
		 */
		public static boolean isValid(int flag) {
			return QUIET.value() <= flag && flag <= DOUBLE_PAWN_PUSH.value();
		}

		/**
		 * Return the Flag associated with the value
		 * 
		 * @param flag
		 *            the flag value to construct a Flags from
		 * @return the Flag associated with the value
		 */
		public static Flags from(int flag) {
			if (!isValid(flag)) {
				throw new IllegalArgumentException("Flag value is not valid");
			}

			return Flags.values()[flag];
		}

		/**
		 * The number of bits required to fully represent a Flag
		 */
		public static final int BIT_WIDTH = 3;
	}

	private final int startPosition, endPosition;
	private final int flags;
	private final int startPiece, endPiece;
	private final int promotionPieceType;

	/**
	 * Constructs a Move from the given values
	 * 
	 * @param startPiece
	 *            the starting piece of the move
	 * @param endPiece
	 *            the ending piece of the move
	 * @param startPos
	 *            the starting position of the move
	 * @param endPos
	 *            the ending position of the move
	 * @param flag
	 *            the flags for the move
	 * @param promotion
	 *            the promotion piece type of the move, if relevant
	 */
	public Move(int startPiece, int endPiece, int startPos, int endPos, int flag, int promotion) {
		if (!Position.isValid(startPos)) {
			throw new IllegalArgumentException("Start position value is not valid");
		} else if (!Position.isValid(endPos)) {
			throw new IllegalArgumentException("End position value is not valid");
		} else if (!Flags.isValid(flag)) {
			throw new IllegalArgumentException("Flag value is not valid");
		} else if (!ChessPiece.isValid(startPiece)) {
			throw new IllegalArgumentException("Start piece value is not valid");
		} else if (!(ChessPiece.isValid(endPiece) || endPiece == ChessPiece.NULL_PIECE)) {
			throw new IllegalArgumentException(
					"End piece value is not valid or not equal to the null piece value");
		} else if (!(PieceType.isValidPromotion(promotion)
				|| promotion == PieceType.NULL_PROMOTION)) {
			throw new IllegalArgumentException(
					"Piece type value is not a valid promotion or not equal to the null promotion value");
		}

		this.startPiece = startPiece;
		this.endPiece = endPiece;
		this.startPosition = startPos;
		this.endPosition = endPos;
		this.flags = flag;
		this.promotionPieceType = promotion;
	}

	/**
	 * Returns the starting position of the move
	 * 
	 * @return the starting position of the move
	 */
	public int getStartPosition() {
		return startPosition;
	}

	/**
	 * Returns the ending position of the move
	 * 
	 * @return the ending position of the move
	 */
	public int getEndPosition() {
		return endPosition;
	}

	/**
	 * Returns the flags associated with the move
	 * 
	 * @return the flags associated with the move
	 */
	public int getFlags() {
		return flags;
	}

	/**
	 * Returns the serialized ChessPiece that the move originates from
	 * 
	 * @return the serialized ChessPiece that the move originates from
	 */
	public int getStartPiece() {
		return startPiece;
	}

	/**
	 * Returns the serialized ChessPiece that the move ends with
	 * 
	 * @return the serialized ChessPiece that the move ends with
	 */
	public int getEndPiece() {
		return endPiece;
	}

	/**
	 * Returns the serialized PieceType of the promotion, if the move has the
	 * PROMOTION flag
	 * 
	 * @return the serialized PieceType of the promotion
	 */
	public int getPromotionPieceType() {
		return promotionPieceType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(
				"Move[startPos=%s, endPos=%s, startPiece=%s, endPiece=%s, flag=%s, promotion=%s]",
				Position.toString(startPosition), Position.toString(endPosition),
				ChessPiece.from(startPiece),
				(endPiece != ChessPiece.NULL_PIECE) ? ChessPiece.from(endPiece) : "NULL",
				Move.Flags.from(flags), (promotionPieceType != PieceType.NULL_PROMOTION)
						? PieceType.from(promotionPieceType) : "NULL");
	}

	private static final int FLAG_SHIFT = 0;
	private static final int FLAG_MASK = calculateMask(FLAG_SHIFT, Flags.BIT_WIDTH);

	private static final int START_POSITION_SHIFT = FLAG_SHIFT + Flags.BIT_WIDTH;
	private static final int START_POSITION_MASK =
			calculateMask(START_POSITION_SHIFT, Position.BIT_WIDTH);

	private static final int END_POSITION_SHIFT = START_POSITION_SHIFT + (Position.BIT_WIDTH);
	private static final int END_POSITION_MASK =
			calculateMask(END_POSITION_SHIFT, Position.BIT_WIDTH);

	private static final int START_PIECE_SHIFT = END_POSITION_SHIFT + (Position.BIT_WIDTH);
	private static final int START_PIECE_MASK =
			calculateMask(START_PIECE_SHIFT, ChessPiece.BIT_WIDTH);

	private static final int END_PIECE_SHIFT = START_PIECE_SHIFT + (ChessPiece.BIT_WIDTH);
	private static final int END_PIECE_MASK = calculateMask(END_PIECE_SHIFT, ChessPiece.BIT_WIDTH);

	private static final int PROMOTION_PIECE_SHIFT = END_PIECE_SHIFT + (ChessPiece.BIT_WIDTH);
	private static final int PROMOTION_PIECE_MASK =
			calculateMask(PROMOTION_PIECE_SHIFT, ChessPiece.BIT_WIDTH);

	private static int calculateMask(int firstBit, int width) {
		return (Integer.MIN_VALUE >> (width - 1)) >>> (32 - (width + firstBit));
	}

	/**
	 * Returns the serialized form of the Move
	 * 
	 * @return the serialized form of the Move
	 */
	public int value() {
		return (flags << FLAG_SHIFT) | (startPosition << START_POSITION_SHIFT)
				| (endPosition << END_POSITION_SHIFT) | (startPiece << START_PIECE_SHIFT)
				| (endPiece << END_PIECE_SHIFT) | (promotionPieceType << PROMOTION_PIECE_SHIFT);
	}

	/**
	 * Returns the serialized form of the Move, given the necessary values
	 * 
	 * @param startPiece
	 *            the starting piece value
	 * @param endPiece
	 *            the ending piece value
	 * @param startPosition
	 *            the starting position value
	 * @param endPosition
	 *            the ending position value
	 * @param flags
	 *            the value of any flags that apply to the move
	 * @param promotionPieceType
	 *            the type value of the promotion
	 * @return the serialized form of the Move, given the necessary valuess
	 */
	public static int value(int startPiece, int endPiece, int startPosition, int endPosition,
			int flags, int promotionPieceType) {
		return (flags << FLAG_SHIFT) | (startPosition << START_POSITION_SHIFT)
				| (endPosition << END_POSITION_SHIFT) | (startPiece << START_PIECE_SHIFT)
				| (endPiece << END_PIECE_SHIFT) | (promotionPieceType << PROMOTION_PIECE_SHIFT);
	}

	/**
	 * Returns true if the given value is a valid serialized move
	 * 
	 * @param move
	 *            the move value to check the validity of
	 * @return true if the given value is a valid serialized move
	 */
	public static boolean isValid(int move) {
		int flag = (move & FLAG_MASK) >>> FLAG_SHIFT;
		int startPos = (move & START_POSITION_MASK) >>> START_POSITION_SHIFT;
		int endPos = (move & END_POSITION_MASK) >>> END_POSITION_SHIFT;
		int startPiece = (move & START_PIECE_MASK) >>> START_PIECE_SHIFT;
		int endPiece = (move & END_PIECE_MASK) >>> END_PIECE_SHIFT;
		int promotion = (move & PROMOTION_PIECE_MASK) >>> PROMOTION_PIECE_SHIFT;

		return Flags.isValid(flag) && Position.isValid(startPos) && Position.isValid(endPos)
				&& ChessPiece.isValid(startPiece)
				&& (ChessPiece.isValid(endPiece) || endPiece == ChessPiece.NULL_PIECE)
				&& PieceType.isValidPromotion(promotion);
	}

	public static final int NULL_MOVE = -1;

	/**
	 * Returns a Move constructed from the given serialized value
	 * 
	 * @param move
	 *            the move value to construct a Move from
	 * @return a Move constructed from the given serialized value
	 */
	public static Move from(int move) {
		int flag = (move & FLAG_MASK) >>> FLAG_SHIFT;
		int startPos = (move & START_POSITION_MASK) >>> START_POSITION_SHIFT;
		int endPos = (move & END_POSITION_MASK) >>> END_POSITION_SHIFT;
		int startPiece = (move & START_PIECE_MASK) >>> START_PIECE_SHIFT;
		int endPiece = (move & END_PIECE_MASK) >>> END_PIECE_SHIFT;
		int promotion = (move & PROMOTION_PIECE_MASK) >>> PROMOTION_PIECE_SHIFT;

		return (move != NULL_MOVE)
				? new Move(startPiece, endPiece, startPos, endPos, flag, promotion) : null;
	}

	/**
	 * Returns the value of the start position from the given serialized move
	 * 
	 * @param move
	 *            the move value to get the starting position of
	 * @return the value of the start position from the given serialized move
	 */
	public static int getStartPosition(int move) {
		return (move & START_POSITION_MASK) >>> START_POSITION_SHIFT;
	}

	/**
	 * Returns the value of the end position from the given serialized move
	 * 
	 * @param move
	 *            the move value to get the ending position of
	 * @return the value of the end position from the given serialized move
	 */
	public static int getEndPosition(int move) {
		return (move & END_POSITION_MASK) >>> END_POSITION_SHIFT;
	}

	/**
	 * Returns the value of the flags from the given serialized move
	 * 
	 * @param move
	 *            the move value to get the flags of
	 * @return the value of the flags from the given serialized move
	 */
	public static int getFlags(int move) {
		return (move & FLAG_MASK) >>> FLAG_SHIFT;
	}

	/**
	 * Returns the value of the start piece from the given serialized move
	 * 
	 * @param move
	 *            the move value to get the starting piece of
	 * @return the value of the start piece from the given serialized move
	 */
	public static int getStartPiece(int move) {
		return (move & START_PIECE_MASK) >>> START_PIECE_SHIFT;
	}

	/**
	 * Returns the value of the end piece from the given serialized move
	 * 
	 * @param move
	 *            the move value to get the ending piece of
	 * @return the value of the end piece from the given serialized move
	 */
	public static int getEndPiece(int move) {
		return (move & END_PIECE_MASK) >>> END_PIECE_SHIFT;
	}

	/**
	 * Returns the value of the promotion piece type from the given serialized
	 * move
	 * 
	 * @param move
	 *            the move value to get the promotion piece type of
	 * @return the value of the promotion piece type from the given serialized
	 *         move
	 */
	public static int getPromotionPieceType(int move) {
		return (move & PROMOTION_PIECE_MASK) >>> PROMOTION_PIECE_SHIFT;
	}

	/**
	 * Prints a visualization of the serialized move by deconstructing it into
	 * its component partss
	 * 
	 * @param move
	 *            the move value to visualize
	 */
	public static void visualizeMoveContents(int move) {
		printSubset("Total", move, ~0, 0);
		printSubset("Flag", move, FLAG_MASK, FLAG_SHIFT);
		printSubset("Start Pos", move, START_POSITION_MASK, START_POSITION_SHIFT);
		printSubset("End Pos", move, END_POSITION_MASK, END_POSITION_SHIFT);
		printSubset("Start piece", move, START_PIECE_MASK, START_PIECE_SHIFT);
		printSubset("End Piece", move, END_PIECE_MASK, END_PIECE_SHIFT);
		printSubset("Promotion", move, PROMOTION_PIECE_MASK, PROMOTION_PIECE_SHIFT);
	}

	private static void printSubset(String tag, int val, int mask, int shift) {
		System.err.printf("%11s: |%32s|->%+10d\n", tag, Integer.toBinaryString(val & mask),
				(val & mask) >> shift);
		System.err.printf("%13s|%32s|\n", "", Integer.toBinaryString(mask));
	}
}
