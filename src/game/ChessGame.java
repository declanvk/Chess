package game;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import gui.ChessSerializable;

@SuppressWarnings("serial")
public class ChessGame implements ChessSerializable {
	
	public static final int BOARD_HEIGHT = 8;
	public static final int BOARD_WIDTH = 8;
	
	public static boolean isPositionValid(Point pos) {
		return pos.x > 0 && pos.x <= BOARD_WIDTH && pos.y > 0 && pos.y <= ChessGame.BOARD_HEIGHT;
	}
	
	private String name;
	private File save;
	private final HashMap<Point, ChessPiece> whitePieces;
	private final HashMap<Point, ChessPiece> blackPieces;
	private final ArrayList<Move> history;
	
	public ChessGame(String name) {
		this.name = name;
		this.whitePieces = new HashMap<Point, ChessPiece>();
		this.blackPieces = new HashMap<Point, ChessPiece>();
		this.history = new ArrayList<Move>();
	}

	@Override
	public boolean isSaved() {
		return save != null;
	}

	@Override
	public File getSave() {
		return save;
	}

	@Override
	public void setSave(File f) {
		this.save = f;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String s) {
		this.name = s;
	}

	@Override
	public String getSuffix() {
		return "chess";
	}
}
