package core;

import java.util.EnumSet;

import core.Position.File;
import core.Position.Rank;

public enum CastlingBitFlags {
	WHITE_QUEENSIDE(Position.from(File.F_C, Rank.R_1)),
	WHITE_KINGSIDE(Position.from(File.F_G, Rank.R_1)),
	BLACK_QUEENSIDE(Position.from(File.F_C, Rank.R_8)),
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

	public int getEndPosition() {
		return endPosition;
	}

	public int getMidPosition() {
		return midPosition;
	}

	public int value() {
		return value;
	}

	public int color() {
		return color;
	}

	public int side() {
		return side;
	}

	public static final int NO_CASTLING = 0;

	public static int value(EnumSet<CastlingBitFlags> castleFlags) {
		int total = 0;
		for (CastlingBitFlags flag : castleFlags) {
			total |= flag.value();
		}

		return total;
	}

	public static boolean isValid(int castleFlags) {
		return 0 < castleFlags && castleFlags < 16;
	}

	public static boolean isValidSingle(int castleFlag) {
		return isValid(castleFlag) && (castleFlag & (castleFlag - 1)) == 0;
	}

	public static EnumSet<CastlingBitFlags> from(int castleFlags) {
		assert isValid(castleFlags) || castleFlags == 0;

		EnumSet<CastlingBitFlags> set = EnumSet.noneOf(CastlingBitFlags.class);
		while (castleFlags != 0) {
			set.add(CastlingBitFlags.values()[Integer.numberOfTrailingZeros(castleFlags)]);
			castleFlags &= ~Integer.lowestOneBit(castleFlags);;
		}

		return set;
	}

	public static CastlingBitFlags fromSingle(int castleFlag) {
		assert isValidSingle(castleFlag);

		return CastlingBitFlags.values()[Integer.lowestOneBit(castleFlag) - 1];
	}
}
