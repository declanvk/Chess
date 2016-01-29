package game;

public enum PieceType {
	KING(Integer.MAX_VALUE, "king"),
	QUEEN(8, "queen"),
	ROOK(5, "rook"),
	BISHOP(3, "bishop"),
	KNIGHT(3, "knight"),
	PAWN(1, "pawn");
	
	private final int value;
	private final String name;
	
	PieceType(int val, String name) {
		this.value = val;
		this.name = name;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public int getID() {
		return this.ordinal();
	}
	
	@Override
	public String toString() {
		return name;
	}
}
