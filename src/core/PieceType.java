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
	
	private static final int[] scores = {100, 325, 325, 500, 975, Integer.MAX_VALUE};
	
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
	
	public static int getScore(int type) {
		assert isValid(type);
		
		return scores[type];
	}
	
}
