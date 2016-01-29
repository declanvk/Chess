package game.pieces;

import static util.ChessNotationConversion.convertPointToAlgebraic;

import java.awt.Point;
import java.util.ArrayList;

import game.ChessBoard;
import game.ChessColor;
import game.ChessMove;

public class Queen extends ChessPiece {
	
	public static final int VALUE = 8;

	public Queen(Point pos, ChessColor color) {
		super(pos, color, Queen.VALUE);
	}

	@Override
	public String toString() {
		return this.getColor() + " queen at " + convertPointToAlgebraic(this.getPosition());
	}

	@Override
	public ArrayList<ChessMove> generateMoves(ChessBoard board) {
		// TODO Implement
		return null;
	}

}
