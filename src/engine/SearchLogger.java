package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import core.ChessPiece;
import core.Move;
import core.Move.Flags;
import core.Position;
import engine.TranspositionTable.Transposition;
import engine.TranspositionTable.TranspositionType;

public class SearchLogger {

	private PrintStream output;

	public SearchLogger(File out, long delay, long key) {
		try {
			this.output = new PrintStream(new FileOutputStream(out), true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		output.printf("Logging Search with delay of %d milliseconds and key %d\n", delay, key);
	}
	
	public void logIterativeDeepeningLevel(int level) {
		output.printf("Searching to depth of %d\n", level);
	}

	public void logTerminal(int ply, String value, long positionKey, int depth) {
		indent(ply);
		output.printf("Evaluating %d to %s at depth %d\n", positionKey, value, depth);
	}

	public void logTranspositionHit(int ply, Transposition entry) {
		indent(ply);
		output.printf("Cache hit %s: %d\n", TranspositionType.from(entry.type), entry.value);
	}

	public void logNewSearchLevel(int ply, int move) {
		indent(ply);
		output.printf("Searching %s\n", algebraic(move));
	}

	public void logSearchLevelReturn(int ply, int move, int value) {
		indent(ply);
		output.printf("Searching %s -> %d\n", algebraic(move), value);
	}

	public void logTimeLimitReached() {
		output.printf("Time restriction reached\n");
	}

	private void indent(int val) {
		for (int i = 0; i < val + 1; i++) {
			output.print("\t");
		}
	}

	public void close() {
		output.close();
	}

	private String algebraic(int move) {
		Move m = Move.from(move);
		String entire = "";

		entire += pieceTypeRepr(ChessPiece.getPieceType(m.getStartPiece()))
				+ Position.toString(m.getStartPosition()) + "-";

		if (m.getFlags() == Flags.QUIET.value()) {
			entire += "|";
		} else if (m.getFlags() == Flags.CAPTURE.value()) {
			entire += "X" + pieceTypeRepr(ChessPiece.getPieceType(m.getEndPiece()));
		} else if (m.getFlags() == Flags.CASTLE.value()) {
			entire += "OO";
		} else if (m.getFlags() == Flags.EN_PASSANT.value()) {
			entire += "EN";
		} else if (m.getFlags() == Flags.PROMOTION.value()) {
			entire += "^" + pieceTypeRepr(m.getPromotionPieceType());
		} else if (m.getFlags() == Flags.DOUBLE_PAWN_PUSH.value()) {
			entire += "||";
		}

		entire += "-" + Position.toString(m.getEndPosition());

		return entire;
	}

	private String[] pieceTypeRepr = { "P", "N", "B", "R", "Q", "K" };

	private String pieceTypeRepr(int piece) {
		if (piece == ChessPiece.NULL_PIECE) {
			return "";
		} else {
			return pieceTypeRepr[ChessPiece.getPieceType(piece)];
		}
	}

}
