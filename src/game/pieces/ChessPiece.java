package game.pieces;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import game.ChessColor;
import game.ChessMove;

public abstract class ChessPiece {
	
	private final Point position;
	protected final ChessColor color;
	
	public ChessPiece(Point pos, ChessColor color) {
		this.position = pos;
		this.color = color;
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
	
	@Override
	public abstract String toString();
	
	public abstract ArrayList<ChessMove> generateMoves();
	
	public abstract void draw(Graphics2D g);
}
