package game;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import engine.BitBoard;
import gui.ChessSerializable;

@SuppressWarnings("serial")
public class Board implements ChessSerializable {
	
	public static final int BOARD_HEIGHT = 8;
	public static final int BOARD_WIDTH = 8;
	
	public static boolean isPositionValid(Point pos) {
		return pos.x > 0 && pos.x <= BOARD_WIDTH && pos.y > 0 && pos.y <= Board.BOARD_HEIGHT;
	}
	
	private final String name;
	private final HashMap<Point, ChessPiece> whitePieces;
	private final HashMap<Point, ChessPiece> blackPieces;
	private final ArrayList<Move> history;
	
	public Board(String name, BitBoard board) {
		this.name = name;
		this.whitePieces = new HashMap<Point, ChessPiece>();
		this.blackPieces = new HashMap<Point, ChessPiece>();
		this.history = new ArrayList<Move>();
	}

	@Override
	public boolean isSaved() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public File getSave() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSave(File f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSuffix() {
		// TODO Auto-generated method stub
		return null;
	}
}
