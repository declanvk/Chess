package engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.ChessBoard;
import core.ChessPiece;
import core.Move;
import engine.TranspositionTable.Transposition;

/**
 * A wrapper for the MoveGeneration that organizes moves into quiet and capture
 * moves and exposes an iterator for all the moves
 * 
 * @author declan
 *
 */
public class MoveList implements Iterable<Integer> {

	private final ChessBoard position;

	private Transposition pv;
	private final ArrayList<Integer> captures;
	private final ArrayList<Integer> quiet;

	private final ArrayList<Integer> moves;

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
	public MoveList(List<Integer> moves, ChessBoard position, TranspositionTable table) {
		this.position = position;

		if (table.get(position.getZobristKey()) != null) {
			this.pv = table.get(position.getZobristKey());
			if (this.pv.key != position.getZobristKey().getKey()
					|| this.pv.bestMove == Move.NULL_MOVE) {
				this.pv = null;
			}
		}

		this.moves = new ArrayList<Integer>();
		this.captures = new ArrayList<Integer>();
		this.quiet = new ArrayList<Integer>();
		for (Integer move : moves) {
			if (pv != null && move == pv.bestMove) {
				continue;
			}

			if (Move.getEndPiece(move) == ChessPiece.NULL_PIECE) {
				this.quiet.add(move);
			} else {
				this.captures.add(move);
			}
		}

		if (pv != null && pv.bestMove != Move.NULL_MOVE) {
			this.moves.add(pv.bestMove);
		}

		this.moves.addAll(captures);
		this.moves.addAll(quiet);
	}

	public int size() {
		return moves.size();
	}

	@Override
	public Iterator<Integer> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
