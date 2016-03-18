package engine;

public class ChessNotation {

	/**
	 * Convert the given raw Bitboard to a String board format, arranging all
	 * the ones and zeros in an 8x8 square
	 * 
	 * @param board
	 *            the board to convert to String format
	 * @return the given String representation of the given raw Bitboard
	 */
	public static String convertBitBoardToString(long board) {
		String sBoard = Long.toBinaryString(board);
		sBoard = new String(new char[Long.numberOfLeadingZeros(board)]).replace('\0', '0') + sBoard;

		String[] characters = sBoard.split("(?<=\\G.{1})");
		sBoard = "";
		for (int i = 0; i < characters.length; i++) {
			sBoard += characters[i] + (i < characters.length - 1 ? " " : "");
		}

		String[] chunks = sBoard.split("(?<=\\G.{16})");
		String result = "";
		for (int i = 0; i < chunks.length; i++) {
			result += chunks[i] + (i < chunks.length - 1 ? "\n" : "");
		}

		return result;
	}

	/**
	 * Converts all the given raw Bitboards to a String board format, and then
	 * arranges them all in a row
	 * 
	 * @param boards
	 *            the boards to convert to String format and collate
	 * @return the String representation of all the given raw Bitboards arranged
	 *         in a row
	 */
	public static String collateBitBoards(long... boards) {
		String[][] boardStrings = new String[boards.length][8];
		String temp = null;
		for (int i = 0; i < boards.length; i++) {
			temp = convertBitBoardToString(boards[i]);
			boardStrings[i] = temp.substring(0, temp.length()).split("\n");
		}

		String result = "";
		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < boardStrings.length; i++) {
				result += boardStrings[i][j] + ((i < boardStrings.length - 1) ? " \t" : " ");
			}
			result += "\n";
		}
		return result;
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

		long diagonal = 0x102040810204080L;
		long rank1 = 0x8080808080808080L;
		long masked = 0x2040810204080L;
		System.out.println(convertBitBoardToString((diagonal & (aBoard | bBoard))) + "\n");
		System.out
				.println(convertBitBoardToString(((diagonal & (aBoard | bBoard)) * rank1)) + "\n");
		System.out.println(
				convertBitBoardToString(((diagonal & (aBoard | bBoard)) * rank1) >>> 56) + "\n");

		System.out.println(convertBitBoardToString(masked) + "\n");
		System.out.println(convertBitBoardToString(rank1) + "\n");
		System.out.println(convertBitBoardToString((rank1 * masked)) + "\n");
		System.out.println(convertBitBoardToString((rank1 * masked) << 1) + "\n");
		System.out.println(convertBitBoardToString((rank1 * masked) >>> 56) + "\n");
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