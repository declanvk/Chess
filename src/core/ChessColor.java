package core;

import java.awt.Color;

public enum ChessColor {
	WHITE("white", Color.WHITE, 1, +1) {@Override public ChessColor opposite() {return BLACK;}},
	BLACK("black", Color.BLACK, 8, -1) {@Override public ChessColor opposite() {return WHITE;}};
	
	private final String name;
	private final Color color;
	private final int homeRank;
	private final int forwardDirection;
	
	ChessColor(String name, Color color, int rank, int direction) {
		this.name = name;
		this.color = color;
		this.homeRank = rank;
		this.forwardDirection = direction;
	}
	
	public String toString() {
		return name;
	}
	
	public Color getDrawColor() {
		return color;
	}
	
	public int getHomeRank() {
		return homeRank;
	}
	
	public int getForwardDirection() {
		return forwardDirection;
	}
	
	public int getID() {
		return this.ordinal();
	}
	
	public abstract ChessColor opposite();
}
