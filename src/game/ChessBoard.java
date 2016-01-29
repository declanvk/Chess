package game;

import java.awt.Point;

public class ChessBoard {
	
	public static final int BOARD_HEIGHT = 8;
	public static final int BOARD_WIDTH = 8;
	
	public static boolean isPositionValid(Point pos) {
		return pos.x > 0 && pos.x <= BOARD_WIDTH && pos.y > 0 && pos.y <= ChessBoard.BOARD_HEIGHT;
	}
}
