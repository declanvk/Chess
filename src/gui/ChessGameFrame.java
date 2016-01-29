package gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ChessGameFrame extends JFrame implements Runnable {

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
	}

}
