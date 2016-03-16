package gui;

import javax.swing.SwingUtilities;

import core.ChessBoard;
import core.Move;
import engine.Search;

public class ArtificialPlayer extends Player<ChessBoard> {

	public ArtificialPlayer(String name, int color) {
		super(name, color);
	}

	@Override
	public void updateWith(Move m) {
	}

	@Override
	protected void startTurnProtected() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				submitMove(Search.search(input));
			}

		});
	}

	@Override
	protected void endTurnProtected() {
	}

}
