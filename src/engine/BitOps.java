package engine;

public class BitOps {
	
	/*
	 * Code for these methods is from:
	 * https://chessprogramming.wikispaces.com
	 * 
	 * I did produce any of this, only formatted and changed it to work with Java.
	 */
	
	private static byte[] popCountLookup = new byte[256];
	static {
		popCountLookup[0] = 0;
		for(int i = 1; i< popCountLookup.length; i++) {
			popCountLookup[i] = (byte) (popCountLookup[i / 2] + (i & 1));
		}
	}

	public static long flipVert(long board) {
		long k1 = 0x00FF00FF00FF00FFL;
		long k2 = 0x0000FFFF0000FFFFL;
		board = ((board >> 8) & k1) | ((board & k1) <<  8);
		board = ((board >> 16) & k2) | ((board & k2) << 16);
		board = ( board >> 32) | (board << 32);
		return board;
	}
	
	public static long flipHoriz(long board) {
		long k2 = 0x3333333333333333L;
		long k1 = 0x5555555555555555L;
		long k4 = 0x0f0f0f0f0f0f0f0fL;
		board = ((board >> 1) & k1) + 2*(board & k1);
		board = ((board >> 2) & k2) + 4*(board & k2);
		board = ((board >> 4) & k4) + 16*(board & k4);
		return board;
	}
	
	public static long flipDiagA1H8(long board) {
		long t;
		long k1 = 0x5500550055005500L;
		long k2 = 0x3333000033330000L;
		long k4 = 0x0f0f0f0f00000000L;
		t = k4 & (board ^ (board << 28));
		board ^= t ^ (t >> 28) ;
		t = k2 & (board ^ (board << 14));
		board ^= t ^ (t >> 14) ;
		t = k1 & (board ^ (board << 7));
		board ^= t ^ (t >> 7) ;
		return board;
	}
	
	public static long flipDiagA8H1(long x) {
		long t;
		long k1 = 0xaa00aa00aa00aa00L;
		long k2 = 0xcccc0000cccc0000L;
		long k4 = 0xf0f0f0f00f0f0f0fL;
		t = x ^ (x << 36) ;
		x ^= k4 & (t ^ (x >> 36));
		t = k2 & (x ^ (x << 18));
		x ^= t ^ (t >> 18) ;
		t = k1 & (x ^ (x << 9));
		x ^= t ^ (t >> 9) ;
		return x;
	}
	
	public static boolean isSingleton(long x) {
		return (x & (x - 1)) == 0L;
	}
	
	public static int popCount(long x) {
		return popCountLookup[(int) (x & 0xff)]
				+ popCountLookup[(int) ((x >>  8) & 0xff)]
				+ popCountLookup[(int) ((x >> 16) & 0xff)]
				+ popCountLookup[(int) ((x >> 24) & 0xff)]
				+ popCountLookup[(int) ((x >> 32) & 0xff)]
				+ popCountLookup[(int) ((x >> 40) & 0xff)]
				+ popCountLookup[(int) ((x >> 48) & 0xff)]
				+ popCountLookup[ (int) (x >> 56)];
	}
	
	public static long[] splitIntoSingletons(long x) {
		long[] singletons = new long[popCount(x)];
		
		int i = 0;
		long l = 0;
		while(x != 0) {
			l = Long.lowestOneBit(x);
			singletons[i] = l;
			x ^= l;
			i++;
		}
		
		return singletons;
	}
}
