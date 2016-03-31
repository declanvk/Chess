package core;

import java.awt.Color;

/**
 * ChessColor represents the different sides in chess.
 * 
 * @author declan
 *
 */
public enum ChessColor {

	/**
	 * The white side player
	 *
	 */
	WHITE(Color.WHITE) {
		@Override
		public ChessColor opposite() {
			return BLACK;
		}
	},

	/**
	 * The black side player
	 *
	 */
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

	/**
	 * Returns the java.awt.Color corresponding to this ChessColor
	 * 
	 * @return the java.awt.Color corresponding to this ChessColor
	 */
	public Color getDrawColor() {
		return drawColor;
	}

	/**
	 * Returns the opposite ChessColor
	 * 
	 * @return the opposite ChessColor
	 */
	public abstract ChessColor opposite();

	/**
	 * Returns the serialized value
	 * 
	 * @return the serialized value
	 */
	public int value() {
		return this.ordinal();
	}

	/**
	 * Returns the ChessColor corresponding to the input
	 * 
	 * @param color
	 *            the value to construct a ChessColor from
	 * @return the ChessColor corresponding to the input
	 */
	public static ChessColor from(int color) {
		if (!isValid(color)) {
			throw new IllegalArgumentException("Not a valid color value");
		}

		return ChessColor.values()[color];
	}

	/**
	 * Returns true if the input is valid as a serialized form of ChessColor
	 * 
	 * @param color
	 *            the value to check the validity of
	 * @return true if the input is valid as a serialized form of ChessColor
	 */
	public static boolean isValid(int color) {
		return color >= WHITE.value() && color <= BLACK.value();
	}

	/**
	 * If the input is valid, return the opposite serialized ChessColor
	 * 
	 * @param color
	 *            the color value to get the opposite of
	 * @return the opposite serialized ChessColor
	 */
	public static int opposite(int color) {
		if (!isValid(color)) {
			throw new IllegalArgumentException("Not a valid color value");
		}

		return color ^ 1;
	}
}