package game;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;

import gui.ChessSerializable;

@SuppressWarnings("serial")
public class ChessGame {

	// board[file][rank]
	private final ChessBoard			board		= new ChessBoard();
	private final boolean[][]			tabooBoard	= new boolean[8][8];

	private final ArrayDeque<Move>		history		= new ArrayDeque<Move>();
	private final ArrayList<ChessPiece>	taken		= new ArrayList<ChessPiece>();

	private String						name;
	private File						save;

	public ChessGame(String name) {
		this.name = name;
	}

	public ArrayDeque<Move> getHistory() {
		return history;
	}

	@Override
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

	public void updateWith(Move move) {
		board.updateWith(move);
		this.tabooBoard = MoveGeneration.generateTabooBoard
	}
}
