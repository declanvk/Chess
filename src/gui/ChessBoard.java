package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JPanel;

import game.ChessGame;

@SuppressWarnings("serial")
public class ChessBoard extends JPanel {
	
	private final ChessGame game;
	
	private static final Color backgroundColor = Color.WHITE;
	
	
	private static final int cellSize = 55;
	private static final int borderWidth = 6;
	private static final int dx = 20, dy = 40;
	private static final int boxLength = 9;
	private Rectangle[][] boxes = new Rectangle[boxLength][boxLength];
	private boolean engaged = false;
	private Point selection = new Point(0, 0);
	
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		if (game == null)
			return;
	}
	
	@Override
	public Dimension getPreferredSize() {
		int l = (int) ((2 * (borderWidth - 1)) + (boxLength * (cellSize + 4)) + (2 * (boxLength - 1)) + (2 * borderWidth));
		return new Dimension(l, l);
	}

	@Override
	public void repaint() {
		if (game != null)
			game.refresh();
		super.repaint();
	}

	@Override
	public String toString() {
		return game.getName() + " Board";
	}
}
