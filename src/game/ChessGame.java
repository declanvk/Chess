package game;

import java.util.ArrayDeque;

public class ChessGame {

	// board[file][rank]
	private final ChessBoard		board		= new ChessBoard();

	private final ArrayDeque<Move>	history		= new ArrayDeque<Move>();

	private String					name;

	private ChessColor				turnToggle	= ChessColor.WHITE;			// True
																			// =
																			// white,
																			// false
																			// =
																			// black

	public ChessGame(String name) {
		this.name = name;
	}

	public ArrayDeque<Move> getHistory() {
		return history;
	}

	public String getName() {
		return name;
	}

	public ChessPiece getPiece(Position p) {
		return board.getPiece(p);
	}

	@Override
	public String toString() {
		return "ChessGame [name=" + name + "]";
	}

	// Update board repr
	// Add to history
	// Check for check or win
	public void updateWith(Move move) {
		if (move.getPiece().getColor() != turnToggle) {
			throw new IllegalArgumentException(
					"The Move passed indicates that a Played moved out of turn. It is the "
							+ turnToggle + " player's turn.");
		}
		board.updateWith(move);
		history.add(move);
	}
}