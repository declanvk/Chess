package core;

/**
 * Position represents all the positions on the chess board, using of the 0x88
 * format. The 0x88 format stores file and rank information in one nibble each,
 * with invalid locations having the highest bit in the nibble set. Therefore,
 * to detect an invalid location the only thing one needs to do is mask by the
 * value 0x88 and check if the value is equal to zero or not. Another point of
 * convenience lies in the ability to get the positions in different directional
 * offsets. Calculating the position to the north and the south is as easy as
 * adding or subtracting 16.
 * 
 * @author declan
 *
 */
/**
 * @author declan
 *
 */
/**
 * @author declan
 *
 */
/**
 * @author declan
 *
 */
/**
 * @author declan
 *
 */
public class Position {

	/**
	 * File represents each file on the chess board.
	 * 
	 * @author declan
	 *
	 */
	public static enum File {
		/**
		 * File A
		 */
		F_A(0x101010101010101L),

		/**
		 * File B
		 */
		F_B(0x202020202020202L),

		/**
		 * File C
		 */
		F_C(0x404040404040404L),

		/**
		 * File D
		 */
		F_D(0x808080808080808L),

		/**
		 * File E
		 */
		F_E(0x1010101010101010L),

		/**
		 * File F
		 */
		F_F(0x2020202020202020L),

		/**
		 * File G
		 */
		F_G(0x4040404040404040L),

		/**
		 * File H
		 */
		F_H(0x8080808080808080L);

		private final Bitboard board;

		private File(long value) {
			this.board = Bitboard.from(value);
		}

		/**
		 * Returns the serialized value of the File
		 * 
		 * @return the serialized value of the File
		 */
		public int value() {
			return this.ordinal();
		}

		/**
		 * Returns a Bitboard with every position in the File set
		 * 
		 * @return a Bitboard with every position in the File set
		 */
		public Bitboard board() {
			return board;
		}

		/**
		 * Returns true if the given value is a valid serialized value for a
		 * File
		 * 
		 * @param file
		 * @return true if the given value is a valid serialized value for a
		 *         File
		 */
		public static boolean isValid(int file) {
			return F_A.value() <= file && file <= F_H.value();
		}

		/**
		 * Returns a File from the given serialized value
		 * 
		 * @param file
		 * @return a File from the given serialized value
		 */
		public static File from(int file) {
			assert isValid(file);

			return File.values()[file];
		}
	}

	/**
	 * Rank represents each rank on the chess board
	 * 
	 * @author declan
	 *
	 */
	public static enum Rank {
		/**
		 * Rank 1
		 */
		R_1(0xffL),

		/**
		 * Rank 2
		 */
		R_2(0xff00L),

		/**
		 * Rank 3
		 */
		R_3(0xff0000L),

		/**
		 * Rank 4
		 */
		R_4(0xff000000L),

		/**
		 * Rank 5
		 */
		R_5(0xff00000000L),

		/**
		 * Rank 6
		 */
		R_6(0xff0000000000L),

		/**
		 * Rank 7
		 */
		R_7(0xff000000000000L),

		/**
		 * Rank 8
		 */
		R_8(0xff00000000000000L);

		private final Bitboard board;

		private Rank(long value) {
			this.board = Bitboard.from(value);
		}

		/**
		 * Returns the serialized value of the Rank
		 * 
		 * @return the serialized value of the File
		 */
		public int value() {
			return this.ordinal();
		}

		/**
		 * Returns a Bitboard with every position in the Rank set
		 * 
		 * @return a Bitboard with every position in the Rank set
		 */
		public Bitboard board() {
			return board;
		}

		/**
		 * Returns true if the given value could represent a serialized Rank
		 * 
		 * @param rank
		 * @return true if the given value could represent a serialized Rank
		 */
		public static boolean isValid(int rank) {
			return R_1.value() <= rank && rank <= R_8.value();
		}

		/**
		 * Returns a Rank from the given serialized form
		 * 
		 * @param rank
		 * @return a Rank from the given serialized form
		 */
		public static Rank from(int rank) {
			assert isValid(rank);

			return Rank.values()[rank];
		}
	}

	/**
	 * North
	 */
	public static final int N = 16;

	/**
	 * East
	 */
	public static final int E = 1;

	/**
	 * South
	 */
	public static final int S = -16;

	/**
	 * West
	 */
	public static final int W = -1;

	/**
	 * North East
	 */
	public static final int NE = N + E;

	/**
	 * South East
	 */
	public static final int SE = S + E;

	/**
	 * South West
	 */
	public static final int SW = S + W;

	/**
	 * North West
	 */
	public static final int NW = N + W;

	/**
	 * North North East
	 */
	public static final int NNE = N + N + E;

	/**
	 * North East East
	 */
	public static final int NEE = N + E + E;

	/**
	 * South East East
	 */
	public static final int SEE = S + E + E;

	/**
	 * South South East
	 */
	public static final int SSE = S + S + E;

	/**
	 * South South West
	 */
	public static final int SSW = S + S + W;

	/**
	 * South West West
	 */
	public static final int SWW = S + W + W;

	/**
	 * North West West
	 */
	public static final int NWW = N + W + W;

	/**
	 * North North West
	 */
	public static final int NNW = N + N + W;

	/**
	 * The four cardinal directions
	 */
	public static final int[] mainDirections = { N, E, S, W };

	/**
	 * The four intercardinal directions
	 */
	public static final int[] diagDirections = { NE, SE, SW, NW };

