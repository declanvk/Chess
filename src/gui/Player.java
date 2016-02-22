package gui;

import javax.swing.JComponent;

import game.ChessColor;
import game.Move;

public abstract class Player {
	
	private final String name;
	private final ChessColor color;

	public Player(String name, ChessColor color) {
		this.name = name;
		this.color = color;
	}
	
	public abstract void updateWith(Move m);
	public abstract Move getMove(JComponent parent);

	public String getName() {
		return name;
	}

	public ChessColor getColor() {
		return color;
	}
}
