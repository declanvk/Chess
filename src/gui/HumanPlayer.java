package gui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import core.ChessColor;
import core.ChessGame;
import core.ChessPiece;
import core.Move;
import core.Position;

public class HumanPlayer extends Player<ChessBoardPanel> {

	private final PlayerAdapter adapter;
	private final ChessGame game;

	public HumanPlayer(String name, ChessColor color, ChessGame game) {
		super(name, color);
		
		this.game = game;
		this.adapter = new PlayerAdapter(color);
	}

	// Do nothing. No need to maintain an internal representation.
	@Override
	public void updateWith(Move m) {
	}

	@Override
	protected void startTurnProtected() {
		input.addMouseListener(adapter);
	}

	@Override
	protected void endTurnProtected() {
		input.removeMouseListener(adapter);
	}

	private class PlayerAdapter extends MouseAdapter {

		private final Color				possibleMovesColor	= new Color(255, 25, 25, 128);

		private final ChessColor		color;

		private HashMap<Position, Move>	positionsToMoves;

		// States:
		// false - has not registed a click at all
		// true - has registed the first click
		private boolean						state;

		public PlayerAdapter(ChessColor c) {
			this.color = c;
			this.state = false;
			this.positionsToMoves = new HashMap<Position, Move>();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			Position pos = input.getPositionContaining(e.getPoint());
			if (!state && pos != null && game.getPiece(pos).getColor() == color) {
				ChessPiece piece = game.getPiece(pos);
				ArrayList<Move> moves = game.getMovesFor(piece, pos);

				for (Move m : moves) {
					positionsToMoves.put(m.getEnd(), m);
				}
				input.addColorPositions(new ArrayList<Position>(positionsToMoves.keySet()),
						possibleMovesColor);
				input.repaint();

				state = true;
			} else if (state) {
				input.clearColorPositions();
				input.repaint();
				
				if (pos != null && positionsToMoves.containsKey(pos)) {
					submitMove(positionsToMoves.get(pos));
				} else {
					state = false;
				}

				positionsToMoves.clear();
			}
		}
	}

}
