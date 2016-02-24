package game;

import java.util.HashMap;

public class ChessBoard {

	private final HashMap<Position, ChessPiece> white, black;

	public ChessBoard() {
		this.white = new HashMap<Position, ChessPiece>();
		this.black = new HashMap<Position, ChessPiece>();

		for (int file = 1; file <= 8; file++) {
			white.put(new Position(file, 2), new ChessPiece(ChessColor.WHITE, Piece.PAWN));
			black.put(new Position(file, 7), new ChessPiece(ChessColor.BLACK, Piece.PAWN));
		}

		black.put(new Position(1, 8), new ChessPiece(ChessColor.BLACK, Piece.ROOK));
		black.put(new Position(2, 8), new ChessPiece(ChessColor.BLACK, Piece.KNIGHT));
		black.put(new Position(3, 8), new ChessPiece(ChessColor.BLACK, Piece.BISHOP));
		black.put(new Position(4, 8), new ChessPiece(ChessColor.BLACK, Piece.QUEEN));
		black.put(new Position(5, 8), new ChessPiece(ChessColor.BLACK, Piece.KING));
		black.put(new Position(6, 8), new ChessPiece(ChessColor.BLACK, Piece.BISHOP));
		black.put(new Position(7, 8), new ChessPiece(ChessColor.BLACK, Piece.KNIGHT));
		black.put(new Position(8, 8), new ChessPiece(ChessColor.BLACK, Piece.ROOK));

		white.put(new Position(1, 1), new ChessPiece(ChessColor.WHITE, Piece.ROOK));
		white.put(new Position(2, 1), new ChessPiece(ChessColor.WHITE, Piece.KNIGHT));
		white.put(new Position(3, 1), new ChessPiece(ChessColor.WHITE, Piece.BISHOP));
		white.put(new Position(4, 1), new ChessPiece(ChessColor.WHITE, Piece.QUEEN));
		white.put(new Position(5, 1), new ChessPiece(ChessColor.WHITE, Piece.KING));
		white.put(new Position(6, 1), new ChessPiece(ChessColor.WHITE, Piece.BISHOP));
		white.put(new Position(7, 1), new ChessPiece(ChessColor.WHITE, Piece.KNIGHT));
		white.put(new Position(8, 1), new ChessPiece(ChessColor.WHITE, Piece.ROOK));
	}

	public ChessPiece getPiece(ChessColor color, Position pos) {
		switch (color) {
			case BLACK:
				return black.get(pos);
			case WHITE:
				return white.get(pos);
		}
		return null;
	}

	public ChessPiece getPiece(Position pos) {
		return (white.containsKey(pos) ? white.get(pos) : black.get(pos));
	}

	public boolean isEmpty(Position pos) {
		return !white.containsKey(pos) && !black.containsKey(pos);
	}

	public boolean isWhite(Position pos) {
		return white.containsKey(pos);
	}

	public boolean isBlack(Position pos) {
		return black.containsKey(pos);
	}

	public boolean isColor(ChessColor color, Position pos) {
		switch (color) {
			case BLACK:
				return isBlack(pos);
			case WHITE:
				return isWhite(pos);
		}
		return false;
	}
}
