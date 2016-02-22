package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.HashMap;

import javax.swing.JPanel;

import game.ChessGame;
import game.ChessPiece;
import game.Position;

@SuppressWarnings("serial")
public class ChessBoard extends JPanel {

	private final String						name;

	private final ChessGame						game;
	private final Player						whitePlayer, blackPlayer;
	private Player								currentPlayer;

	// Input map Rectangle to Position
	private final HashMap<Rectangle, Position>	rectangleToPosition;

	// Dimensions for drawing
	private static final int					cellSize		= 60;
	private static final int					dx				= 10;
	private static final int					dy				= 40;
	private static final int					rank_tx			= 10;
	private static final int					rank_ty			= cellSize / 2 - 10;
	private static final int					file_tx			= cellSize / 2 - 5;
	private static final int					file_ty			= 20;
	private static final int					marginSize		= 30;
	private static final int					borderSize		= 2;

	// Objects to draw. Will be assigned location, dynamically assigned color
	private static final int					boxLength		= 8;
	private final Rectangle[][]					boxes;
	private final Font							contentsFont	= new Font("Serif", Font.PLAIN, 40);
	private final Font							annotationsFont	= new Font("Serif", Font.PLAIN, 22);
	private final Color							dark			= Color.DARK_GRAY;
	private final Color							light			= Color.LIGHT_GRAY;

	public ChessBoard(String name, Player white, Player black) {
		this.name = name;
		this.game = new ChessGame(name);
		this.whitePlayer = white;
		this.blackPlayer = black;
		this.currentPlayer = this.whitePlayer;
		this.boxes = new Rectangle[boxLength][boxLength];
		this.rectangleToPosition = new HashMap<>();

		initializeBoxes(this.boxes, this.rectangleToPosition);
	}

	private void initializeBoxes(Rectangle[][] boxes, HashMap<Rectangle, Position> map) {
		int x = marginSize;
		int y = marginSize;
		Dimension boxSize = new Dimension(cellSize, cellSize);
		for (int file = 0; file < boxes.length; file++) {
			for (int rank = 0; rank < boxes[file].length; rank++) {
				boxes[file][rank] = new Rectangle(new Point(x, y), boxSize);
				map.put(boxes[file][rank], new Position(file + 1, rank + 1));
				x += cellSize + (rank != boxes[file].length - 1 ? borderSize : 0);
			}
			y += cellSize + (file != boxes.length - 1 ? borderSize : 0);
			x = marginSize;
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

		g2.setBackground(Color.WHITE);
		g2.setFont(contentsFont);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

		drawBoxes(g2);

		g2.setColor(Color.BLACK);
		g2.setFont(annotationsFont);
		drawAnnotations(g2);

		g2.setFont(contentsFont);
		g2.setColor(Color.WHITE);
		drawContents(g2);
	}

	private void drawBoxes(Graphics2D g2) {
		for (int file = 0; file < boxes.length; file++) {
			for (int rank = 0; rank < boxes[file].length; rank++) {
				if ((rank + (1 + file) % 2) % 2 == 0) {
					g2.setColor(dark);
				} else {
					g2.setColor(light);
				}

				g2.fill(boxes[file][rank]);
			}
		}
	}

	private void drawAnnotations(Graphics2D g2) {
		Dimension dim = getPreferredSize();
		int file_x = marginSize;
		int rank_y = (int) (dim.height - marginSize);
		for (int i = 1; i <= 8; i++) {
			g2.drawString(((char) ('a' + (i - 1))) + "", file_x + file_tx,
					(dim.height - marginSize) + file_ty);
			g2.drawString(((char) ('a' + (i - 1))) + "", file_x + file_tx, file_ty);

			g2.drawString(i + "", rank_tx, rank_y - rank_ty);
			g2.drawString(i + "", rank_tx + (dim.width - marginSize), rank_y - rank_ty);

			file_x += cellSize + borderSize;
			rank_y -= cellSize + borderSize;
		}
	}

	private void drawContents(Graphics2D g2) {
		int x = 0, y = 0;
		String content = null;
		ChessPiece piece = null;
		for (int file = 0; file < boxes.length; file++) {
			for (int rank = 0; rank < boxes[file].length; rank++) {
				x = boxes[file][rank].x + dx;
				y = boxes[file][rank].y + dy;
				piece = game.getPiece(new Position(file + 1, rank + 1).flipFile());
				if (piece != null) {
					content = piece.getUnicode();
					g2.setColor(piece.getColor().getDrawColor());
					g2.drawString(content, x, y);
				}
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		int l = (int) ((boxLength * cellSize) + (2 * marginSize) + ((boxLength - 1) * borderSize));
		return new Dimension(l, l);
	}

	@Override
	public String toString() {
		return game.getName() + " Board";
	}
}
