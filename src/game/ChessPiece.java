package game;

import util.ChessNotation;

public class ChessPiece {
	
	private Position position;
	private final Color color;
	private final PieceType type;
	
	public ChessPiece(Position pos, Color color, PieceType type) {
		this.position = pos;
		this.color = color;
		this.type = type;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public void setPosition(Position pos) {
		this.position = pos;
	}
	
	public int getValue() {
		return type.getValue();
	}
	
	@Override
	public String toString() {
		return color + " " + type + " at " + ChessNotation.convertPointToAlgebraic(position);
	}
}
