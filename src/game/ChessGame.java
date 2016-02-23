package game;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;

import game.Move.MoveFlags;
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
		ArrayList<Move> results = null;
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
		return results;
	}

	public ArrayDeque<Move> getHistory() {
		return history;
	}

	@Override
	public String toString() {
		return "ChessGame [name=" + name + "]";
	}

	public static class MoveGeneration {

		// TODO Needs to implement all the special moves
		// Castling, the complexity of pawns, en passant, all that other crap

		// TODO Implement something that converts arraylists of positions into
		// Arraylists of moves with appropriate flags.

		private static final int[][]	knightOffsets				= { { -1, +2 }, { -2, +1 },
				{ +1, +2 }, { +2, +1 }, { +2, -1 }, { +1, -2 }, { -1, -2 }, { -2, -1 } };

		private static final int[][]	kingOffsets					= { { -1, +1 }, { 0, +1 },
				{ +1, +1 }, { +1, 0 }, { +1, -1 }, { 0, -1 }, { -1, -1 }, { -1, 0 } };

		private static final Position	initialWhiteKingPosition	= new Position(5, 1);
		private static final Position	initialBlackKingPosition	= new Position(5, 8);

		public static ArrayList<Position> knightPositions(Position pos) {
			return positionsByOffsets(pos, knightOffsets);
		}

		private static ArrayList<Position> basicKingPositions(Position pos) {
			return positionsByOffsets(pos, kingOffsets);
		}

		private static void kingCastling(ArrayList<Position> results, Position pos, ChessColor color, ChessPiece[][] board) {
			if (color == ChessColor.WHITE) {
				
			}
		}

		public static ArrayList<Position> kingPositions(Position pos, ChessColor color, ChessPiece[][] board) {
			ArrayList<Position> results = basicKingPositions(pos);
			kingCastling(results, pos, color, board);
			return results;
		}

		private static ArrayList<Position> positionsByOffsets(Position pos, int[][] offsets) {
			ArrayList<Position> results = new ArrayList<Position>();

			for (int i = 0; i < offsets.length; i++) {
				checkAndAdd(pos.getFile() + offsets[i][0], pos.getRank() + offsets[i][1], results);
			}

			return results;
		}

		public static ArrayList<Position> rookPositions(Position pos) {
			ArrayList<Position> results = new ArrayList<Position>();

			for (int file = 1; file <= 8; file++) {
				if (file != pos.getFile()) {
					checkAndAdd(file, pos.getRank(), results);
				}
			}

			for (int rank = 1; rank <= 8; rank++) {
				if (rank != pos.getRank()) {
					checkAndAdd(pos.getFile(), rank, results);
				}
			}

			return results;
		}

		public static ArrayList<Position> bishopPositions(Position pos) {
			ArrayList<Position> results = new ArrayList<Position>();

			for (int i = 1, lim = 8 - Math.min(pos.getFile(), pos.getRank()); i < lim; i++) {
				checkAndAdd(pos.getFile() + i, pos.getRank() + i, results);
				checkAndAdd(pos.getFile() + i, pos.getRank() - i, results);
				checkAndAdd(pos.getFile() - i, pos.getRank() + i, results);
				checkAndAdd(pos.getFile() - i, pos.getRank() - i, results);
			}

			return results;
		}

		public static ArrayList<Position> queenPositions(Position pos) {
			ArrayList<Position> results = rookPositions(pos);
			results.addAll(bishopPositions(pos));
			return results;
		}

		public static ArrayList<Position> pawnPositions(Position pos, ChessColor color, ChessPiece[][] board, ArrayDeque<Move> history) {
			ArrayList<Position> results = new ArrayList<Position>();

			if (color == ChessColor.WHITE) {
				checkAndAdd(pos.getFile(), pos.getRank() + 1, results);
				if (pos.getRank() == 2) {
					checkAndAdd(pos.getFile(), pos.getRank() + 2, results);
				}

				if (isEnemy(pos.getFile() + 1, pos.getRank() + 1, ChessColor.WHITE, board)) {
					results.add(new Position(pos.getFile() + 1, pos.getRank() + 1));
				}

				if (isEnemy(pos.getFile() - 1, pos.getRank() + 1, ChessColor.WHITE, board)) {
					results.add(new Position(pos.getFile() - 1, pos.getRank() + 1));
				}

				if (pos.getRank() == 5 && history.peekLast() != null
						&& history.peekLast().getFlag() == MoveFlags.DOUBLE_PAWN_PUSH) {

					Move possibleEnPassant = history.peekLast();
					if (possibleEnPassant.getEnd().getFile() == pos.getFile() - 1) {
						results.add(new Position(pos.getRank() + 1, pos.getFile() - 1));
					} else if (possibleEnPassant.getEnd().getFile() == pos.getFile() + 1) {
						results.add(new Position(pos.getRank() + 1, pos.getFile() + 1));
					}
				}
			} else {
				checkAndAdd(pos.getFile(), pos.getRank() - 1, results);
				if (pos.getRank() == 7) {
					checkAndAdd(pos.getFile(), pos.getRank() - 2, results);
				}

				if (isEnemy(pos.getFile() + 1, pos.getRank() - 1, ChessColor.BLACK, board)) {
					results.add(new Position(pos.getFile() + 1, pos.getRank() + 1));
				}

				if (isEnemy(pos.getFile() - 1, pos.getRank() - 1, ChessColor.BLACK, board)) {
					results.add(new Position(pos.getFile() - 1, pos.getRank() + 1));
				}

				if (pos.getRank() == 4 && history.peekLast() != null
						&& history.peekLast().getFlag() == MoveFlags.DOUBLE_PAWN_PUSH) {

					Move possibleEnPassant = history.peekLast();
					if (possibleEnPassant.getEnd().getFile() == pos.getFile() - 1) {
						results.add(new Position(pos.getRank() - 1, pos.getFile() - 1));
					} else if (possibleEnPassant.getEnd().getFile() == pos.getFile() + 1) {
						results.add(new Position(pos.getRank() - 1, pos.getFile() + 1));
					}
				}
			}

			return results;
		}

		private static void checkAndAdd(int file, int rank, ArrayList<Position> results) {
			if (Position.isValidPosition(file, rank)) {
				results.add(new Position(file, rank));
			}
		}

		private static boolean isEnemy(int file, int rank, ChessColor color, ChessPiece[][] board) {
			return Position.isValidPosition(file, rank) && board[file][rank] != null
					&& board[file][rank].getColor() != color;
		}

	}

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
