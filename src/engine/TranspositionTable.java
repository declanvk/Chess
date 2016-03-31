package engine;

import core.ZobristKey;

public class TranspositionTable {

	/**
	 * Returns a mask from the starting bit with the specified length. The
	 * acceptable range of values for a bit is from 1-32.
	 * 
	 * @param startBit
	 *            the bit to start the mask at
	 * @param length
	 *            the length of the mask
	 * @return a mask from the starting bit to the ending bit
	 */
	private static int createMask(int startBit, int length) {
		if (!(1 <= startBit && startBit <= 32)) {
			throw new IllegalArgumentException("Starting bit value is not valid");
		} else if (!(1 <= length && length <= 32)) {
			throw new IllegalArgumentException("Length value is not valid");
		} else if (!(Integer.SIZE - (startBit + length) >= 0)) {
			throw new IllegalArgumentException(
					"Combination of starting bit and length values is not valid");
		}

		return ((-1) >>> (Integer.SIZE - length)) << (startBit - 1);
	}

	public static class Transposition {
		public final long key;
		public final int bestMove;
		public final int value;
		public final int depth;
		public final int type;

		public Transposition(long key, int move, int value, int depth, int type) {
			this.key = key;
			this.bestMove = move;
			this.value = value;
			this.depth = depth;
			this.type = type;
		}
	}

	public enum TranspositionType {
		EXACT, UPPER, LOWER;

		private final int value;

		private TranspositionType() {
			this.value = this.ordinal();
		}

		public int value() {
			return value;
		}

		public static boolean isValid(int type) {
			return EXACT.value <= type && type <= LOWER.value;
		}

		public static TranspositionType from(int value) {
			if (!isValid(value)) {
				throw new IllegalArgumentException("Given value is not valid");
			}

			return TranspositionType.values()[value];
		}
	}

	private final Transposition[] table;
	private final int TABLE_SIZE;
	private final int KEY_MASK;

	public TranspositionTable(int bits) {
		this.TABLE_SIZE = 1 << (bits - 1);
		this.KEY_MASK = createMask(1, bits - 1);
		this.table = new Transposition[TABLE_SIZE];
	}

	public Transposition get(ZobristKey key) {
		return table[squashKey(key.getKey())];
	}

	public Transposition set(ZobristKey key, Transposition value) {
		Transposition oldValue = table[squashKey(key.getKey())];
		table[squashKey(key.getKey())] = value;
		return oldValue;
	}

	private int squashKey(long key) {
		return (int) (key & KEY_MASK);
	}

}
