package game;

import java.util.ArrayDeque;
import java.util.ArrayList;

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

			if (board.isEmpty(newPos)) {
				return results.add(new Move(piece, oldPos, newPos, MoveFlags.QUIET));
			} else if (board.isColor(piece.getColor(), newPos)) {
				return false;
			} else {
				if (!pawnFlag) {
					results.add(new Move(piece, oldPos, newPos, MoveFlags.CAPTURE));
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

	// TODO Implement
	private static ArrayList<Move> kingMoves(ChessPiece piece, Position pos, ChessBoard board) {
		ArrayList<Move> results = new ArrayList<Move>();

		return results;
	}

	// Avoiding unnecessary branching with preloaded information

	// Key:
	// pawnOffsets[a][b][c]
	// a: [0 - white, 1 - black]
	// b: [0 - regular pawn move, 1 - double pawn push, 3 and 4 capture on diag]
	// c: [0 - file, 1 - rank]
	// En passant is just a special case of the capture on diag
	private static int[][][]	pawnOffsets	= { { { 0, +1 }, { 0, +2 }, { -1, +1 }, { +1, +1 } },
			{ { 0, -1 }, { 0, -2 }, { +1, -1 }, { -1, -1 } } };

	// Key
	// pawnInfo[a][b][c]
	// a: [0 - white, 1 - black]
	// b: [0 - starting rank, 1 - en passant rank]
	private static int[][]		pawnInfo	= { { 2, 5 }, { 7, 4 } };

	private static ArrayList<Move> pawnMoves(ChessPiece piece, Position pos, ChessBoard board, ArrayDeque<Move> history) {
		ArrayList<Move> results = new ArrayList<Move>();

		boolean normalMoveResult = checkAndAdd(piece, pos,
				pos.getFile() + pawnOffsets[piece.getColor().getID()][0][0],
				pos.getRank() + pawnOffsets[piece.getColor().getID()][0][1], results, board, true);

		if (normalMoveResult && pos.getRank() == pawnInfo[piece.getColor().getID()][0]) {
			checkAndAdd(piece, pos, pos.getFile() + pawnOffsets[piece.getColor().getID()][1][0],
					pos.getRank() + pawnOffsets[piece.getColor().getID()][1][1], results, board,
					true);
		}

		for (int i = 3; i <= 4; i++) {
			checkAndAdd(piece, pos, pos.getFile() + pawnOffsets[piece.getColor().getID()][i][0],
					pos.getRank() + pawnOffsets[piece.getColor().getID()][i][1], results, board,
					false);
		}

		if (history.size() > 0 && history.getLast().getFlag() == MoveFlags.DOUBLE_PAWN_PUSH
				&& history.getLast().getPiece().getColor() != piece.getColor()) {
			Move possibleEnPassant = history.getLast();
			if (pos.isFileAdjacent(possibleEnPassant.getEnd())) {
				Position passantPosition = new Position(possibleEnPassant.getEnd().getFile(),
						pos.getRank() + pawnOffsets[piece.getColor().getID()][3][1]);
				results.add(new Move(piece, pos, passantPosition, MoveFlags.EN_PASSANT));
			}
		}

		return results;
	}

	// TODO Implement. Highly inefficient design.
	public static boolean[][] generateTabooBoard(ChessColor c, ChessBoard board) {
		return null;
	}

	public static ArrayList<Move> getMoves(ChessPiece piece, Position position, ChessBoard board, ArrayDeque<Move> history) {
		ArrayList<Move> results = null;
		switch (piece.getType()) {
			case PAWN:
				results = pawnMoves(piece, position, board, history);
				break;
			case ROOK:
				results = rookMoves(piece, position, board);
				break;
			case KNIGHT:
				results = knightMoves(piece, position, board);
				break;
			case BISHOP:
				results = bishopMoves(piece, position, board);
				break;
			case QUEEN:
				results = queenMoves(piece, position, board);
				break;
			case KING:
				results = kingMoves(piece, position, board);
				break;
		}
		return results;
	}

}