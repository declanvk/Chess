package gui;

import javax.swing.SwingUtilities;

import core.ChessBoard;
import core.Move;
import engine.Search;

/**
 * Represents an AI player within the Player framework.
 * 
 * @author declan
 *
 */
public class ArtificialPlayer extends Player<ChessBoard> {

	/**
	 * Constructs a new ArtificalPlayer with the given name and color
	 * 
	 * @param name
	 *            the name of the ArtificialPlayer
	 * @param color
	 *            the color of the ArtificialPlayer
	 */
	public ArtificialPlayer(String name, int color) {
		super(name, color);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.Player#updateWith(core.Move)
	 */
	@Override
	public void updateWith(Move m) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.Player#startTurnProtected()
	 */
	@Override
	protected void startTurnProtected() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				submitMove(Search.search(input));
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.Player#endTurnProtected()
	 */
	@Override
	protected void endTurnProtected() {
	}

}
