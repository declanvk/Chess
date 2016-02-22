package game;

import java.awt.Color;

public enum ChessColor {
	WHITE("white", Color.WHITE),
	BLACK("black", Color.BLACK);
	
	private final String name;
	private final Color color;
	
	ChessColor(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	public String toString() {
		return name;
	}
	
	public Color getDrawColor() {
		return color;
	}
	
	public int getID() {
		return this.ordinal();
	}
}
