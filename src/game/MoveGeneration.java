package game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

import game.Move.MoveFlags;

public class MoveGeneration {

	// TODO Needs to implement all the special moves
	// Castling, the complexity of pawns, en passant, all that other crap

	// TODO Implement something that converts arraylists of positions into
	// Arraylists of moves with appropriate flags.

	private static final int[][] knightOffsets = { { -1, +2 }, { -2, +1 }, { +1, +2 },
			{ +2, +1 }, { +2, -1 }, { +1, -2 }, { -1, -2 }, { -2, -1 } };
	
	private static final int[][] bishopRay = { {+1, +1}, {+1, -1}, {-1, +1}, {-1, -1} };
	private static final int[][] rookRay = { {+1, 0}, {-1, 0}, {0, +1}, {0, -1} };
	private static final int[][] queenRay = { {+1, 0}, {-1, 0}, {0, +1}, {0, -1}, {+1, +1}, {+1, -1}, {-1, +1}, {-1, -1} };
	
	private static ArrayList<Move> generateSlidingPieceMoves(int[][] ray, ChessPiece piece, Position pos, ChessBoard board) {
		ArrayList<Move> results = new ArrayList<Move>();
		for(int i = 0; i < ray.length; i++) {
			int j = 1;
			while (checkAndAdd(piece, pos, pos.getFile() + (j * ray[i][0]), pos.getRank() + (j * ray[i][1]), results, board)) {
				i++;
			}
		}
		return results;
	}
	
	private static ArrayList<Move> generateOffsetPieceMoves(int[][] offset, ChessPiece piece, Position pos, ChessBoard board) {
		ArrayList<Move> results = new ArrayList<Move>();
		for(int i = 0; i < offset.length; i++) {
			checkAndAdd(piece, pos, pos.getFile() + offset[i][0], pos.getRank() + offset[i][1], results, board);
		}
		return results;
	}

	private static boolean checkAndAdd(ChessPiece piece, Position oldPos, int file, int rank, ArrayList<Move> results, ChessBoard board) {
		if (!Position.isValidPosition(file, rank)) {
			return false;
		} else {
			Position newPos = new Position(rank, file);

			if (board.isEmpty(newPos)) {
				return results.add(new Move(piece, oldPos, newPos, MoveFlags.QUIET));
			} else if (board.isColor(piece.getColor(), newPos)) {
				return false;
			} else {
				results.add(new Move(piece, oldPos, newPos, MoveFlags.CAPTURE));
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
	
	private static ArrayList<Move> kingMoves(ChessPiece piece, Position Pos, ChessBoard board) {
		ArrayList<Move> results = new ArrayList<Move>();
		boolean[][] tabooBoard = generateKingTabooBoard(piece.getColor(), )
		
		
		
		return results;
	}
	
	private static boolean[][] generateKingTabooBoard(ChessColor color, ChessBoard board) {
		boolean[][] taboo = new boolean[8][8];
		for(int i = 0; i < taboo.length; i++) {
			Arrays.fill(taboo[i], 0, taboo[i].length, true);
		}
		
		ArrayList<Move> generatedMoves = new ArrayList<Move>();
		for(ChessPiece piece: relevantPieces) {
			generatedMoves.add(getMoves(piece,))
		}
		
		for(Move m: generatedMoves) {
			if(m.getPiece().getColor() != color) {
				taboo[m.getEnd().getFile()][m.getEnd().getRank()] = false;
			}
		}
		
		return taboo;
	}
	
	public ArrayList<Move> getMoves(ChessPiece piece, Position position, ChessBoard board, ArrayDeque<Move> history) {
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