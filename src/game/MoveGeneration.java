package game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import game.Move.MoveFlags;

public class MoveGeneration {

	private static final int[][]	knightOffsets	= { { -1, +2 }, { -2, +1 }, { +1, +2 },
			{ +2, +1 }, { +2, -1 }, { +1, -2 }, { -1, -2 }, { -2, -1 } };

	private static final int[][]	bishopRay		= { { +1, +1 }, { +1, -1 }, { -1, +1 },
			{ -1, -1 } };
	private static final int[][]	rookRay			= { { +1, 0 }, { -1, 0 }, { 0, +1 },
			{ 0, -1 } };
	private static final int[][]	queenRay		= { { +1, 0 }, { -1, 0 }, { 0, +1 }, { 0, -1 },
			{ +1, +1 }, { +1, -1 }, { -1, +1 }, { -1, -1 } };

	private static ArrayList<Move> generateSlidingPieceMoves(int[][] ray, ChessPiece piece, Position pos, ChessBoard board) {
		ArrayList<Move> results = new ArrayList<Move>();
		for (int i = 0; i < ray.length; i++) {
			int j = 1;
			while (checkAndAdd(piece, pos, pos.getFile() + (j * ray[i][0]),
					pos.getRank() + (j * ray[i][1]), results, board, false)) {
				i++;
			}
		}
		return results;
	}

	private static ArrayList<Move> generateOffsetPieceMoves(int[][] offset, ChessPiece piece, Position pos, ChessBoard board) {
		ArrayList<Move> results = new ArrayList<Move>();
		for (int i = 0; i < offset.length; i++) {
			checkAndAdd(piece, pos, pos.getFile() + offset[i][0], pos.getRank() + offset[i][1],
					results, board, false);
		}
		return results;
	}

	private static boolean checkAndAdd(ChessPiece piece, Position oldPos, int file, int rank, ArrayList<Move> results, ChessBoard board, boolean pawnFlag) {
		if (!Position.isValidPosition(file, rank)) {
			return false;
		} else {
			Position newPos = new Position(rank, file);
			EnumSet<MoveFlags> flags = EnumSet.noneOf(MoveFlags.class);
			if (piece.getType().equals(Piece.PAWN)
					&& piece.getColor().opposite().getHomeRank() == rank) {
				flags.add(MoveFlags.PROMOTION);
			}

			if (board.isEmpty(newPos)) {
				flags.add(MoveFlags.QUIET);
				return results.add(new Move(piece, oldPos, newPos, flags));
			} else if (board.isColor(piece.getColor(), newPos)) {
				return false;
			} else {
				if (!pawnFlag) {
					flags.add(MoveFlags.CAPTURE);
					results.add(new Move(piece, oldPos, newPos, flags));
				}
				return false;
			}
		}
	}

	private static ArrayList<Move> knightMoves(ChessPiece piece, Position pos, ChessBoard board) {
		return generateOffsetPieceMoves(knightOffsets, piece, pos, board);
	}

	private static ArrayList<Move> bishopMoves(ChessPiece piece, Position pos, ChessBoard board) {
		return generateSlidingPieceMoves(bishopRay, piece, pos, board);
	}

	private static ArrayList<Move> rookMoves(ChessPiece piece, Position pos, ChessBoard board) {
		return generateSlidingPieceMoves(rookRay, piece, pos, board);
	}

	private static ArrayList<Move> queenMoves(ChessPiece piece, Position pos, ChessBoard board) {
		return generateSlidingPieceMoves(queenRay, piece, pos, board);
	}

	private static final int[][] kingOffsets = { { -1, -1 }, { 0, -1 }, { +1, -1 }, { +1, 0 },
			{ -1, 0 }, { -1, +1 }, { 0, +1 }, { +1, +1 } };

