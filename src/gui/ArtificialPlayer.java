package gui;

import game.ChessColor;

public abstract class ArtificialPlayer extends Player<Void> {

	public ArtificialPlayer(String name, ChessColor color, Void input) {
		super(name, color, input);
	}

}
