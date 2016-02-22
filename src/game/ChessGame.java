package game;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;

import gui.ChessSerializable;

@SuppressWarnings("serial")
public class ChessGame implements ChessSerializable {

	// board[file][rank]
	private final ChessPiece[][]	board	= new ChessPiece[8][8];
	private final ArrayDeque<Move>	history	= new ArrayDeque<Move>();

	private String					name;
	private File					save;

	public ChessGame(String name) {
		this.name = name;

		for (int file = 1; file <= 8; file++) {
			board[1][file - 1] = new ChessPiece(ChessColor.WHITE, Piece.PAWN);
			board[6][file - 1] = new ChessPiece(ChessColor.BLACK, Piece.PAWN);
		}

		board[0][0] = new ChessPiece(ChessColor.WHITE, Piece.ROOK);
		board[7][0] = new ChessPiece(ChessColor.BLACK, Piece.ROOK);
		board[0][7] = new ChessPiece(ChessColor.WHITE, Piece.ROOK);
		board[7][7] = new ChessPiece(ChessColor.BLACK, Piece.ROOK);

		board[0][1] = new ChessPiece(ChessColor.WHITE, Piece.KNIGHT);
		board[7][1] = new ChessPiece(ChessColor.BLACK, Piece.KNIGHT);
		board[0][6] = new ChessPiece(ChessColor.WHITE, Piece.KNIGHT);
		board[7][6] = new ChessPiece(ChessColor.BLACK, Piece.KNIGHT);

		board[0][2] = new ChessPiece(ChessColor.WHITE, Piece.BISHOP);
		board[7][2] = new ChessPiece(ChessColor.BLACK, Piece.BISHOP);
		board[0][5] = new ChessPiece(ChessColor.WHITE, Piece.BISHOP);
		board[7][5] = new ChessPiece(ChessColor.BLACK, Piece.BISHOP);

		board[0][3] = new ChessPiece(ChessColor.WHITE, Piece.QUEEN);
		board[7][3] = new ChessPiece(ChessColor.BLACK, Piece.QUEEN);

		board[0][4] = new ChessPiece(ChessColor.WHITE, Piece.KING);
		board[7][4] = new ChessPiece(ChessColor.BLACK, Piece.KING);
	}

	public ChessPiece getPiece(Position p) {
		return board[p.getFile() - 1][p.getRank() - 1];
	}

	public void updateWith(Move move) {
		// TODO Implement
	}

	public ArrayList<Move> getMoves(ChessPiece piece, Position position) {
		switch (piece.getType()) {
			case PAWN:
				break;
			case ROOK:
				break;
			case KNIGHT:
				break;
			case BISHOP:
				break;
			case QUEEN:
				break;
			case KING:
				break;
		}
		return null;
	}

	public ArrayDeque<Move> getHistory() {
		return history;
	}

	@Override
	public String toString() {
		return "ChessGame [name=" + name + "]";
	}

	// TODO Unimportant at the moment
	@Override
	public boolean isSaved() {
		return save != null;
	}

	@Override
	public File getSave() {
		return save;
	}

	@Override
	public void setSave(File f) {
		this.save = f;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String s) {
		this.name = s;
	}

	@Override
	public String getSuffix() {
		return "chess";
	}
}
