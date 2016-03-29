package engine;

import core.ZobristKey;

/**
 * A hash table indexed by ZobristKey that contains Transpositions
 * 
 * @author declan
 *
 */
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
		assert 1 <= startBit && startBit <= 32;
		assert 1 <= length && length <= 32;
		assert (Integer.SIZE - (startBit + length) >= 0);

		return ((-1) >>> (Integer.SIZE - length)) << (startBit - 1);
	}

	/**
	 * A container for useful information about an already evaluated position.
	 * 
	 * @author declan
	 *
	 */
	public static class Transposition {
		/**
		 * The value of the ZobristKey for the stored position
		 */
		public final long key;
		/**
		 * The best move for the stored position
		 */
		public final int bestMove;
		/**
		 * The score evaluation for the stored position
		 */
		public final int value;
		/**
		 * The depth this position was saved at
		 */
		public final int depth;
		/**
		 * The type of stored entry
		 */
		public final int type;

		public Transposition(long key, int move, int value, int depth, int type) {
			this.key = key;
			this.bestMove = move;
			this.value = value;
			this.depth = depth;
			this.type = type;
		}
	}

	/**
	 * The type of stored entry for the transposition table
	 * 
	 * @author declan
	 *
	 */
	public enum TranspositionType {
		EXACT, UPPER, LOWER;

		private final int value;

		private TranspositionType() {
			this.value = this.ordinal();
		}

		/**
		 * Returns the value of the TranspositionType
		 * 
		 * @return the value of the TranspositionType
		 */
		public int value() {
			return value;
		}

		/**
		 * Returns true if the given value is a valid serialized
		 * TranspositionType
		 * 
		 * @param type
		 *            the value to check for validity
		 * @return true if the given value is a valid serialized
		 *         TranspositionType
		 */
		public static boolean isValid(int type) {
			return EXACT.value <= type && type <= LOWER.value;
		}

		public static TranspositionType from(int value) {
			assert isValid(value);

			return TranspositionType.values()[value];
		}
	}

	private final Transposition[] table;
	private final int TABLE_SIZE;
	private final int KEY_MASK;

	/**
	 * Construct new TranspositionTable with key bit size give. The size of the
	 * table is determined from that number.
	 * 
	 * @param bits
	 *            the number of bits that the kye will have
	 */
	public TranspositionTable(int bits) {
		this.TABLE_SIZE = 1 << (bits - 1);
		this.KEY_MASK = createMask(1, bits - 1);
		this.table = new Transposition[TABLE_SIZE];
	}

	/**
	 * Returns the Transposition entry stored at the given key value, or null
	 * 
	 * @param key
	 *            the key value to perform the lookup with
	 * @return the Transposition entry stored at the given key value, or null
	 */
	public Transposition get(ZobristKey key) {
		return table[squashKey(key.getKey())];
	}

	/**
	 * Sets the entry in the table at the given key to the given value,
	 * returning the entry that was stored in that location
	 * 
	 * @param key
	 *            the key value to perform the lookup at
	 * @param value
	 *            the value to set the entry to
	 * @return the entry previously stored in the given location, null if
	 *         nothing
	 */
	public Transposition set(ZobristKey key, Transposition value) {
		Transposition oldValue = table[squashKey(key.getKey())];
		table[squashKey(key.getKey())] = value;
		return oldValue;
	}

	private int squashKey(long key) {
		return (int) (key & KEY_MASK);
	}

}
