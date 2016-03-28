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
		this.boardPanel = new ChessBoardPanel(name + " Board", board);
		add(this.boardPanel);

		String[] options =
				new String[] { "PLAYER - AI", "PLAYER - PLAYER", "AI - RANDOM", "AI - AI" };
		int chosenIndex = JOptionPane.showOptionDialog(null, "Choose play configuration",
				"Choose Play Type", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, "PLAYER - AI");
		String chosen = options[chosenIndex != -1 ? chosenIndex : 0];

		if ("PLAYER - AI".equals(chosen)) {
			this.white = new HumanPlayer("White Human player", ChessColor.WHITE.value(), board,
					boardPanel);
			this.black = new ArtificialPlayer("Black AI player", ChessColor.BLACK.value(), board,
					30000L);
		} else if ("PLAYER - PLAYER".equals(chosen)) {
			this.white = new HumanPlayer("White Human player", ChessColor.WHITE.value(), board,
					boardPanel);
			this.black = new HumanPlayer("Black Human player", ChessColor.BLACK.value(), board,
					boardPanel);
		} else if ("AI - RANDOM".equals(chosen)) {
			this.white = new ArtificialPlayer("White AI player", ChessColor.WHITE.value(), board,
					30000L);
			this.black = new RandomPlayer("Black Random player", ChessColor.BLACK.value(), board);
		} else if ("AI - AI".equals(chosen)) {
			this.white = new ArtificialPlayer("White AI player", ChessColor.WHITE.value(), board,
					30000L);
			this.black =
					new ArtificialPlayer("Black AI player", ChessColor.BLACK.value(), board, 300L);
		} else {
			assert false;
		}

		GameThread gameThread = new GameThread(this.white, this.black, board, this);
		SwingUtilities.invokeLater(gameThread);
	}

	/**
	 * r The game thread will manage the state of the game, updating the players
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
	private final class GameThread implements Runnable {

		private final Player white;
		private final Player black;
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
		public GameThread(Player w, Player b, ChessBoard g, final ChessGameFrame frame) {
			this.white = w;
			this.black = b;
			this.board = g;
			this.panel = frame.boardPanel;
			this.frame = frame;

			this.playerToggle = false;
			this.changeListener = new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Move move = (Move) evt.getNewValue();
					System.err.println((playerToggle ? black : white) + ": " + move);
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			startCurrentPlayerTurn();
		}

		private void startCurrentPlayerTurn() {
			(playerToggle ? black : white).addMoveListener(changeListener);
			if (playerToggle) {
				black.startTurn();
			} else {
				white.startTurn();
			}
		}

	}
}
