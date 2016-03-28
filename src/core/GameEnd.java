package core;

public enum GameEnd {
	WIN_BY_CHECKMATE(20000), DRAW_BY_STALEMATE(0), DRAW_BY_50_MOVE(0), DRAW_BY_3_REPETITION(0),
	DRAW_BY_INSUFFICIENT_MATERIAL(0);

	private final int score;

	private GameEnd(int score) {
		this.score = score;
	}

	public int score() {
		return score;
	}
}
