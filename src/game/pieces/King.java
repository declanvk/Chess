package game.pieces;

import static util.ChessNotationConversion.convertPointToAlgebraic;

import java.awt.Point;
import java.util.ArrayList;

import game.ChessBoard;
import game.ChessColor;
import game.ChessMove;

public class King extends ChessPiece {
	
	public static final int VALUE = Integer.MAX_VALUE;

	public King(Point pos, ChessColor color) {
		super(pos, color, King.VALUE);
	}

	@Override
	public String toString() {
		return this.getColor() + " king at " + convertPointToAlgebraic(this.getPosition());
	}

	@Override
	public ArrayList<ChessMove> generateMoves(ChessBoard board) {
		// TODO Implement
		return null;
	}

}
