package core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class ChessGame {

	// board[file][rank]
	private final ChessBoard		board		= new ChessBoard();
	private final ArrayDeque<Move>	history		= new ArrayDeque<Move>();

	private String					name;
	private ChessColor				turnToggle	= ChessColor.WHITE;
	private boolean[][][]			taboo		= new boolean[2][8][8];

	public ChessGame(String name) {
		this.name = name;
		
		//taboo[color][file - 1][rank - 1]
		for(int i = 0; i < taboo[0].length; i++) {
			Arrays.fill(taboo[0][i], true);
			Arrays.fill(taboo[1][i], true);
		}
		
		for(int i = 0; i < taboo[0].length; i++) {
			taboo[0][i][6] = false;
			taboo[0][i][5] = false;
			
			taboo[1][i][1] = false;
			taboo[1][i][2] = false;
			
			if(i > 0 && i < 7) {
				taboo[0][i][7] = false;
				taboo[1][i][0] = false;
			}
		}
	}

	public ArrayDeque<Move> getHistory() {
		return history;
	}

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

	public ArrayList<Move> getMovesFor(ChessPiece piece, Position position) {
		PieceType type = piece.getType();
		if (type == PieceType.PAWN) {
			return MoveGeneration.pawnMoves(piece, position, board, history);
		} else if (type == PieceType.ROOK) {
			return MoveGeneration.rookMoves(piece, position, board);
		} else if (type == PieceType.KNIGHT) {
			return MoveGeneration.knightMoves(piece, position, board);
		} else if (type == PieceType.BISHOP) {
			return MoveGeneration.bishopMoves(piece, position, board);
		} else if (type == PieceType.QUEEN) {
			return MoveGeneration.queenMoves(piece, position, board);
		} else {
			return MoveGeneration.kingMoves(piece, position, board, taboo[piece.getColor().getID()]);
		}
	}

	public void updateWith(Move move) {
		if (move.getPiece().getColor() != turnToggle) {
			throw new IllegalArgumentException(
					"The Move passed indicates that a Played moved out of turn. It is the "
							+ turnToggle + " player's turn.");
		}
		board.updateWith(move);
		history.add(move);
		taboo[turnToggle.getID()] = MoveGeneration.generateTabooBoard(turnToggle, board, history, taboo[turnToggle.opposite().getID()]);
		
		turnToggle = turnToggle.opposite();
	}
}