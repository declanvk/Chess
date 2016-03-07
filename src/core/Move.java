package core;

public class Move {

	public static enum Flag {
		QUIET, CAPTURE, CASTLE, PROMOTION, EN_PASSANT, DOUBLE_PAWN_PUSH;

		public int value() {
			return this.ordinal();
		}

		public static boolean isValid(int flag) {
			return QUIET.value() >= flag && flag <= DOUBLE_PAWN_PUSH.value();
		}

		public static Flag from(int flag) {
			assert isValid(flag);

			return Flag.values()[flag];
		}
	}

	private final int	startPosition, endPosition;
	private final int	flags;
	private final int	startPiece, endPiece;
	private final int	promotionPieceType;

	public Move(int startPiece, int endPiece, int startPos, int endPos, int flag, int promotion) {
		assert Position.isValid(startPos);
		assert Position.isValid(endPos);
		assert Flag.isValid(flag);
		assert ChessPiece.isValid(startPiece);
		assert ChessPiece.isValid(endPiece);
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

	private static final int	FLAG_SHIFT				= 0;
	private static final int	FLAG_MASK				= calculateMask(FLAG_SHIFT, 3);

	private static final int	START_POSITION_SHIFT	= 3;
	private static final int	START_POSITION_MASK		= calculateMask(START_POSITION_SHIFT, 7);

	private static final int	END_POSITION_SHIFT		= 10;
	private static final int	END_POSITION_MASK		= calculateMask(END_POSITION_SHIFT, 7);

	private static final int	START_PIECE_SHIFT		= 17;
	private static final int	START_PIECE_MASK		= calculateMask(START_PIECE_SHIFT, 5);

	private static final int	END_PIECE_SHIFT			= 22;
	private static final int	END_PIECE_MASK			= calculateMask(END_PIECE_SHIFT, 5);

	private static final int	PROMOTION_PIECE_SHIFT	= 27;
	private static final int	PROMOTION_PIECE_MASK	= calculateMask(PROMOTION_PIECE_SHIFT, 5);

	private static int calculateMask(int firstBit, int width) {
		return (Integer.MIN_VALUE >> (width - 1)) >>> (32 - (width + firstBit));
	}

	public int value() {
		return (flags << FLAG_SHIFT) | (startPosition << START_POSITION_SHIFT)
				| (endPosition << END_POSITION_SHIFT) | (startPiece << START_PIECE_SHIFT)
				| (endPiece << END_PIECE_SHIFT) | (promotionPieceType << PROMOTION_PIECE_SHIFT);
	}

	public static Move from(int move) {
		// TODO implement integer -> move conversion
		int flag = (move & FLAG_MASK) >> FLAG_SHIFT;
		int startPos = (move & START_POSITION_MASK) >> START_POSITION_SHIFT;
		int endPos = (move & END_POSITION_MASK) >> END_POSITION_SHIFT;
		int startPiece = (move & START_PIECE_MASK) >> START_PIECE_SHIFT;
		int endPiece = (move & END_PIECE_MASK) >> END_PIECE_SHIFT;
		int promotion = (move & PROMOTION_PIECE_MASK) >> PROMOTION_PIECE_SHIFT;

		return new Move(startPiece, endPiece, startPos, endPos, flag, promotion);
	}
}
