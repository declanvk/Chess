package core;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Representation of a baord stored in the bits of a primitive long. The
 * position a1 is stored in the lowest bit, and the position h8 in the highest
 * bit. The benefit of this representation derives from the ability to perform
 * bit shifting operations on it, for use in move generation or other utilities,
 * gaining a large amount of parallel operations with a fairly cheap
 * implementation. A note about the position common position parameter, it is
 * always formated in 0x88 format unless otherwise specified.
 * 
 * @author declan
 *
 */
/**
 * @author declan
 *
 */
public class Bitboard implements Iterable<Integer> {

	/**
	 * Interface for a function to operate on the inner representation of the
	 * Bitboard. Typically used when one needs to perform several operations in
	 * sequence and update the Bitboard with the result.
	 *
	 */
	public interface BitboardOperation {

		/**
		 * Used to perform operations on the internal board representation.
		 * 
		 * @param board
		 *            the internal value of the Bitboard
		 * @return the new value that the Bitboard should contain.
		 */
		long operate(long board);
	}

	private long board;
	private BitboardIterator iterator;

	/**
	 * Constructs an empty Bitboard
	 */
	public Bitboard() {
		board = 0L;
		iterator = null;
	}

	/**
	 * Counts the number of set bits in the Bitboard
	 * 
	 * @return the number of set bits
	 */
	public int size() {
		return size(board);
	}

	/**
	 * Sets the bit at the specified position.
	 * 
	 * @param position
	 *            the position to set
	 */
	public void set(int position) {
		checkIterator();

		this.board = set(board, position);
	}

	/**
	 * Clears the bit at the specified position.
	 * 
	 * @param position
	 *            the position to clear
	 */
	public void clear(int position) {
		checkIterator();

		this.board = clear(board, position);
	}

	/**
	 * Toggles the bit at the specified position.
	 * 
	 * @param position
	 *            the position to toggle
	 */
	public void toggle(int position) {
		checkIterator();

		this.board = toggle(board, position);
	}

	/**
	 * Checks if the bit at the specified position is set
	 * 
	 * @param position
	 *            the position to check
	 * @return if the bit at the specified position is set
	 */
	public boolean check(int position) {
		return check(board, position);
	}

	/**
	 * If the size of the Bitboard is equal to 1, returns the position of the
	 * single bit in 0x88 format.
	 * 
	 * @return the position of the single set bit
	 */
	public int getSingle() {
		assert size() == 1;

		return Position.getPosition(Long.numberOfTrailingZeros(this.board));
	}

	/**
	 * Returns the underlying value of the Bitboard
	 * 
	 * @return the underlying value of the Bitboard
	 */
	public long value() {
		return board;
	}

	/**
	 * Used to operate on the underlying representation of the Bitboard without
	 * removing it from the context of the Bitboard.
	 * 
	 * @param bitboardOperation
	 *            the operation to execute
	 */
	public void operate(BitboardOperation bitboardOperation) {
		this.board = bitboardOperation.operate(this.board);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Integer> iterator() {
		iterator = new BitboardIterator(this);
		return iterator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.size() + ": " + Long.toBinaryString(board);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Bitboard clone() {
		return Bitboard.from(this.board);
	}

	/**
	 * Returns the inverse of the underlying representation. Typically used for
	 * masking purposes.
	 * 
	 * @return the inverse of the Bitboard
	 */
	public Bitboard opposite() {
		return Bitboard.from(~this.board);
	}

	private void checkIterator() {
		if (iterator != null && iterator.index < 64) {
			throw new ConcurrentModificationException(
					"Cannot modify the bitboard while it is being iterated over.");
		}
	}

	private class BitboardIterator implements Iterator<Integer> {

		private final Bitboard board;
		protected int index = 0;
		protected boolean removeCalled = false;

		public BitboardIterator(Bitboard board) {
			this.board = board;
		}

		@Override
		public boolean hasNext() {
			return index < 64 && Long.bitCount(board.board >>> index) > 0;
		}

		@Override
		public Integer next() {
			if (index >= 64) {
				throw new NoSuchElementException("Index: " + index);
			}

			int bitIndex = Long.numberOfTrailingZeros(board.board >>> index);
			index += bitIndex + 1;
			removeCalled = false;
			return Position.getPosition(index - 1);
		}

		@Override
		public void remove() {
			if (removeCalled || index == 0) {
				throw new IllegalStateException();
			}
			board.board = clear(board.board, Position.getPosition(index - 1));
		}

	}

	private static int size(long board) {
		return Long.bitCount(board);
	}

	private static long set(long board, int position) {
		return board | 1L << (Position.getBitIndex(position));
	}

	private static long clear(long board, int position) {
		return board & ~(1L << (Position.getBitIndex(position)));
	}

	private static long toggle(long board, int position) {
		return board ^ 1L << (Position.getBitIndex(position));
	}

	private static boolean check(long board, int position) {
		return ((board >>> Position.getBitIndex(position)) & 1L) == 0;
	}

	/**
	 * Factory style method used to construct Bitboards from a primitive long
	 * 
	 * @param board
	 *            the value to construct a board from
	 * @return new Bitboard containing the board parameter
	 */
	public static Bitboard from(long board) {
		Bitboard newBoard = new Bitboard();
		newBoard.board = board;
		return newBoard;
	}

	/**
	 * Combines many Bitboards into one using the &amp; operator.
	 * 
	 * @param boards
	 *            the boards to combine
	 * @return a Bitboard containing the result of "anding" all the boards
	 *         together
	 */
	public static Bitboard and(Bitboard... boards) {
		long result = ~(0L);

		for (int i = 0; i < boards.length; i++) {
			result &= boards[i].board;
		}

		return Bitboard.from(result);
	}

	/**
	 * Combines many Bitboards into one using the | operator.
	 * 
	 * @param boards
	 *            the boards to combine
	 * @return a Bitboard containing the result of "oring" all the boards
	 *         together
	 */
	public static Bitboard or(Bitboard... boards) {
		long result = 0L;

		for (int i = 0; i < boards.length; i++) {
			result |= boards[i].board;
		}

		return Bitboard.from(result);
	}

	/**
	 * Combines many Bitboards into one using the ^ operator.
	 * 
	 * @param boards
	 *            the boards to combine
	 * @return a Bitboard containing the result of "xoring" all the boards
	 *         together
	 */
	public static Bitboard xor(Bitboard... boards) {
		long result = (boards.length > 0 ? boards[0].board : 0L);

		for (int i = 1; i < boards.length; i++) {
			result ^= boards[i].board;
		}

		return Bitboard.from(result);
	}

}
