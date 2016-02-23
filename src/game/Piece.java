package game;

public enum Piece {
	PAWN(1, "pawn", "\u2659", "\u265F"),
	KNIGHT(3, "knight", "\u2658", "\u265E"),
	BISHOP(3, "bishop", "\u2657", "\u265D"),
	ROOK(5, "rook", "\u2656", "\u265C"),
	QUEEN(8, "queen", "\u2655", "\u265B"),
	KING(Integer.MAX_VALUE, "king", "\u2654", "\u265A");
	
	private final int value;
	private final String name;
	private final String whiteUnicode;
	private final String blackUnicode;
	
	Piece(int val, String name, String white, String black) {
		this.value = val;
		this.name = name;
		this.whiteUnicode = white;
		this.blackUnicode = black;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public int getID() {
		return this.ordinal();
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the whiteUnicode
	 */
	public String getWhiteUnicode() {
		return whiteUnicode;
	}

	/**
	 * @return the blackUnicode
	 */
	public String getBlackUnicode() {
		return blackUnicode;
	}

	@Override
	public String toString() {
		return name;
	}
}