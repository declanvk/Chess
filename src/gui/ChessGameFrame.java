package gui;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import core.ChessBoard;
import core.ChessColor;
import core.Move;
import engine.MoveGeneration;

@SuppressWarnings("serial")
public class ChessGameFrame extends JFrame implements Runnable {

	private ChessBoardPanel boardPanel;
	private Player white, black;

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

		createComponents();
	}

	private void createComponents() {
		String name = "Chess";
		ChessBoard board = ChessBoard.ChessBoardFactory.startingBoard();
		this.white = new HumanPlayer("White player", core.ChessColor.WHITE.value(), board);
		this.black = new HumanPlayer("Black Player", core.ChessColor.BLACK.value(), board);
		this.boardPanel = new ChessBoardPanel(name + " Board", board);

		add(this.boardPanel);
		GameThread<ChessBoardPanel, ChessBoardPanel> gameThread =
				new GameThread<ChessBoardPanel, ChessBoardPanel>(this.white, this.black,
						this.boardPanel, this.boardPanel, board, this);
		SwingUtilities.invokeLater(gameThread);
	}

	private final class GameThread<W, B> implements Runnable {

		private final Player<W> white;
		private final Player<B> black;
		private final W whiteInput;
		private final B blackInput;
		private final PropertyChangeListener changeListener;

		private final ChessBoard board;
		private final ChessBoardPanel panel;
		private final ChessGameFrame frame;

		private boolean playerToggle; // false -
										// white, true -
										// black

		public GameThread(Player<W> w, Player<B> b, W whiteIn, B blackIn, ChessBoard g,
				final ChessGameFrame frame) {
			this.white = w;
			this.black = b;
			this.whiteInput = whiteIn;
			this.blackInput = blackIn;
			this.board = g;
			this.panel = frame.boardPanel;
			this.frame = frame;

			this.playerToggle = false;
			this.changeListener = new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					System.err.println("ChessGameFrame: Registering move submission");

					Move move = (Move) evt.getNewValue();
					System.err.println("GameThread: " + move);
					board.move(move);
					panel.repaint();

					white.updateWith(move);
					black.updateWith(move);

					(playerToggle ? black : white).removeMoveListener(changeListener);

					playerToggle = !playerToggle;
					if (board.isCheck() && MoveGeneration.getMoves(board, false).size() == 0) {
						JOptionPane.showMessageDialog(null,
								"The " + ChessColor
										.from(ChessColor.opposite(board.getActiveColor()))
								+ " player has won!");
						return;
					}

					startCurrentPlayerTurn();
				}

			};
		}

		@Override
		public void run() {
			System.err.println("ChessGameFrame: Starting current player's turn");
			startCurrentPlayerTurn();
		}

		private void startCurrentPlayerTurn() {
			(playerToggle ? black : white).addMoveListener(changeListener);
			if (playerToggle) {
				System.err.println("ChessGameFrame: Starting black's turn");
				black.startTurn(blackInput);
			} else {
				System.err.println("ChessGameFrame: Starting white's turn");
				white.startTurn(whiteInput);
			}
		}

	}
}
