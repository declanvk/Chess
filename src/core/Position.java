package core;

public class Position {

	public static enum File {
		F_A(0x101010101010101L), F_B(0x202020202020202L), F_C(0x404040404040404L),
		F_D(0x808080808080808L), F_E(0x1010101010101010L), F_F(0x2020202020202020L),
		F_G(0x4040404040404040L), F_H(0x8080808080808080L);

		private final Bitboard board;

		private File(long value) {
			this.board = Bitboard.from(value);
		}

		public int value() {
			return this.ordinal();
		}

		public Bitboard board() {
			return board;
		}

		public static boolean isValid(int file) {
			return F_A.value() <= file && file <= F_H.value();
		}

		public static File from(int file) {
			assert isValid(file);

			return File.values()[file];
		}
	}

	public static enum Rank {
		R_1(0xffL), R_2(0xff00L), R_3(0xff0000L), R_4(0xff000000L), R_5(0xff00000000L),
		R_6(0xff0000000000L), R_7(0xff000000000000L), R_8(0xff00000000000000L);

		private final Bitboard board;

		private Rank(long value) {
			this.board = Bitboard.from(value);
		}

		public int value() {
			return this.ordinal();
		}

		public Bitboard board() {
			return board;
		}

		public static boolean isValid(int rank) {
			return R_1.value() <= rank && rank <= R_8.value();
		}

		public static Rank from(int rank) {
			assert isValid(rank);

			return Rank.values()[rank];
		}
	}

	public static final int N = 16;
	public static final int E = 1;
	public static final int S = -16;
	public static final int W = -1;
	public static final int NE = N + E;
	public static final int SE = S + E;
	public static final int SW = S + W;
	public static final int NW = N + W;
	public static final int NNE = N + N + E;
	public static final int NEE = N + E + E;
	public static final int SEE = S + E + E;
	public static final int SSE = S + S + E;
	public static final int SSW = S + S + W;
	public static final int SWW = S + W + W;
	public static final int NWW = N + W + W;
	public static final int NNW = N + N + W;

	public static final int[] mainDirections = { N, E, S, W };
	public static final int[] diagDirections = { NE, SE, SW, NW };
	public static final int[] allDirections = { N, NE, E, SE, S, SW, W, NW };

	public static final int BIT_WIDTH = 8;
	public static final int NULL_POSITION = -1;
	public static final int NUM_TOTAL_VALUES = 1 << (BIT_WIDTH - 1);

	private static final int[] positions = new int[64];

	static {
		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				positions[(rank * 8) + file] = (16 * rank) + file;
			}
		}
	}

	public static int getPosition(int file, int rank) {
		assert File.isValid(file);
		assert Rank.isValid(rank);

		return positions[(rank * 8) + file];
	}

	public static int getPosition(int index) {
		assert index > -1 && index < positions.length;

		return positions[index];
	}

	public static int getBitIndex(int position) {
		return (getRank(position) * 8) + getFile(position);
	}

	private Position() {
	}

	public static boolean isValid(int file, int rank) {
		return !(rank < 1 || rank > 8 || file < 1 || file > 8);
	}

	public static boolean isValid(int position) {
		return (position & 0x88) == 0;
	}

	public static int from(int file, int rank) {
		assert 0 <= file && file <= 7;
		assert 0 <= rank && rank <= 7;

		return 16 * rank + file;
	}

	public static int from(File file, Rank rank) {
		return Position.from(file.value(), rank.value());
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

	private static boolean isAdjacentByOffsets(int centre, int possiblyAdjacent, int[] offsets) {
		for (int offset : offsets) {
			if (Position.isValid(offset + centre) && offset + centre == possiblyAdjacent) {
				return true;
			}
		}

		return false;
	}

	public static String toString(int pos) {
		return ((char) ('a' + (Position.getFile(pos) + 1) - 1)) + "" + (Position.getRank(pos) + 1);
	}
}
