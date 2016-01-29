package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import gui.ChessSerializable;

public class Board implements ChessSerializable {
	
	public static final int BOARD_HEIGHT = 8;
	public static final int BOARD_WIDTH = 8;
	
	public static boolean isPositionValid(Point pos) {
		return pos.x > 0 && pos.x <= BOARD_WIDTH && pos.y > 0 && pos.y <= Board.BOARD_HEIGHT;
	}
	
	private static void putStartingPositions(HashMap<Point, ChessPiece> pieces, Color color) {
		for(int i = 1; i<= 8; i++) {
			
		}
	}
	
	private final String name;
	private final HashMap<Point, ChessPiece> whitePieces;
	private final HashMap<Point, ChessPiece> blackPieces;
	private final ArrayList<Move> history;
	
	public Board(String name) {
		this.name = name;
		this.whitePieces = new HashMap<>();
		this.blackPieces = new HashMap<>();
	}
	
	public void registerTurn(Move cm) {
		
	}
}
