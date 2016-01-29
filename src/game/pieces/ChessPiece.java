package game.pieces;

import java.awt.Point;
import java.util.ArrayList;

import game.ChessBoard;
import game.ChessColor;
import game.ChessMove;

public abstract class ChessPiece {
	
	private final Point position;
	private final ChessColor color;
	private final int value;
	
	public ChessPiece(Point pos, ChessColor color, int val) {
		this.position = pos;
		this.color = color;
		this.value = val;
	}
	
	public ChessColor getColor() {
		return this.color;
	}
	
	public Point getPosition() {
		return this.position;
	}
	
	public void translatePosition(Point delta) {
		this.position.translate(delta.x, delta.y);
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public abstract String toString();
	public abstract ArrayList<ChessMove> generateMoves(ChessBoard board);
}
