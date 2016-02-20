package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import game.ChessGame;
import game.Position;

@SuppressWarnings("serial")
public class ChessBoard extends JPanel {

	private final String name;

	private final ChessGame game;
	private final Player whitePlayer, blackPlayer;
	private Player currentPlayer;

	// Dimensions for drawing
	private static final int cellSize = 60;
	private static final int borderWidth = 20;
	private static final int dx = 20, dy = 40;

	// Objects to draw. Will be assigned location, dynamically assigned color
	private static final int boxLength = 8;
	private final Rectangle[][] boxes;
	private final Font contentsFont = new Font("Serif", Font.PLAIN, 40);

	public ChessBoard(String name, Player white, Player black) {
		this.name = name;
		this.game = new ChessGame(name);
		this.whitePlayer = white;
		this.blackPlayer = black;
		this.currentPlayer = this.whitePlayer;
		this.boxes = new Rectangle[boxLength][boxLength];

		initializeBoxes(this.boxes);
	}

	private void initializeBoxes(Rectangle[][] boxes) {
		int x = borderWidth;
		int y = borderWidth;
		Dimension boxSize = new Dimension(cellSize, cellSize);
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				boxes[i][j] = new Rectangle(new Point(x, y), boxSize);
				x += cellSize + 1;
			}
			y += cellSize + 1;
			x = borderWidth;
		}
	}

	public String getName() {
		return name;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public Player getWhitePlayer() {
		return whitePlayer;
	}

	public Player getBlackPlayer() {
		return blackPlayer;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		if (game == null)
			return;

		g2.setFont(contentsFont);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		drawBoxes(g2);
		drawContents(g2);
	}

	private void drawBoxes(Graphics2D g2) {
		for(int i = 0; i < boxes.length; i++) {
			for(int j = 0; j < boxes[i].length; j++) {
				if((j + i * boxLength) % 2 == (true ? 1 : 0)) {
					g2.setColor(Color.DARK_GRAY);
				} else {
					g2.setColor(Color.LIGHT_GRAY);
				}
				
				g2.draw(boxes[i][j]);
			}
		}
	}

	private void drawContents(Graphics2D g2) {
		int x = 0, y = 0;
		String content = null;
		for(int i = 0; i < boxes.length; i++) {
			for(int j = 0; j < boxes[i].length; j++) {
				x = boxes[i][j].x + dx;
				y = boxes[i][j].y + dy;
				content = game.getPiece(new Position(j + 1, i + 1)).getUnicode();
				g2.drawString(content, x, y);
			}	
		}
	}

	@Override
	public Dimension getPreferredSize() {
		int l = (int) ((2 * (borderWidth - 1)) + (boxLength * (cellSize + 4)) + (2 * (boxLength - 1))
				+ (2 * borderWidth));
		return new Dimension(l, l);
	}

	@Override
	public String toString() {
		return game.getName() + " Board";
	}
}
