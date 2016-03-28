package gui;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.SwingUtilities;

import core.ChessBoard;
import core.Move;
import engine.MoveGeneration;

/**
 * The RandomPlayer makes moves chosen randomly from a list of all possible
 * moves in the position
 * 
 * @author declan
 *
 */
public class RandomPlayer extends Player {

	private final Random rGen;

	/**
	 * Construct a new RandomPlayer
	 * 
	 * @param name
	 *            the name of the RandomPlayer
	 * @param color
	 *            the color of the RandomPlayer
	 */
	public RandomPlayer(String name, int color, ChessBoard board) {
		super(name, color, board);
		rGen = new Random();
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
				ArrayList<Integer> moves = MoveGeneration.getMoves(input, false);
				submitMove(Move.from(moves.get(rGen.nextInt(moves.size()))));
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
