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

/**
 * The top level frame for the Chess GUI
 * 
 * @author declan
 *
 */
@SuppressWarnings("serial")
public class ChessGameFrame extends JFrame implements Runnable {

	private ChessBoardPanel boardPanel;
	private Player white, black;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
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

	/**
	 * Construct a new ChessGameFrame and populate it with GUI components
	 */
	public ChessGameFrame() {
		super("Chess");
		this.setBackground(Color.BLACK);

		createComponents();
	}

	private void createComponents() {
		String name = "Chess";
		ChessBoard board = ChessBoard.ChessBoardFactory.startingBoard();
		this.white = new RandomPlayer("White player", ChessColor.WHITE.value());
		this.black = new ArtificialPlayer("Black player", ChessColor.BLACK.value());
		this.boardPanel = new ChessBoardPanel(name + " Board", board);

		add(this.boardPanel);
		GameThread<ChessBoard, ChessBoard> gameThread = new GameThread<ChessBoard, ChessBoard>(
				this.white, this.black, board, board, board, this);
		SwingUtilities.invokeLater(gameThread);
	}

	/**
	 * The game thread will manage the state of the game, updating the players
	 * with each new move, maintaining the overall state of the board, and
	 * checking for the end of the game
	 * 
	 * @author declan
	 *
	 * @param <W>
	 *            the input that the White player will take
	 * @param <B>
	 *            the input that the Black player will take
	 */
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

		/**
		 * Construct what new GameThread
		 * 
		 * @param w
		 *            white player
		 * @param b
		 *            black player
		 * @param whiteIn
		 *            white input
		 * @param blackIn
		 *            black input
		 * @param g
		 *            ChessBoard position
		 * @param frame
		 *            top level GUI frame
		 */
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

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
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
