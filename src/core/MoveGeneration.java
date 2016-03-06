package engine;

public class MoveGeneration {
	
	public static final long[] knightAttacksLookup = new long[64];
	static {
		long occupancy = 0, l1, l2, r1, r2, h1, h2;
		for(int i = 0; i < 64; i++) {
			occupancy = 1l << i;
			
			l1 = (occupancy >> 1) & 0x7f7f7f7f7f7f7f7fl;
			l2 = (occupancy >> 2) & 0x3f3f3f3f3f3f3f3fl;
			r1 = (occupancy << 1) & 0xfefefefefefefefel;
			r2 = (occupancy << 2) & 0xfcfcfcfcfcfcfcfcl;
			h1 = l1 | r1;
			h2 = l2 | r2;
			
			knightAttacksLookup[i] = (h1<<16) | (h1>>16) | (h2<<8) | (h2>>8);
		}
	}
	
	public final static long[] kingAttacksLookup = new long[64];
	static {
		long occupancy = 0;
		for(int i = 0; i < 64; i++) {
			occupancy = 1l << i;
			kingAttacksLookup[i] =
					BitOps.north(occupancy) | BitOps.south(occupancy) |
					BitOps.east(occupancy) | BitOps.west(occupancy) |
					BitOps.northEast(occupancy) | BitOps.northWest(occupancy) |
					BitOps.southEast(occupancy) | BitOps.southWest(occupancy);
		}
	}

	public static long kingAttacks(int kingSquare) {
		return kingAttacksLookup[kingSquare];
	}
	
	public static long knightAttacks(int knightSquare) {
		return knightAttacksLookup[knightSquare];
	}
}
