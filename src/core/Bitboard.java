package core;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Bitboard implements Iterable<Integer> {

	public interface BitboardOperation {
		long operate(long board);
	}

	private long board;
	private BitboardIterator iterator;

	public Bitboard() {
		board = 0L;
		iterator = null;
	}

	public int size() {
		return size(board);
	}

	public void set(int position) {
		checkIterator();

		this.board = set(board, position);
	}

	public void clear(int position) {
		checkIterator();

		this.board = clear(board, position);
	}

	public void toggle(int position) {
		checkIterator();

		this.board = toggle(board, position);
	}

	public boolean check(int position) {
		return check(board, position);
	}

	public int getSingle() {
		assert size() == 1;

		return Position.getPosition(Long.numberOfTrailingZeros(this.board));
	}

	public long value() {
		return board;
	}

	public void operate(BitboardOperation bitboardOperation) {
		this.board = bitboardOperation.operate(this.board);
	}

	@Override
	public Iterator<Integer> iterator() {
		iterator = new BitboardIterator(this);
		return iterator;
	}

	@Override
	public String toString() {
		return this.size() + ": " + Long.toBinaryString(board);
	}

	public Bitboard clone() {
		return Bitboard.from(this.board);
	}

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

	public static Bitboard from(long board) {
		Bitboard newBoard = new Bitboard();
		newBoard.board = board;
		return newBoard;
	}

	public static Bitboard and(Bitboard... boards) {
		long result = ~(0L);

		for (int i = 0; i < boards.length; i++) {
			result &= boards[i].board;
		}

		return Bitboard.from(result);
	}

	public static Bitboard or(Bitboard... boards) {
		long result = 0L;

		for (int i = 0; i < boards.length; i++) {
			result |= boards[i].board;
		}

		return Bitboard.from(result);
	}

	public static Bitboard xor(Bitboard... boards) {
		long result = (boards.length > 0 ? boards[0].board : 0L);

		for (int i = 1; i < boards.length; i++) {
			result ^= boards[i].board;
		}

		return Bitboard.from(result);
	}

}
