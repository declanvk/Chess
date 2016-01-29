package util;

import java.awt.Point;

public class ChessNotationConversion {
	
	public static String convertPointToAlgebraic(Point pos) {
		return pos.y + "" + ((char)('a' + (pos.x - 1)));
	}
	
}
