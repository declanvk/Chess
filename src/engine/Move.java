package engine;

public class Move {

	public static final class MoveFlags {
		public static final short PROMOTION_FLAG = 0b0001;
		public static final short CAPTURES_FLAG = 0b0010;
		public static final short SPECIAL_1_FLAG = 0b0100;
		public static final short SPECIAL_2_FLAG = 0b1000;
	}

	public static final short QUIET_MOVES = 0;
	public static final short DOUBLE_PAWN_PUSH = MoveFlags.SPECIAL_2_FLAG;
	public static final short KING_CASTLE = MoveFlags.SPECIAL_1_FLAG;
	public static final short QUEEN_CASTLE = MoveFlags.SPECIAL_1_FLAG | MoveFlags.SPECIAL_2_FLAG;
	public static final short CAPTURES = MoveFlags.CAPTURES_FLAG;
	public static final short EP_CAPTURE = MoveFlags.CAPTURES_FLAG | MoveFlags.SPECIAL_2_FLAG;
	public static final short KNIGHT_PROMOTION = MoveFlags.PROMOTION_FLAG;
	public static final short BISHOP_PROMOTION = MoveFlags.PROMOTION_FLAG | MoveFlags.SPECIAL_2_FLAG;
	public static final short ROOK_PROMOTION = MoveFlags.PROMOTION_FLAG | MoveFlags.SPECIAL_1_FLAG;
	public static final short QUEEN_PROMOTION = MoveFlags.PROMOTION_FLAG | MoveFlags.SPECIAL_1_FLAG
			| MoveFlags.SPECIAL_2_FLAG;
	public static final short KNIGHT_PROMO_CAPTURE = MoveFlags.PROMOTION_FLAG | MoveFlags.CAPTURES_FLAG;
	public static final short BISHOP_PROMO_CAPTURE = MoveFlags.PROMOTION_FLAG | MoveFlags.CAPTURES_FLAG
			| MoveFlags.SPECIAL_2_FLAG;
	public static final short ROOK_PROMO_CAPTURE = MoveFlags.PROMOTION_FLAG | MoveFlags.CAPTURES_FLAG
			| MoveFlags.SPECIAL_1_FLAG;
	public static final short QUEEN_PROMO_CAPTURE = MoveFlags.PROMOTION_FLAG | MoveFlags.CAPTURES_FLAG
			| MoveFlags.SPECIAL_1_FLAG | MoveFlags.SPECIAL_2_FLAG;

	private final short from, to, flags;

	public Move(short from, short to, short flags) {
		this.from = from;
		this.to = to;
		this.flags = flags;
	}

	/**
	 * @return the from
	 */
	public short getFrom() {
		return from;
	}

	/**
	 * @return the to
	 */
	public short getTo() {
		return to;
	}

	/**
	 * @return the flags
	 */
	public short getFlags() {
		return flags;
	}

	public boolean isCapture() {
		return (flags & MoveFlags.CAPTURES_FLAG) != 0;
	}

	public boolean isPromotion() {
		return (flags & MoveFlags.PROMOTION_FLAG) != 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + flags;
		result = prime * result + from;
		result = prime * result + to;
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
		if (flags != other.flags)
			return false;
		if (from != other.from)
			return false;
		if (to != other.to)
			return false;
		return true;
	}

}
