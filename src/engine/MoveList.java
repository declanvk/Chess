package engine;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import core.ChessBoard;
import core.ChessPiece;
import core.Move;
import engine.TranspositionTable.Transposition;
import util.Pair;

/**
 * A wrapper for the MoveGeneration that organizes moves into quiet and capture
 * moves and exposes an iterator for all the moves
 * 
 * @author declan
 *
 */
public class MoveList implements Iterable<Integer> {

	private static Comparator<Pair<Integer, Integer>> moveComparator =
			new Comparator<Pair<Integer, Integer>>() {

				@Override
				public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
					return Integer.compare(o2.second(), o1.second());
				}

			};

	private Transposition pv;
	private final PriorityQueue<Pair<Integer, Integer>> moves;

	/**
	 * Constructs a MoveList with a given List of moves, a given position, and a
	 * given transposition table
	 * 
	 * @param moves
	 *            the moves to sort
	 * @param position
	 *            the position to evaluate against
	 * @param table
	 *            the transposition table to search for PV
	 */
	public MoveList(List<Integer> moves, ChessBoard position, TranspositionTable table,
			int[][] killerMoves, int[][][] historyTable, int activeColor, int ply,
			boolean quiescence) {
		if (table.get(position.getZobristKey()) != null) {
			this.pv = table.get(position.getZobristKey());
			if (this.pv.key != position.getZobristKey().getKey()
					|| this.pv.bestMove == Move.NULL_MOVE) {
				this.pv = null;
			}
		}

		this.moves = new PriorityQueue<Pair<Integer, Integer>>(moves.size() > 1 ? moves.size() : 1,
				moveComparator);

		for (Integer move : moves) {
			if (pv != null && move == pv.bestMove && pv.bestMove != Move.NULL_MOVE) {
				this.moves.add(new Pair<Integer, Integer>(move, Integer.MAX_VALUE));
			} else if (Move.getEndPiece(move) == ChessPiece.NULL_PIECE) {
				int value = isKiller(killerMoves, ply, move) ? 100 : 0;
				value += historyTable[activeColor][Move.getStartPosition(move)][Move
						.getEndPiece(move)];
				this.moves.add(new Pair<Integer, Integer>(move, value));
			} else {
				int value = position.staticExchangeEvaluation(move);
				if (!(quiescence && value < 0)) {
					this.moves.add(new Pair<Integer, Integer>(move, value));
				}
			}
		}
	}

	private boolean isKiller(int[][] killerMoves, int ply, int move) {
		for (int killerMove : killerMoves[ply]) {
			if (killerMove == move) {
				return true;
			}
		}
		return false;
	}

	public int size() {
		return moves.size();
	}

	@Override
	public Iterator<Integer> iterator() {
		return new MoveIterator(moves);
	}

	private class MoveIterator implements Iterator<Integer> {

		private final Iterator<Pair<Integer, Integer>> iter;

		public MoveIterator(Collection<Pair<Integer, Integer>> coll) {
			this.iter = coll.iterator();
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public Integer next() {
			return iter.next().first();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
