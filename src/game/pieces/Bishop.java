package game.pieces;

import static util.ChessNotationConversion.convertPointToAlgebraic;

import java.awt.Point;
import java.util.ArrayList;

import game.ChessBoard;
import game.ChessColor;
import game.ChessMove;

public class Bishop extends ChessPiece {
	
	public static final int VALUE = 3;

	public Bishop(Point pos, ChessColor color) {
		super(pos, color, Bishop.VALUE);
	}

	@Override
	public String toString() {
		return this.getColor() + " bishop at " + convertPointToAlgebraic(this.getPosition());
	}

	@Override
	public ArrayList<ChessMove> generateMoves(ChessBoard board) {
		// TODO Implement
		return null;
	}

}
