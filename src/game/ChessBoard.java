package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import game.Move.MoveFlags;

public class ChessBoard {
	
	private static final HashMap<Position, ChessPiece> initialWhite, initialBlack;
	static {
		HashMap<Position, ChessPiece> tempWhite = new HashMap<Position, ChessPiece>();
		HashMap<Position, ChessPiece> tempBlack = new HashMap<Position, ChessPiece>();
		
		for (int file = 1; file <= 8; file++) {
            tempWhite.put(new Position(file, 2), new ChessPiece(ChessColor.WHITE, Piece.PAWN));
            tempBlack.put(new Position(file, 7), new ChessPiece(ChessColor.BLACK, Piece.PAWN));
        }

        tempBlack.put(new Position(1, 8), new ChessPiece(ChessColor.BLACK, Piece.ROOK));
        tempBlack.put(new Position(2, 8), new ChessPiece(ChessColor.BLACK, Piece.KNIGHT));
        tempBlack.put(new Position(3, 8), new ChessPiece(ChessColor.BLACK, Piece.BISHOP));
        tempBlack.put(new Position(4, 8), new ChessPiece(ChessColor.BLACK, Piece.QUEEN));
        tempBlack.put(new Position(5, 8), new ChessPiece(ChessColor.BLACK, Piece.KING));
        tempBlack.put(new Position(6, 8), new ChessPiece(ChessColor.BLACK, Piece.BISHOP));
        tempBlack.put(new Position(7, 8), new ChessPiece(ChessColor.BLACK, Piece.KNIGHT));
        tempBlack.put(new Position(8, 8), new ChessPiece(ChessColor.BLACK, Piece.ROOK));

        tempWhite.put(new Position(1, 1), new ChessPiece(ChessColor.WHITE, Piece.ROOK));
        tempWhite.put(new Position(2, 1), new ChessPiece(ChessColor.WHITE, Piece.KNIGHT));
        tempWhite.put(new Position(3, 1), new ChessPiece(ChessColor.WHITE, Piece.BISHOP));
        tempWhite.put(new Position(4, 1), new ChessPiece(ChessColor.WHITE, Piece.QUEEN));
        tempWhite.put(new Position(5, 1), new ChessPiece(ChessColor.WHITE, Piece.KING));
        tempWhite.put(new Position(6, 1), new ChessPiece(ChessColor.WHITE, Piece.BISHOP));
        tempWhite.put(new Position(7, 1), new ChessPiece(ChessColor.WHITE, Piece.KNIGHT));
        tempWhite.put(new Position(8, 1), new ChessPiece(ChessColor.WHITE, Piece.ROOK));
		
		initialWhite = (HashMap<Position, ChessPiece>) Collections.unmodifiableMap(tempWhite);
		initialBlack = (HashMap<Position, ChessPiece>) Collections.unmodifiableMap(tempBlack);
	}

	private final HashMap<Position, ChessPiece>			white, black;
	private final HashSet<Pair<Position, ChessPiece>>	hasMoved;
	private final ArrayList<ChessPiece>					taken;

	@SuppressWarnings("unchecked")
	public ChessBoard() {
		this.white = (HashMap<Position, ChessPiece>) initialWhite.clone();
		this.black = (HashMap<Position, ChessPiece>) initialBlack.clone();
		this.hasMoved = new HashSet<Pair<Position, ChessPiece>>();

		this.taken = new ArrayList<ChessPiece>();
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

	public boolean hasPieceMoved(Position pos, ChessPiece piece) {
		return hasMoved.contains(new Pair<Position, ChessPiece>(pos, piece));
	}

	// TODO Implement a way to include choices for the promotion
	public void updateWith(Move move) {
		HashMap<Position, ChessPiece> pieces = getPiecesByColor(move.getPiece().getColor());

		hasMoved.add(new Pair<Position, ChessPiece>(move.getStart(), move.getPiece()));
		pieces.remove(move.getStart());
		ChessPiece previousOccupant = null;
		if (move.getFlags().contains(MoveFlags.PROMOTION)) {
			previousOccupant = pieces.put(move.getEnd(),
					new ChessPiece(move.getPiece().getColor(), Piece.QUEEN));
		} else {
			previousOccupant = pieces.put(move.getEnd(), move.getPiece());
		}

		if (previousOccupant != null) {
			taken.add(previousOccupant);
		}

		if (move.getFlags().contains(MoveFlags.EN_PASSANT)) {
			Position enPassantPawn = new Position(move.getEnd().getFile(),
					move.getEnd().getRank() - move.getPiece().getColor().getForwardDirection());
			taken.add(pieces.remove(enPassantPawn));
		}

		if (move.getFlags().contains(MoveFlags.CASTLE)) {
			int sign = Integer.signum(move.getEnd().getFile() - move.getStart().getFile());
			int rookFile = (sign > 0) ? 8 : 1;
			Position oldCastlePosition = new Position(rookFile, move.getEnd().getRank());
			Position newCastlePosition = new Position(move.getEnd().getFile() - sign,
					move.getEnd().getRank());

			pieces.put(newCastlePosition, pieces.remove(oldCastlePosition));
		}
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
	
	public HashMap<Position, ChessPiece> returnInitialWhitePositions() {
		return initialWhite;
	}
	
	public HashMap<Position, ChessPiece> returnInitialBlackPositions() {
		return initialBlack;
	}

	public final class Pair<A, B> {
		private final A	first;
		private final B	second;

		public Pair(A first, B second) {
			this.first = first;
			this.second = second;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((first == null) ? 0 : first.hashCode());
			result = prime * result + ((second == null) ? 0 : second.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pair<?, ?> other = (Pair<?, ?>) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (first == null) {
				if (other.first != null)
					return false;
			} else if (!first.equals(other.first))
				return false;
			if (second == null) {
				if (other.second != null)
					return false;
			} else if (!second.equals(other.second))
				return false;
			return true;
		}

		private ChessBoard getOuterType() {
			return ChessBoard.this;
		}

		public A first() {
			return first;
		}

		public B second() {
			return second;
		}
	}
}
