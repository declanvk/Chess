package engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.ChessBoard;
import core.ChessPiece;
import core.Move;
import core.Move.Flags;
import engine.TranspositionTable.Transposition;

public class MoveList implements Iterable<Integer> {

	private final ChessBoard position;

	private Transposition pv;
	private final ArrayList<Integer> captures;
	private final ArrayList<Integer> quiet;

	private final ArrayList<Integer> moves;

	public MoveList(List<Integer> moves, ChessBoard position, TranspositionTable table) {
		this.position = position;

		this.pv = (table.get(position.getZobristKey()) != null
				&& table.get(position.getZobristKey()).key == position.getZobristKey().getKey())
						? table.get(position.getZobristKey()) : null;

		this.moves = new ArrayList<Integer>();
		this.captures = new ArrayList<Integer>();
		this.quiet = new ArrayList<Integer>();
		for (Integer move : moves) {
			if (pv != null && move == pv.bestMove) {
				continue;
			}

			if (Move.getFlags(move) == Flags.QUIET.value()
					|| Move.getFlags(move) == Flags.DOUBLE_PAWN_PUSH.value()
					|| Move.getFlags(move) == Flags.CASTLE.value()) {
				this.quiet.add(move);
			} else if (Move.getFlags(move) == Flags.CAPTURE.value()
					|| Move.getEndPiece(move) != ChessPiece.NULL_PIECE) {
				this.captures.add(move);
			} else {
				assert false;
			}
		}

		if (pv != null) {
			this.moves.add(pv.bestMove);
		}

		this.moves.addAll(captures);
		this.moves.addAll(quiet);
	}

	@Override
	public Iterator<Integer> iterator() {
		return moves.iterator();
	}

}