	/**
	 * The combination of the cardinal and intercardinal directions
	 */
	public static final int[] allDirections = { N, NE, E, SE, S, SW, W, NW };

	/**
	 * The number of bits needed to fully represent a 0x88 position
	 */
	public static final int BIT_WIDTH = 8;

	/**
	 * Value to represent an invalid position
	 */
	public static final int NULL_POSITION = 0x7F;

	/**
	 * The total number of values that an 0x88 position could contain
	 */
	public static final int NUM_TOTAL_VALUES = 1 << (BIT_WIDTH - 1);

	private static final int[] positions = new int[64];

	static {
		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				positions[(rank * 8) + file] = (16 * rank) + file;
			}
		}
	}

	/**
	 * Returns the position value given the file and rank
	 * 
	 * @param file
	 * @param rank
	 * @return the position value given the file and rank
	 */
	public static int getPosition(int file, int rank) {
		assert File.isValid(file);
		assert Rank.isValid(rank);

		return positions[(rank * 8) + file];
	}

	/**
	 * Returns the position value given its position in a one dimensional array
	 * containing valid positions
	 * 
	 * @param index
	 * @return the position value given its position in a one dimensional array
	 *         containing valid positions
	 */
	public static int getPosition(int index) {
		assert index > -1 && index < positions.length;

		return positions[index];
	}

	/**
	 * Returns the index of the position in a Bitboard
	 * 
	 * @param position
	 * @return the index of the position in a Bitboard
	 */
	public static int getBitIndex(int position) {
		return (getRank(position) * 8) + getFile(position);
	}

	private Position() {
	}

	/**
	 * Returns true if the given file and rank are on the chess board
	 * 
	 * @param file
	 * @param rank
	 * @return true if the given file and rank are on the chess board
	 */
	public static boolean isValid(int file, int rank) {
		return !(rank < 1 || rank > 8 || file < 1 || file > 8);
	}

	/**
	 * Returns true if the given position is valid in the 0x88 format
	 * 
	 * @param position
	 * @return true if the given position is valid in the 0x88 format
	 */
	public static boolean isValid(int position) {
		return (position & 0x88) == 0;
	}

	/**
	 * Returns the 0x88 position value for the given file and rank
	 * 
	 * @param file
	 * @param rank
	 * @return the 0x88 position value for the given file and rank
	 */
	public static int from(int file, int rank) {
		assert 0 <= file && file <= 7;
		assert 0 <= rank && rank <= 7;

		return 16 * rank + file;
	}

	/**
	 * Returns the 0x88 position value for the given File and Rank
	 * 
	 * @param file
	 * @param rank
	 * @return the 0x88 position value for the given File and Rank
	 */
	public static int from(File file, Rank rank) {
		return Position.from(file.value(), rank.value());
	}

	/**
	 * Returns the rank from the given 0x88 position value
	 * 
	 * @param position
	 * @return the rank from the given 0x88 position value
	 */
	public static int getRank(int position) {
		return position >>> 4;
	}

	/**
	 * Returns the file from the given 0x88 position value
	 * 
	 * @param position
	 * @return the file from the given 0x88 position value
	 */
	public static int getFile(int position) {
		return position & 0xF;
	}

	/**
	 * Returns the 0x88 position value from the given position with the rank
	 * flipped
	 * 
	 * @param position
	 * @return the 0x88 position value from the given position with the rank
	 *         flipped
	 */
	public static int flipRank(int position) {
		return Position.from(Position.getFile(position), 8 - Position.getRank(position) + 1);
	}

	/**
	 * Returns the 0x88 position value from the given position with the file
	 * flipped
	 * 
	 * @param position
	 * @return the 0x88 position value from the given position with the file
	 *         flipped
	 */
	public static int flipFile(int position) {
		return Position.from(8 - Position.getFile(position) + 1, Position.getRank(position));
	}

	/**
	 * Returns true if the given positions are adjacent in all the cardinal and
	 * intercardinal directions
	 * 
	 * @param pos1
	 * @param pos2
	 * @return true if the given positions are adjacent in all the cardinal and
	 *         intercardinal directions
	 */
	public static boolean isAdjacent(int pos1, int pos2) {
		return isAdjacentByOffsets(pos1, pos2, allDirections);
	}

	/**
	 * Returns true if the given positions are adjacent in all the intercardinal
	 * directions
	 * 
	 * @param pos1
	 * @param pos2
	 * @return true if the given positions are adjacent in all the intercardinal
	 *         directions
	 */
	public static boolean isDiagonallyAdjacent(int pos1, int pos2) {
		return isAdjacentByOffsets(pos1, pos2, diagDirections);
	}

	/**
	 * Returns true if the given positions are adjacent in all the cardinal
	 * directions
	 * 
	 * @param pos1
	 * @param pos2
	 * @return true if the given positions are adjacent in all the cardinal
	 *         directions
	 */
	public static boolean isDirectlyAdjacent(int pos1, int pos2) {
		return isAdjacentByOffsets(pos1, pos2, mainDirections);
	}

	private static boolean isAdjacentByOffsets(int centre, int possiblyAdjacent, int[] offsets) {
		for (int offset : offsets) {
			if (Position.isValid(offset + centre) && offset + centre == possiblyAdjacent) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Convert the given position to a String in the algebraic position format
	 * 
	 * @param pos
	 * @return the given position to a String in the algebraic position format
	 */
	public static String toString(int pos) {
		return ((char) ('a' + (Position.getFile(pos) + 1) - 1)) + "" + (Position.getRank(pos) + 1);
	}
}
