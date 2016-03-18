package gui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import core.ChessBoard;
import core.ChessColor;
import core.ChessPiece;
import core.Move;
import core.PieceType;
import core.Position;
import engine.MoveGeneration;

/**
 * Represents a HumanPlayer in the Player framework
 * 
 * @author declan
 *
 */
public class HumanPlayer extends Player<ChessBoardPanel> {

	private final PlayerAdapter adapter;
	private final ChessBoard game;
	private HashMap<Integer, ArrayList<Move>> positionMoveMap;

	/**
	 * Constructs a new HumanPlayer with the given name, color, and position
	 * 
	 * @param name
	 *            the name of the HumanPlayer
	 * @param color
	 *            the color of the HumanPlayer
	 * @param game
	 *            the internal board representation for the HumanPlayer
	 */
	public HumanPlayer(String name, int color, ChessBoard game) {
		super(name, color);

		this.game = game;
		this.adapter = new PlayerAdapter(ChessColor.from(color));
		this.positionMoveMap = MoveGeneration.getSortedMoves(game);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.Player#updateWith(core.Move)
	 */
	@Override
	public void updateWith(Move m) {
		positionMoveMap = MoveGeneration.getSortedMoves(game);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.Player#startTurnProtected()
	 */
	@Override
	protected void startTurnProtected() {
		input.addMouseListener(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.Player#endTurnProtected()
	 */
	@Override
	protected void endTurnProtected() {
		input.removeMouseListener(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * A listener to capture events on the ChessBoardPanel
	 * 
	 * @author declan
	 *
	 */
	private class PlayerAdapter extends MouseAdapter {

		private final Color possibleMovesColor = new Color(255, 25, 25, 128);

		private final ChessColor color;

		private HashMap<Integer, Move> positionsToMoves;

		// States:
		// false - has not registed a click at all
		// true - has registed the first click
		private boolean state;

		/**
		 * Constructs a new PlayerAdapter with the given color
		 * 
		 * @param c
		 *            the color of the PlayerAdapter
		 */
		public PlayerAdapter(ChessColor c) {
			this.color = c;
			this.state = false;
			this.positionsToMoves = new HashMap<Integer, Move>();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			int pos = input.getPositionContaining(e.getPoint());
			ChessPiece piece = game.getObject(pos);
			if (!state && pos != Position.NULL_POSITION && piece != null
					&& piece.getColor() == color) {
				ArrayList<Move> moves = positionMoveMap.get(pos);

				int moveCount = 0;
				if (moves != null) {
					moveCount = 0;
					for (Move m : moves) {
						positionsToMoves.put(m.getEndPosition(), m);
						moveCount++;
					}
				}

				input.addColorPositions(new ArrayList<Integer>(positionsToMoves.keySet()),
						possibleMovesColor);
				input.repaint();

				if (moveCount > 0) {
					state = true;
				}
			} else if (state) {
				input.clearColorPositions();
				input.repaint();

				if (pos != -1 && positionsToMoves.containsKey(pos)) {
					Move move = positionsToMoves.get(pos);
					if (move.getPromotionPieceType() != PieceType.NULL_PROMOTION) {
						PieceType type =
								(PieceType) JOptionPane
										.showInputDialog(null,
												"Choose a piece type to promote your pawn to.",
												"Promotion", JOptionPane.INFORMATION_MESSAGE, null,
												new PieceType[] { PieceType.KNIGHT,
														PieceType.BISHOP, PieceType.ROOK,
														PieceType.QUEEN },
												PieceType.QUEEN);
						move = new Move(move.getStartPiece(), move.getEndPiece(),
								move.getStartPosition(), move.getEndPosition(), move.getFlags(),
								type.value());
					}
					System.err.println(move);
					submitMove(move);
					state = false;
				} else {
					state = false;
				}

				positionsToMoves.clear();
			}
		}
	}

}
