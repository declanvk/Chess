package gui;

import javax.swing.JComponent;

import game.ChessColor;
import game.Move;

public class MinimalPlayer extends Player {

	public MinimalPlayer(String name, ChessColor color) {
		super(name, color);
	}

	@Override
	public void updateWith(Move m) {
		
	}

	@Override
	public Move getMove(JComponent parent) {
		return null;
	}

}
