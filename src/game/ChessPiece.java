package game;

import java.awt.Point;
import java.util.ArrayList;

public class ChessPiece {
	
	private final Point position;
	private final Color color;
	private final PieceType type;
	
	public ChessPiece(Point pos, Color color, PieceType type) {
		this.position = pos;
		this.color = color;
		this.type = type;
	}
	
	public Color getColor() {
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
	public abstract ArrayList<Move> generateMoves(Board board);
}
