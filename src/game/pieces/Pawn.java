package game.pieces;

import static util.ChessNotationConversion.convertPointToAlgebraic;

import java.awt.Point;
import java.util.ArrayList;

import game.ChessBoard;
import game.ChessColor;
import game.ChessMove;

public class Pawn extends ChessPiece {
	
	public static final int VALUE = 1;
	
	public Pawn(Point pos, ChessColor color) {
		super(pos, color, Pawn.VALUE);
	}

	@Override
	public String toString() {
		return this.getColor() + " pawn at " + convertPointToAlgebraic(this.getPosition());
	}

	@Override
	public ArrayList<ChessMove> generateMoves(ChessBoard board) {
		// TODO Implement
		return null;
	}
}
