package core;

public class Move {

	public static enum Flags {
		QUIET, CAPTURE, CASTLE, PROMOTION, EN_PASSANT, DOUBLE_PAWN_PUSH;

		public int value() {
			return this.ordinal();
		}

		public static boolean isValid(int flag) {
			return QUIET.value() <= flag && flag <= DOUBLE_PAWN_PUSH.value();
		}

		public static Flags from(int flag) {
			assert isValid(flag);

			return Flags.values()[flag];
		}

		public static final int BIT_WIDTH = 3;
	}

	private final int startPosition, endPosition;
	private final int flags;
	private final int startPiece, endPiece;
	private final int promotionPieceType;

	public Move(int startPiece, int endPiece, int startPos, int endPos, int flag, int promotion) {
		assert Position.isValid(startPos);
		assert Position.isValid(endPos);
		assert Flags.isValid(flag);
		assert ChessPiece.isValid(startPiece);
		if (!(ChessPiece.isValid(endPiece) || endPiece == ChessPiece.NULL_PIECE))
			System.out.println(endPiece);
		assert ChessPiece.isValid(endPiece) || endPiece == ChessPiece.NULL_PIECE;
		assert PieceType.isValidPromotion(promotion) || promotion == -1;

		this.startPiece = startPiece;
		this.endPiece = endPiece;
		this.startPosition = startPos;
		this.endPosition = endPos;
		this.flags = flag;
		this.promotionPieceType = promotion;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public int getFlags() {
		return flags;
	}

	public int getStartPiece() {
		return startPiece;
	}

	public int getEndPiece() {
		return endPiece;
	}

	public int getPromotionPieceType() {
		return promotionPieceType;
	}

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
			calculateMask(START_POSITION_SHIFT, Position.BIT_WIDTH - 1);

	private static final int END_POSITION_SHIFT = START_POSITION_SHIFT + (Position.BIT_WIDTH - 1);
	private static final int END_POSITION_MASK =
			calculateMask(END_POSITION_SHIFT, Position.BIT_WIDTH - 1);

	private static final int START_PIECE_SHIFT = END_POSITION_SHIFT + (Position.BIT_WIDTH);
	private static final int START_PIECE_MASK =
			calculateMask(START_PIECE_SHIFT, ChessPiece.BIT_WIDTH + 1);

	private static final int END_PIECE_SHIFT = START_PIECE_SHIFT + (ChessPiece.BIT_WIDTH + 1);
	private static final int END_PIECE_MASK =
			calculateMask(END_PIECE_SHIFT, ChessPiece.BIT_WIDTH + 1);

	private static final int PROMOTION_PIECE_SHIFT = END_PIECE_SHIFT + (ChessPiece.BIT_WIDTH + 1);
	private static final int PROMOTION_PIECE_MASK =
			calculateMask(PROMOTION_PIECE_SHIFT, ChessPiece.BIT_WIDTH);

	private static int calculateMask(int firstBit, int width) {
		return (Integer.MIN_VALUE >> (width - 1)) >>> (32 - (width + firstBit));
	}

	public int value() {
		return (flags << FLAG_SHIFT) | (startPosition << START_POSITION_SHIFT)
				| (endPosition << END_POSITION_SHIFT) | (startPiece << START_PIECE_SHIFT)
				| (endPiece << END_PIECE_SHIFT) | (promotionPieceType << PROMOTION_PIECE_SHIFT);
	}

	public static int value(int startPiece, int endPiece, int startPosition, int endPosition,
			int flags, int promotionPieceType) {
		return (flags << FLAG_SHIFT) | (startPosition << START_POSITION_SHIFT)
				| (endPosition << END_POSITION_SHIFT) | (startPiece << START_PIECE_SHIFT)
				| (endPiece << END_PIECE_SHIFT) | (promotionPieceType << PROMOTION_PIECE_SHIFT);
	}

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

	public static int getStartPosition(int move) {
		return (move & START_POSITION_MASK) >>> START_POSITION_SHIFT;
	}

	public static int getEndPosition(int move) {
		return (move & END_POSITION_MASK) >>> END_POSITION_SHIFT;
	}

	public static int getFlags(int move) {
		return (move & FLAG_MASK) >>> FLAG_SHIFT;
	}

	public static int getStartPiece(int move) {
		return (move & START_PIECE_MASK) >>> START_PIECE_SHIFT;
	}

	public static int getEndPiece(int move) {
		return (move & END_PIECE_MASK) >>> END_PIECE_SHIFT;
	}

	public static int getPromotionPieceType(int move) {
		return (move & PROMOTION_PIECE_MASK) >>> PROMOTION_PIECE_SHIFT;
	}

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
