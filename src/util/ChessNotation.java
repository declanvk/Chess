package util;

import java.awt.Point;

public class ChessNotation {

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
		long aBoard = 0x000000000000FFFFL;
		long bBoard = 0xFFFF000000000000L;
		
		printPieces(aBoard, bBoard, "Boards");
		
		long aPawns = 0x000000000000FF00L;
		long bPawns = 0x00FF000000000000L;
		
		printPieces(aPawns, bPawns, "Pawns");
		
		long aRooks = 0x0000000000000081L;
		long bRooks = 0x8100000000000000L;
		
		printPieces(aRooks, bRooks, "Rooks");
		
		long aKnight = 0x0000000000000042L;
		long bKnight = 0x4200000000000000L;
		
		printPieces(aKnight, bKnight, "Knights");
		
		long aBishop = 0x0000000000000024L;
		long bBishop = 0x2400000000000000L;
		
		printPieces(aBishop, bBishop, "Bishops");
		
		long aQueen = 0x00000000000000008L;
		long aKings = 0x00000000000000010L;
		
		long bQueen = 0x01000000000000000L;
		long bKings = 0x00800000000000000L;
		
		printPieces(aQueen, bQueen, "Queens");
		printPieces(aKings, bKings, "Kings");
		
	}
	
	private static void printPieces(long a, long b, String label) {
		System.out.println(label + ":");
		System.out.println(convertBitBoardToString(a | b) + "\n");
		
		System.out.println("a " + label + ":");
		System.out.println(convertBitBoardToString(a) + "\n");
		
		System.out.println("b " + label + ":");
		System.out.println(convertBitBoardToString(b) + "\n");
	}
	
}