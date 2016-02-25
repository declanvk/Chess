package game;

import java.util.HashMap;

public class ChessBoard {

	private final HashMap<Position, ChessPiece>	white, black;

	// Key:
	// hashMoved[a][b]
	// a: [0 - white, 1 - black]
	// b: [0 - king, 1 - kingside rook, 2 - queensize rook]
	private final boolean[][]					hasMoved;

	public ChessBoard() {
		this.white = new HashMap<Position, ChessPiece>();
		this.black = new HashMap<Position, ChessPiece>();

		this.hasMoved = new boolean[2][3];

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
		if (color.equals(ChessColor.BLACK)) {
			return black.get(pos);
		} else {
			return white.get(pos);
		}
	}

	public ChessPiece getPiece(Position pos) {
		return (white.containsKey(pos) ? white.get(pos) : black.get(pos));
	}

	public boolean isEmpty(Position pos) {
		return !white.containsKey(pos) && !black.containsKey(pos);
	}

	public boolean isFileRangeEmpty(int fileStart, int fileEnd, int rank) {
		for (int file = fileStart; file <= fileEnd; file++) {
			if (Position.isValidPosition(file, rank) && !isEmpty(new Position(file, rank))) {
				return false;
			}
		}

		return true;
	}

	public boolean isRankRangeEmpty(int file, int rankStart, int rankEnd) {
		for (int rank = rankStart; rank <= rankEnd; rank++) {
			if (Position.isValidPosition(file, rank) && !isEmpty(new Position(file, rank))) {
				return false;
			}
		}

		return true;
	}

	public boolean isWhite(Position pos) {
		return white.containsKey(pos);
	}

	public boolean isBlack(Position pos) {
		return black.containsKey(pos);
	}

	public boolean isColor(ChessColor color, Position pos) {
		if (color.equals(ChessColor.BLACK)) {
			return isBlack(pos);
		} else {
			return isWhite(pos);
		}
	}

	public HashMap<Position, ChessPiece> getPiecesByColor(ChessColor color) {
		if (color.equals(ChessColor.BLACK)) {
			return black;
		} else {
			return white;
		}
	}

	public boolean hasKingMoved(ChessColor color) {
		return hasMoved[color.getID()][0];
	}

	public boolean hasKingsideRookMoved(ChessColor color) {
		return hasMoved[color.getID()][1];
	}

	public boolean hasQueensideRookMoved(ChessColor color) {
		return hasMoved[color.getID()][2];
	}

	// TODO Implement board update
	public void updateWith(Move move) {

	}
	
	public static boolean checkTabooFileRange(boolean[][] taboo, int fileStart, int fileEnd, int rank) {
		for (int file = fileStart; file <= fileEnd; file++) {
			if (Position.isValidPosition(file, rank) && !taboo[file][rank]) {
				return false;
			}
		}

		return true;
	}
	
	public static boolean checkTabooRankRange(boolean[][] taboo, int file, int rankStart, int rankEnd) {
		for (int rank = rankStart; rank <= rankEnd; rank++) {
			if (Position.isValidPosition(file, rank) && !taboo[file][rank]) {
				return false;
			}
		}

		return true;
	}
}
