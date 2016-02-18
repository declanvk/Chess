package game;

public enum PieceType {
	KING(Integer.MAX_VALUE, "king", "\u2654", "\u265A"),
	QUEEN(8, "queen", "\u2655", "\u265B"),
	ROOK(5, "rook", "\u2656", "\u265C"),
	BISHOP(3, "bishop", "\u2657", "\u265D"),
	KNIGHT(3, "knight", "\u2658", "\u265E"),
	PAWN(1, "pawn", "\u2659", "\u265F");
	
	private final int value;
	private final String name;
	private final String whiteUnicode;
	private final String blackUnicode;
	
	PieceType(int val, String name, String white, String black) {
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
