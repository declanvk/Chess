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
public class ArtificialPlayer extends Player {

	private final Search search;

	/**
	 * Constructs a new ArtificalPlayer with the given name and color
	 * 
	 * @param name
	 *            the name of the ArtificialPlayer
	 * @param color
	 *            the color of the ArtificialPlayer
	 */
	public ArtificialPlayer(String name, int color, ChessBoard board, long time) {
		super(name, color, board);
		this.search = new Search(time);
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
				submitMove(search.execute(input));
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
