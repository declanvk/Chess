package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import core.ChessBoard;
import core.ChessPiece;
import core.Position;

@SuppressWarnings("serial")
public class ChessBoardPanel extends JPanel {

	private final String name;

	private final ChessBoard board;

	private final HashMap<Integer, Color> positionsToColor;

	// Rendering values
	private static final int cellSize = 60;
	private static final int dx = 10;
	private static final int dy = 40;
	private static final int rank_tx = 10;
	private static final int rank_ty = cellSize / 2 - 10;
	private static final int file_tx = cellSize / 2 - 5;
	private static final int file_ty = 20;
	private static final int marginSize = 30;
	private static final int borderSize = 2;

	// Objects to draw. Will be assigned location, dynamically assigned color
	private static final int boxLength = 8;
	private final Rectangle[][] boxes;
	private final Font contentsFont = new Font("Serif", Font.PLAIN, 40);
	private final Font annotationsFont = new Font("Serif", Font.PLAIN, 22);
	private final Color dark = Color.DARK_GRAY;
	private final Color light = Color.LIGHT_GRAY;

	public ChessBoardPanel(String name, ChessBoard board) {
		this.name = name;
		this.board = board;
		this.boxes = new Rectangle[boxLength][boxLength];
		this.positionsToColor = new HashMap<>();

		initializeBoxes(this.boxes);
	}

	private void initializeBoxes(Rectangle[][] boxes) {
		Dimension canvasSize = this.getPreferredSize();
		Dimension boxSize = new Dimension(cellSize, cellSize);
		int x = marginSize;
		int y = canvasSize.height - (marginSize + (int) boxSize.getHeight());

		for (int rank = 0; rank < boxes.length; rank++) {
			for (int file = 0; file < boxes[rank].length; file++) {
				boxes[file][rank] = new Rectangle(new Point(x, y), boxSize);
				x += cellSize + (file != boxes[rank].length - 1 ? borderSize : 0);
			}
			y -= cellSize + (rank != boxes.length - 1 ? borderSize : 0);
			x = marginSize;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		if (board == null) return;

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
		Color boxColor = light;
		int boxPosition = Position.NULL_POSITION;
		for (int file = 0; file < boxes.length; file++) {
			for (int rank = 0; rank < boxes[file].length; rank++) {
				boxPosition = Position.from(file, rank);
				boxColor = (rank + (file + 1) % 2) % 2 == 0 ? light : dark;

				if (positionsToColor.containsKey(boxPosition)) {
					g2.setColor(blendColor(boxColor, positionsToColor.get(boxPosition)));
				} else {
					g2.setColor(boxColor);
				}

				g2.fill(boxes[file][rank]);
			}
		}
	}

	private void drawContents(Graphics2D g2) {
		int x = 0, y = 0;
		String content = null;
		ChessPiece piece = null;
		int boxPosition = Position.NULL_POSITION;
		for (int file = 0; file < boxes.length; file++) {
			for (int rank = 0; rank < boxes[file].length; rank++) {
				boxPosition = Position.from(file, rank);

				x = boxes[file][rank].x + dx;
				y = boxes[file][rank].y + dy;
				piece = board.getObject(boxPosition);
				if (piece != null) {
					content = piece.getUnicode();
					g2.setColor(piece.getColor().getDrawColor());
					g2.drawString(content, x, y);
				}
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

	private Color blendColor(Color ca, Color cb) {
		double totalAlpha = ca.getAlpha() + cb.getAlpha();
		double aWeight = ca.getAlpha() / totalAlpha;
		double bWeight = cb.getAlpha() / totalAlpha;

		double r = (aWeight * ca.getRed()) + (bWeight * cb.getRed());
		double g = (aWeight * ca.getGreen()) + (bWeight * cb.getGreen());
		double b = (aWeight * ca.getBlue()) + (bWeight * cb.getBlue());
		;
		double a = Math.max(ca.getAlpha(), cb.getAlpha());

		return new Color((int) r, (int) g, (int) b, (int) a);
	}

	public int getPositionContaining(Point p) {
		int x = p.x - marginSize, y = p.y - marginSize;
		int l = cellSize * 8 + borderSize * 7;
		if (x > 0 || x < l || y > 0 || y < l) {
			return Position.from((x / (cellSize + borderSize)),
					((l - y) / (cellSize + borderSize)));
		} else {
			return Position.NULL_POSITION;
		}
	}

	void addColorPositions(ArrayList<Integer> positions, Color color) {
		for (Integer p : positions) {
			positionsToColor.put(p, color);
		}
	}

	void clearColorPositions() {
		positionsToColor.clear();
	}

	@Override
	public Dimension getPreferredSize() {
		int l = (int) ((boxLength * cellSize) + (2 * marginSize) + ((boxLength - 1) * borderSize));
		return new Dimension(l, l);
	}

	public String getName() {
		return name;
	}
}
