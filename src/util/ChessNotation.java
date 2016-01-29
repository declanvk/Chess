package util;

import java.awt.Point;

import engine.BitBoardOperations;

public class ChessNotation {
	
	private static String[] byteLookupTable = new String[256];
	static {
		for(int i = 0; i < byteLookupTable.length; i++) {
			
			byteLookupTable[i] = (new String(new char[Math.min(7, Integer.numberOfLeadingZeros(i) - 24)]).replace("\0", "0")) + Integer.toBinaryString(i);
		}
	}
	
	public static String convertPointToAlgebraic(Point pos) {
		return pos.y + "" + ((char)('a' + (pos.x - 1)));
	}
	
	public static String convertBitBoardToString(long board) {
		String sBoard = Long.toBinaryString(board);
		sBoard = new String(new char[Long.numberOfLeadingZeros(board)]).replace('\0', '0') + sBoard;
		
		String[] characters = sBoard.split("(?<=\\G.{1})");
		sBoard = String.join(" ", characters);
		
		String[] chunks = sBoard.split("(?<=\\G.{16})");
		return String.join("\n", chunks);
	}
	
	public static void main(String[] args) {
		long a = 0x000000000000FFFFL;
		long b = BitBoardOperations.flipBoardVertically(a);
		
		System.out.println("a Board: ");
		System.out.println(convertBitBoardToString(a) + "\n");
		
		System.out.println("b Board: ");
		System.out.println(convertBitBoardToString(b) + "\n");

		System.out.println("Occupancy: ");
		System.out.println(convertBitBoardToString(a | b) + "\n");
		
		long pawns = 0x000000000000FF00L | BitBoardOperations.flipBoardVertically(0x000000000000FF00L);
		System.out.println("Pawns: ");
		System.out.println(convertBitBoardToString(pawns) + "\n");
		
		System.out.println("a Board Pawns: ");
		System.out.println(convertBitBoardToString(pawns & a) + "\n");
		
		System.out.println("b Board Pawns: ");
		System.out.println(convertBitBoardToString(pawns & b) + "\n");
	}
	
}