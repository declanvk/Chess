package gui;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.event.MouseInputAdapter;

import game.ChessColor;
import game.Move;

public class MinimalPlayer extends Player<MouseInputAdapter> {

	public MinimalPlayer(String name, ChessColor color) {
		super(name, color, new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
			}
		});
	}

	@Override
	public void updateWith(Move m) {
		
	}

	@Override
	public SwingWorker<Move, Integer> getMove(JPanel parent) {
		return null;
	}

}
