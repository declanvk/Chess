package gui;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import game.ChessColor;
import game.Move;

public abstract class Player<I> {
	
	private final String name;
	private final ChessColor color;
	private final I inputMethod;
	
	public Player(String name, ChessColor color, I input) {
		this.name = name;
		this.color = color;
		this.inputMethod = input;
	}
	
	//Update a player's internal representation of the board.
	//A player doesn't necessarily need to maintain a representation
	public abstract void updateWith(Move m);
	
	//The idea behind this is that calling this will
	//return the Player's next choice of Move. However
	//This call will block if called by GUI rendering thread
	//Possible solution is to call this method in a new Thread
	//And give it the resources to update the main board representation
	public abstract SwingWorker<Move, Integer> getMove(JPanel board);
	
	public String getName() {
		return name;
	}

	public ChessColor getColor() {
		return color;
	}
	
	public I getInputMethod() {
		return inputMethod;
	}
}
