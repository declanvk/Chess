package game;

import java.io.File;
import java.util.ArrayList;

import gui.ChessSerializable;

@SuppressWarnings("serial")
public class ChessGame implements ChessSerializable {
	
	//board[rank][file]
	private final ChessPiece[][] board = new ChessPiece[8][8];
	private final ArrayList<Move> history = new ArrayList<Move>();
	
	private String name;
	private File save;
	
	public ChessGame(String name) {
		this.name = name;
		
		for(int file = 1; file <= 8; file++) {
			board[1][file - 1] = new ChessPiece(Color.WHITE, Piece.PAWN);
			board[6][file - 1] = new ChessPiece(Color.BLACK, Piece.PAWN);
		}
		
		board[0][0] = new ChessPiece(Color.WHITE, Piece.ROOK);
		board[7][0] = new ChessPiece(Color.BLACK, Piece.ROOK);
		board[0][7] = new ChessPiece(Color.WHITE, Piece.ROOK);
		board[7][7] = new ChessPiece(Color.BLACK, Piece.ROOK);
		
		board[0][1] = new ChessPiece(Color.WHITE, Piece.KNIGHT);
		board[7][1] = new ChessPiece(Color.BLACK, Piece.KNIGHT);
		board[0][6] = new ChessPiece(Color.WHITE, Piece.KNIGHT);
		board[7][6] = new ChessPiece(Color.BLACK, Piece.KNIGHT);
		
		board[0][2] = new ChessPiece(Color.WHITE, Piece.BISHOP);
		board[7][2] = new ChessPiece(Color.BLACK, Piece.BISHOP);
		board[0][5] = new ChessPiece(Color.WHITE, Piece.BISHOP);
		board[7][5] = new ChessPiece(Color.BLACK, Piece.BISHOP);
		
		board[0][3] = new ChessPiece(Color.WHITE, Piece.QUEEN);
		board[7][3] = new ChessPiece(Color.BLACK, Piece.QUEEN);
		
		board[0][4] = new ChessPiece(Color.WHITE, Piece.KING);
		board[7][4] = new ChessPiece(Color.BLACK, Piece.KING);
	}
	
	public ChessPiece getPiece(Position p) {
		return board[p.getRank() - 1][p.getFile() - 1];
	}
	
	public void updateWith(Move move) {
		//TODO Implement
	}
	
	public ArrayList<Move> getMoves(ChessPiece piece, Position position) {
		//TODO Implement
		return null;
	}

	public ArrayList<Move> getHistory() {
		return history;
	}

	@Override
	public String toString() {
		return "ChessGame [name=" + name + "]";
	}
	
	//TODO Unimportant at the moment
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
