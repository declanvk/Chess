package engine;

public class BitOps {

	/*
		 * Code for these methods is from: https://chessprogramming.wikispaces.com
		 * 
		 * I did produce most of this, only formatted and changed it to work with
		 * Java.
		 */

	private static final byte[] popCountLookup = new byte[256];

	static {
		popCountLookup[0] = 0;
		for (int i = 1; i < popCountLookup.length; i++) {
			popCountLookup[i] = (byte) (popCountLookup[i / 2] + (i & 1));
		}
	}

	private static final long debruijn64Magic = 0x03f79d71b4cb0a89L;
	private static int[] bitScanLookup = { 0, 47, 1, 56, 48, 27, 2, 60, 57, 49, 41, 37, 28, 16, 3, 61, 54, 58, 35, 52,
			50, 42, 21, 44, 38, 32, 29, 23, 17, 11, 4, 62, 46, 55, 26, 59, 40, 36, 15, 53, 34, 51, 20, 43, 31, 22, 10,
			45, 25, 39, 14, 33, 19, 30, 9, 24, 13, 18, 8, 12, 7, 6, 5, 63 };

	public static long flipVert(long board) {
		long k1 = 0x00FF00FF00FF00FFL;
		long k2 = 0x0000FFFF0000FFFFL;
		board = ((board >> 8) & k1) | ((board & k1) << 8);
		board = ((board >> 16) & k2) | ((board & k2) << 16);
		board = (board >> 32) | (board << 32);
		return board;
	}

	public static long flipHoriz(long board) {
		long k2 = 0x3333333333333333L;
		long k1 = 0x5555555555555555L;
		long k4 = 0x0f0f0f0f0f0f0f0fL;
		board = ((board >> 1) & k1) + 2 * (board & k1);
		board = ((board >> 2) & k2) + 4 * (board & k2);
		board = ((board >> 4) & k4) + 16 * (board & k4);
		return board;
	}

	public static long flipDiagA1H8(long board) {
		long t;
		long k1 = 0x5500550055005500L;
		long k2 = 0x3333000033330000L;
		long k4 = 0x0f0f0f0f00000000L;
		t = k4 & (board ^ (board << 28));
		board ^= t ^ (t >> 28);
		t = k2 & (board ^ (board << 14));
		board ^= t ^ (t >> 14);
		t = k1 & (board ^ (board << 7));
		board ^= t ^ (t >> 7);
		return board;
	}

	public static long flipDiagA8H1(long x) {
		long t;
		long k1 = 0xaa00aa00aa00aa00L;
		long k2 = 0xcccc0000cccc0000L;
		long k4 = 0xf0f0f0f00f0f0f0fL;
		t = x ^ (x << 36);
		x ^= k4 & (t ^ (x >> 36));
		t = k2 & (x ^ (x << 18));
		x ^= t ^ (t >> 18);
		t = k1 & (x ^ (x << 9));
		x ^= t ^ (t >> 9);
		return x;
	}

	public static boolean isSingleton(long x) {
		return (x & (x - 1)) == 0L;
	}

	public static int popCount(long x) {
		return popCountLookup[(int) (x & 0xff)] + popCountLookup[(int) ((x >> 8) & 0xff)]
				+ popCountLookup[(int) ((x >> 16) & 0xff)] + popCountLookup[(int) ((x >> 24) & 0xff)]
				+ popCountLookup[(int) ((x >> 32) & 0xff)] + popCountLookup[(int) ((x >> 40) & 0xff)]
				+ popCountLookup[(int) ((x >> 48) & 0xff)] + popCountLookup[(int) (x >> 56)];
	}

	public static long[] splitIntoSingletons(long x) {
		long[] singletons = new long[popCount(x)];

		int i = 0;
		long l = 0;
		while (x != 0) {
			l = Long.lowestOneBit(x);
			singletons[i] = l;
			x ^= l;
			i++;
		}

		return singletons;
	}
	
	public static int bitScanForward(long x) {
		return bitScanLookup[(int) (((x ^ (x-1)) * debruijn64Magic) >> 58)];
	}
	
	public static long south(long x) {
		return x >> 8;
	}
	
	public static long north(long x) {
		return x << 8;
	}
	
	public static long east(long x) {
		return (x << 1) & (~CommonBitBoards.FILE_A);
	}
	
	public static long  northEast(long x) {
		return (x << 9) & (~CommonBitBoards.FILE_A);
	}
	
	public static long  southEast(long x) {
		return (x >> 7) & (~CommonBitBoards.FILE_A);
	}
	
	public static long  west(long x) {
		return (x >> 1) & (~CommonBitBoards.FILE_H);
	}
	
	public static long  southWest(long x) {
		return (x >> 9) & (~CommonBitBoards.FILE_H);
	}
	
	public static long northWest(long x) {
		return (x << 7) & (~CommonBitBoards.FILE_H);
	}
	
	public static long northNorthEast(long x) {
	    return (x & ~CommonBitBoards.FILE_H) << 17;
	}
	
	public static long northEastEast(long x) {
	    return (x & ~(CommonBitBoards.FILE_G | CommonBitBoards.FILE_H)) << 10;
	}
	
	public static long southEastEast(long x) {
	    return (x & ~(CommonBitBoards.FILE_G | CommonBitBoards.FILE_H)) >>  6;
	}
	
	public static long southSouthEast(long x) {
	    return (x & ~CommonBitBoards.FILE_H) >> 15;
	}
	
	public static long northNorthWest(long x) {
	    return (x & ~CommonBitBoards.FILE_A) << 15;
	}
	
	public static long northWestWest(long x) {
	    return (x & ~(CommonBitBoards.FILE_A | CommonBitBoards.FILE_B)) <<  6;
	}
	
	public static long southWestWest(long x) {
	    return (x & ~(CommonBitBoards.FILE_A | CommonBitBoards.FILE_B)) >> 10;
	}
	
	public static long southSouthWest(long x) {
	    return (x & ~CommonBitBoards.FILE_A) >> 17;
	}
	
	public static final class CommonBitBoards {

		public static final long FILE_A = 0x8080808080808080l;
		public static final long FILE_B = 0x4040404040404040l;
		public static final long FILE_C = 0x2020202020202020l;
		public static final long FILE_D = 0x1010101010101010l;
		public static final long FILE_E = 0x0808080808080808l;
		public static final long FILE_F = 0x0404040404040404l;
		public static final long FILE_G = 0x0202020202020202l;
		public static final long FILE_H = 0x0101010101010101l;
		
		public static final long RANK_1 = 0x00000000000000ffl;
		public static final long RANK_2 = 0x000000000000ff00l;
		public static final long RANK_3 = 0x0000000000ff0000l;
		public static final long RANK_4 = 0x00000000ff000000l;
		public static final long RANK_5 = 0x000000ff00000000l;
		public static final long RANK_6 = 0x0000ff0000000000l;
		public static final long RANK_7 = 0x00ff000000000000l;
		public static final long RANK_8 = 0xff00000000000000l;

	}
}
