package gui;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import core.ChessGame;
import core.Move;

@SuppressWarnings("serial")
public class ChessGameFrame extends JFrame implements Runnable {

	private ChessBoardPanel	boardPanel;
	private Player			white, black;

	@Override
	public void run() {
		setResizable(false);
		setLocation(100, 100);
		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new ChessGameFrame());
	}

	public ChessGameFrame() {
		super("Chess");
		this.setBackground(Color.BLACK);
		
		String name = "Chess";
		ChessGame game = new ChessGame(name + " Game");
		this.white = new HumanPlayer("White player", core.ChessColor.WHITE, game);
		this.black = new HumanPlayer("Black Player", core.ChessColor.BLACK, game);
		this.boardPanel = new ChessBoardPanel(name + " Board", game);

		add(this.boardPanel);

		SwingUtilities.invokeLater(new GameThread<ChessBoardPanel, ChessBoardPanel>(this.white,
				this.black, this.boardPanel, this.boardPanel, game));
	}

	private final class GameThread<W, B> implements Runnable {

		private final Player<W>					white;
		private final Player<B>					black;
		private final W							whiteInput;
		private final B							blackInput;
		private final PropertyChangeListener	changeListener;

		private final ChessGame					game;

		private boolean							playerToggle;	// false -
																// white, true -
																// black

		public GameThread(Player<W> w, Player<B> b, W whiteIn, B blackIn, ChessGame g) {
			this.white = w;
			this.black = b;
			this.whiteInput = whiteIn;
			this.blackInput = blackIn;
			this.game = g;

			this.playerToggle = false;
			this.changeListener = new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					System.out.println("Registering move submission");
					game.updateWith((Move) evt.getNewValue());
					(playerToggle ? white : black).removeMoveListener(changeListener);
					playerToggle = !playerToggle;
					startCurrentPlayerTurn();
				}

			};
		}

		@Override
		public void run() {
			System.out.println("Starting current player's turn");
			startCurrentPlayerTurn();
		}

		private void startCurrentPlayerTurn() {
			(playerToggle ? black : white).addMoveListener(changeListener);
			if (playerToggle) {
				System.out.println("Starting black's turn");
				black.startTurn(blackInput);
			} else {
				System.out.println("Starting white's turn");
				white.startTurn(whiteInput);
			}
		}

	}
}
