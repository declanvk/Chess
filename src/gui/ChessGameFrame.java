package gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ChessGameFrame extends JFrame implements Runnable {
	
	private JTabbedPane gameTabs = new JTabbedPane(JTabbedPane.BOTTOM);

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
		
		gameTabs.setBackground(Color.WHITE);
		gameTabs.setFocusable(false);
		add(gameTabs);

        this.setBackground(Color.BLACK);
		
		Player white = new MinimalPlayer("White player", game.ChessColor.WHITE);
		Player black = new MinimalPlayer("Black Player", game.ChessColor.BLACK);
		
		ChessBoard testing = new ChessBoard("Testing1", white, black);
		gameTabs.add(testing.getName(), testing);
		
		ChessBoard testing2 = new ChessBoard("Testing2", white, black);
		gameTabs.add(testing2.getName(), testing2);
		
		ChessBoard testing3 = new ChessBoard("Testing3", white, black);
		gameTabs.add(testing3.getName(), testing3);
		
		ChessBoard testing4 = new ChessBoard("Testing4", white, black);
		gameTabs.add(testing4.getName(), testing4);
	}

}
