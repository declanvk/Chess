package engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.ChessBoard;
import core.ChessPiece;
import core.Move;
import core.Move.Flags;
import engine.TranspositionTable.Transposition;

public class MoveList implements Iterator<Move> {

	private final ChessBoard position;
	private final TranspositionTable table;

	private Transposition pv;
	private final ArrayList<Move> captures;
	private final ArrayList<Move> quiet;

	private final ArrayList<Move> moves;
	private final Iterator<Move> movesIter;

	public MoveList(List<Integer> moves, ChessBoard position, TranspositionTable table) {
		this.position = position;
		this.table = table;

		this.pv = (table.get(position.getZobristKey()).key == position.getZobristKey().getKey())
				? table.get(position.getZobristKey()) : null;

		this.moves = new ArrayList<Move>();
		this.captures = new ArrayList<Move>();
		this.quiet = new ArrayList<Move>();
		for (Integer move : moves) {
			if (pv != null && move == pv.bestMove) {
				continue;
			}

			Move m = Move.from(move);
			if (m.getFlags() == Flags.QUIET.value()
					|| m.getFlags() == Flags.DOUBLE_PAWN_PUSH.value()
					|| m.getFlags() == Flags.CASTLE.value()) {
				this.quiet.add(m);
			} else if (m.getFlags() == Flags.CAPTURE.value()
					|| m.getEndPiece() != ChessPiece.NULL_PIECE) {
				this.captures.add(m);
			} else {
				assert false;
			}
		}

		if (pv != null) {
			this.moves.add(Move.from(pv.bestMove));
		}

		this.moves.addAll(captures);
		this.moves.addAll(quiet);
		this.movesIter = this.moves.iterator();
	}

	@Override
	public boolean hasNext() {
		return movesIter.hasNext();
	}

	@Override
	public Move next() {
		return movesIter.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
