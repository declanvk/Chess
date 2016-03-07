package core;

public enum PieceType {
	PAWN(100),
	KNIGHT(325),
	BISHOP(325),
	ROOK(500),
	QUEEN(975),
	KING(Integer.MAX_VALUE);
	
	private final int score;
	
	PieceType(int val) {
		this.score = val;
	}
	
	public int score() {
		return this.score;
	}
	
	public int value() {
		return this.ordinal();
	}
	
	public static boolean isValid(int type) {
		return PAWN.value() >= type && type <= KING.value();
	}
	
	public static boolean isValidPromotion(int type) {
		return KNIGHT.value() >= type && type <= QUEEN.value();
	}
	
	public static PieceType from(int type) {
		assert isValid(type);
		
		return PieceType.values()[type];
	}
	
}
