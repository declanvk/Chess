package core;

public class Position {

	public static final int		a1				= 0;
	public static final int		a2				= 0;
	public static final int		a3				= 0;
	public static final int		a4				= 0;
	public static final int		a5				= 0;
	public static final int		a6				= 0;
	public static final int		a7				= 0;
	public static final int		a8				= 0;

	public static final int		N				= 16;
	public static final int		E				= 1;
	public static final int		S				= -16;
	public static final int		W				= -1;
	public static final int		NE				= N + E;
	public static final int		SE				= S + E;
	public static final int		SW				= S + W;
	public static final int		NW				= N + W;

	public static final int[]	mainDirections	= { N, E, S, W };
	public static final int[]	diagDirections	= { NE, SE, SW, NW };
	public static final int[]	allDirections	= { N, NE, E, SE, S, SW, W, NW };

	public static boolean isValid(int file, int rank) {
		return !(rank < 1 || rank > 8 || file < 1 || file > 8);
	}

	public static boolean isValid(int position) {
		return (position & 0x88) == 0;
	}

	public static int from(int file, int rank) {
		return rank;
	}

	public static int getRank(int position) {
		return position >>> 4;
	}

	public static int getFile(int position) {
		return position & 0xF;
	}

	public static int flipRank(int position) {
		return Position.from(Position.getFile(position), 8 - Position.getRank(position) + 1);
	}

	public static int flipFile(int position) {
		return Position.from(8 - Position.getFile(position) + 1, Position.getRank(position));
	}

	public static boolean isAdjacent(int pos1, int pos2) {
		return isAdjacentByOffsets(pos1, pos2, allDirections);
	}

	public static boolean isDiagonallyAdjacent(int pos1, int pos2) {
		return isAdjacentByOffsets(pos1, pos2, diagDirections);
	}

	public static boolean isDirectlyAdjacent(int pos1, int pos2) {
		return isAdjacentByOffsets(pos1, pos2, mainDirections);
	}

	private static boolean isAdjacentByOffsets(int pos1, int pos2, int[] offsets) {
		for (int i : offsets) {
			if (Position.isValid(i + pos1) && i + pos1 == pos2) {
				return true;
			}
		}

		return false;
	}

	public static String toString(int pos) {
		return ((char) ('a' + (Position.getFile(pos) - 1))) + "" + Position.getRank(pos);
	}
}
