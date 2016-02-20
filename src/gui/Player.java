package gui;

import javax.swing.JComponent;

import game.Color;
import game.Move;

public abstract class Player {
	
	private final String name;
	private final Color color;

	public Player(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	public abstract void updateWith(Move m);
	public abstract Move getMove(JComponent parent);

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}
}
