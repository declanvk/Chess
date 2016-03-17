package core;

import java.util.EnumSet;

import core.Position.File;
import core.Position.Rank;

/**
 * CastlingBitFlags represent the possibly values of castling permissions.
 * Usually values are combined using either a bitset style or in an EnumSet
 * 
 * @author declan
 *
 */
public enum CastlingBitFlags {
	/**
	 * The white queenside rook is available for castling
	 */
	WHITE_QUEENSIDE(Position.from(File.F_C, Rank.R_1)),

	/**
	 * The white kingside rook is available for castling
	 */
	WHITE_KINGSIDE(Position.from(File.F_G, Rank.R_1)),

	/**
	 * The black queenside rook is available for castling
	 */
	BLACK_QUEENSIDE(Position.from(File.F_C, Rank.R_8)),

	/**
	 * The black kingside rook is available for castling
	 */
	BLACK_KINGSIDE(Position.from(File.F_G, Rank.R_8));

	private final int endPosition;
	private final int midPosition;
	private final int value;
	private final int color;
	private final int side;

	private CastlingBitFlags(int endPos) {
		this.endPosition = endPos;
		this.value = 1 << this.ordinal();
		this.color = this.ordinal() < 2 ? 0 : 1;
		this.side = this.ordinal() % 2;
		this.midPosition = this.endPosition + (this.side == 0 ? Position.E : Position.W);
	}

	/**
	 * Returns the position that the king would be in if it was to castle in
	 * that direction
	 * 
	 * @return the end position for the king
	 */
	public int getEndPosition() {
		return endPosition;
	}

	/**
	 * Returns the position that the king would pass over if it was to castle in
	 * that direction
	 * 
	 * @return the position that the king would pass over
	 */
	public int getMidPosition() {
		return midPosition;
	}

	/**
	 * Returns the serialized value of the enum
	 * 
	 * @return the value of the enum
	 */
	public int value() {
		return value;
	}

	/**
	 * Returns the color of the move
	 * 
	 * @return the color of the move
	 */
	public int color() {
		return color;
	}

	/**
	 * Returns the side that the king would move to, either kingside or
	 * queenside
	 * 
	 * @return the side that the moves takes place on
	 */
	public int side() {
		return side;
	}

	/**
	 * Value of an empty castling permissions bitset
	 */
	public static final int NO_CASTLING = 0;

	/**
	 * Returns the serialized value of an EnumSet
	 * 
	 * @param castleFlags
	 * @return the serialized value of an EnumSet
	 */
	public static int value(EnumSet<CastlingBitFlags> castleFlags) {
		int total = 0;
		for (CastlingBitFlags flag : castleFlags) {
			total |= flag.value();
		}

		return total;
	}

	/**
	 * Returns the validity of a serialized bitset
	 * 
	 * @param castleFlags
	 * @return the validity of a serialized bitset
	 */
	public static boolean isValid(int castleFlags) {
		return 0 < castleFlags && castleFlags < 16;
	}

	/**
	 * Returns the validity of a single serialized value
	 * 
	 * @param castleFlag
	 * @return the validity of a single serialized value
	 */
	public static boolean isValidSingle(int castleFlag) {
		return isValid(castleFlag) && (castleFlag & (castleFlag - 1)) == 0;
	}

	/**
	 * Deserializes a serialized bitset, checking for validity
	 * 
	 * @param castleFlags
	 * @return a deserialized bitset
	 */
	public static EnumSet<CastlingBitFlags> from(int castleFlags) {
		assert isValid(castleFlags) || castleFlags == 0;

		EnumSet<CastlingBitFlags> set = EnumSet.noneOf(CastlingBitFlags.class);
		while (castleFlags != 0) {
			set.add(CastlingBitFlags.values()[Integer.numberOfTrailingZeros(castleFlags)]);
			castleFlags &= ~Integer.lowestOneBit(castleFlags);
		}

		return set;
	}

	/**
	 * Deserializes a single value
	 * 
	 * @param castleFlag
	 * @return a deserialized single value
	 */
	public static CastlingBitFlags fromSingle(int castleFlag) {
		assert isValidSingle(castleFlag);

		return CastlingBitFlags.values()[Integer.lowestOneBit(castleFlag) - 1];
	}
}
