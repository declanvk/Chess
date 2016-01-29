package engine;

public class BitBoardOps {

	public static long flipVert(long x) {
		long k1 = 0x00FF00FF00FF00FFL;
		long k2 = 0x0000FFFF0000FFFFL;
		x = ((x >>  8) & k1) | ((x & k1) <<  8);
		x = ((x >> 16) & k2) | ((x & k2) << 16);
		x = ( x >> 32)       | ( x       << 32);
		return x;
	}
	
	public static long flipHoriz(long x) {
		long k2 = 0x3333333333333333L;
		long k1 = 0x5555555555555555L;
		long k4 = 0x0f0f0f0f0f0f0f0fL;
		x = ((x >> 1) & k1) +  2*(x & k1);
		x = ((x >> 2) & k2) +  4*(x & k2);
		x = ((x >> 4) & k4) + 16*(x & k4);
		return x;
	}
}