	private static ArrayList<Move> kingMoves(ChessPiece piece, Position pos, ChessBoard board, boolean[][] taboo) {
		ArrayList<Move> results = generateOffsetPieceMoves(kingOffsets, piece, pos, board);

		Iterator<Move> iter = results.iterator();
		Move m = null;
		while (iter.hasNext()) {
			m = iter.next();
			if (!taboo[m.getEnd().getFile()][m.getEnd().getRank()]) {
				iter.remove();
			}
		}

		if (!board.hasPieceMoved(new Position(5, piece.getColor().getHomeRank()), piece)) {
			if (!board.hasPieceMoved(new Position(8, piece.getColor().getHomeRank()),
					new ChessPiece(piece.getColor(), Piece.ROOK))
					&& board.isFileRangeEmpty(pos.getFile() - 1, pos.getFile() - 3,
							piece.getColor().getHomeRank())
					&& ChessBoard.checkTabooFileRange(taboo, pos.getFile() - 1, pos.getFile() - 3,
							piece.getColor().getHomeRank())) {
				results.add(new Move(piece, pos,
						new Position(pos.getFile() - 2, piece.getColor().getHomeRank()),
						EnumSet.of(MoveFlags.CASTLE)));
			}

			if (!board.hasPieceMoved(new Position(1, piece.getColor().getHomeRank()),
					new ChessPiece(piece.getColor(), Piece.ROOK))
					&& board.isFileRangeEmpty(pos.getFile() + 1, pos.getFile() + 2,
							piece.getColor().getHomeRank())
					&& ChessBoard.checkTabooFileRange(taboo, pos.getFile() + 1, pos.getFile() + 2,
							piece.getColor().getHomeRank())) {
				results.add(new Move(piece, pos,
						new Position(pos.getFile() + 2, piece.getColor().getHomeRank()),
						EnumSet.of(MoveFlags.CASTLE)));
			}
		}

		return results;
	}

	private static ArrayList<Move> pawnMoves(ChessPiece piece, Position pos, ChessBoard board, ArrayDeque<Move> history) {
		ArrayList<Move> results = new ArrayList<Move>();

		boolean normalMoveResult = checkAndAdd(piece, pos, pos.getFile(),
				pos.getRank() + piece.getColor().getForwardDirection(), results, board, true);

		if (normalMoveResult && pos.getRank() == piece.getColor().getHomeRank()
				+ piece.getColor().getForwardDirection()) {
			checkAndAdd(piece, pos, pos.getFile(),
					pos.getRank() + 2 * piece.getColor().getForwardDirection(), results, board,
					true);
		}

		checkAndAdd(piece, pos, pos.getFile() - 1,
				pos.getRank() + piece.getColor().getForwardDirection(), results, board, false);
		checkAndAdd(piece, pos, pos.getFile() + 1,
				pos.getRank() + piece.getColor().getForwardDirection(), results, board, false);

		if (history.size() > 0 && history.getLast().getFlags().contains(MoveFlags.DOUBLE_PAWN_PUSH)
				&& history.getLast().getPiece().getColor() != piece.getColor()) {
			Move possibleEnPassant = history.getLast();
			if (pos.isFileAdjacent(possibleEnPassant.getEnd())) {
				Position passantPosition = new Position(possibleEnPassant.getEnd().getFile(),
						pos.getRank() + piece.getColor().getForwardDirection());
				results.add(
						new Move(piece, pos, passantPosition, EnumSet.of(MoveFlags.EN_PASSANT)));
			}
		}

		return results;
	}

	// Seems pretty inefficient.
	public static boolean[][] generateTabooBoard(ChessColor c, ChessBoard board, ArrayDeque<Move> history, boolean[][] oppositeTaboo) {
		HashMap<Position, ChessPiece> pieces = board.getPiecesByColor(c.opposite());
		ArrayList<Move> moves = new ArrayList<Move>();
		boolean[][] taboo = new boolean[8][8]; // [file][rank]
		for (int i = 0; i < taboo.length; i++) {
			Arrays.fill(taboo[i], true);
		}

		for (Entry<Position, ChessPiece> entry : pieces.entrySet()) {
			moves.addAll(getMoves(entry.getValue(), entry.getKey(), board, history, oppositeTaboo));
		}

		for (Move move : moves) {
			taboo[move.getEnd().getFile()][move.getEnd().getRank()] = false;
		}

		return taboo;
	}

	public static ArrayList<Move> getMoves(ChessPiece piece, Position position, ChessBoard board, ArrayDeque<Move> history, boolean[][] taboo) {
		Piece type = piece.getType();
		if (type == Piece.PAWN) {
			return pawnMoves(piece, position, board, history);
		} else if (type == Piece.ROOK) {
			return rookMoves(piece, position, board);
		} else if (type == Piece.KNIGHT) {
			return knightMoves(piece, position, board);
		} else if (type == Piece.BISHOP) {
			return bishopMoves(piece, position, board);
		} else if (type == Piece.QUEEN) {
			return queenMoves(piece, position, board);
		} else {
			return kingMoves(piece, position, board, taboo);
		}
	}

}