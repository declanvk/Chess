package game.pieces;

import static util.ChessNotationConversion.convertPointToAlgebraic;

import java.awt.Point;
import java.util.ArrayList;

import game.ChessBoard;
import game.ChessColor;
import game.ChessMove;

public class Rook extends ChessPiece {
	
	public static final int VALUE = 5;
	
	public Rook(Point pos, ChessColor color) {
		super(pos, color, Rook.VALUE);
	}

	@Override
	public String toString() {
		return this.getColor() + " rook at " + convertPointToAlgebraic(this.getPosition());
	}

	@Override
	public ArrayList<ChessMove> generateMoves(ChessBoard board) {
		// TODO Implement
		return null;
	}
}
