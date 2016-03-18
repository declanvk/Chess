package gui;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.SwingUtilities;

import core.ChessBoard;
import core.Move;
import engine.MoveGeneration;

public class RandomPlayer extends Player<ChessBoard> {

	private final Random rGen;

	public RandomPlayer(String name, int color) {
		super(name, color);
		rGen = new Random();
	}

	@Override
	public void updateWith(Move m) {
	}

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

	@Override
	protected void endTurnProtected() {
	}

}
