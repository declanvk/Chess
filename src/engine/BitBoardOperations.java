package engine;

public class BitBoardOperations {

	public static long flipBoardVertically(long x) {
		long k1 = 0x00FF00FF00FF00FFL;
		long k2 = 0x0000FFFF0000FFFFL;
		x = ((x >>  8) & k1) | ((x & k1) <<  8);
		x = ((x >> 16) & k2) | ((x & k2) << 16);
		x = ( x >> 32)       | ( x       << 32);
		return x;
	}

}
