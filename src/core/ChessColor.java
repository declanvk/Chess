package core;

import java.awt.Color;

public enum ChessColor {
	WHITE(Color.WHITE) {
		@Override
		public ChessColor opposite() {
			return BLACK;
		}
	},
	BLACK(Color.BLACK) {
		@Override
		public ChessColor opposite() {
			return WHITE;
		}
	};

	private final Color drawColor;

	private ChessColor(Color color) {
		this.drawColor = color;
	}

	public Color getDrawColor() {
		return drawColor;
	}
	
	public abstract ChessColor opposite();

	public int value() {
		return this.ordinal();
	}

	public static ChessColor from(int color) {
		assert isValid(color);

		return ChessColor.values()[color];
	}

	public static boolean isValid(int color) {
		return color >= WHITE.value() && color <= BLACK.value();
	}
	
	public static int opposite(int color) {
		assert isValid(color);
		
		return ~color;
	}
}