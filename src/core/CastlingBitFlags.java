package core;

import java.util.EnumSet;

import core.Position.File;
import core.Position.Rank;

public enum CastlingBitFlags {
	WHITE_QUEENSIDE(Position.from(File.F_A, Rank.R_1)),
	WHITE_KINGSIDE(Position.from(File.F_H, Rank.R_1)),
	BLACK_QUEENSIDE(Position.from(File.F_A, Rank.R_8)),
	BLACK_KINGSIDE(Position.from(File.F_H, Rank.R_8));

	private final int	position;
	private final int	value;

	private CastlingBitFlags(int position) {
		this.position = position;
		this.value = 1 << this.ordinal();
	}

	public int getPosition() {
		return position;
	}

	public int value() {
		return value;
	}
	
	public static final int NO_CASTLING = 0;
	
	public static int value(EnumSet<CastlingBitFlags> castleFlags) {
		int total = 0;
		for(CastlingBitFlags flag: castleFlags) {
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
		int bitPosition;
		while (castleFlags != 0) {
			bitPosition = Integer.lowestOneBit(castleFlags);
			set.add(CastlingBitFlags.values()[bitPosition - 1]);
			castleFlags ^= 1 << bitPosition;
		}

		return set;
	}

	public static CastlingBitFlags fromSingle(int castleFlag) {
		assert isValidSingle(castleFlag);

		return CastlingBitFlags.values()[Integer.lowestOneBit(castleFlag) - 1];
	}
}
