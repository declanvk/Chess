package engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import core.Bitboard;
import core.CastlingBitFlags;
import core.ChessBoard;
import core.ChessColor;
import core.ChessPiece;
import core.Move;
import core.PieceType;
import core.Position;
import util.Pair;

/**
 * A collection of methods that produce legal Moves given a ChessBoars
 * 
 * @author declan
 *
 */
public class MoveGeneration {

	/**
	 * The directions that the rook piece travels in
	 */
	public static final int[] rookDirections = { Position.N, Position.S, Position.E, Position.W };

	/**
	 * The directions that the bishop piece travels in
	 */
	public static final int[] bishopDirections =
			{ Position.NE, Position.NW, Position.SE, Position.SW };

	/**
	 * The offsets that the knight piece moves on
	 */
	public static final int[] knightOffsets = { Position.NNE, Position.NEE, Position.SEE,
			Position.SSE, Position.SSW, Position.SWW, Position.NWW, Position.NNW };

	/**
	 * The offsets that the king piece moves on
	 */
	public static final int[] kingOffsets = { Position.N, Position.S, Position.E, Position.W,
			Position.NE, Position.NW, Position.SE, Position.SW };

	/**
	 * The directions that the queen piece travels in
	 */
	public static final int[] queenDirections = { Position.N, Position.S, Position.E, Position.W,
			Position.NE, Position.NW, Position.SE, Position.SW };

	/**
	 * The offsets that the pawn piece moves and attacks on, separated by color
	 */
	public static final int[][] pawnOffsets =
			{ { Position.N, Position.NE, Position.NW }, { Position.S, Position.SE, Position.SW } };

	/**
	 * The initial positions of the king, separated by color
	 */
	public static final int[] initialKingPos =
			{ Position.from(Position.File.F_E, Position.Rank.R_1),
					Position.from(Position.File.F_E, Position.Rank.R_8) };

	private static final Bitboard[][] castlingEmptyMask = new Bitboard[2][2];

	static {
		castlingEmptyMask[CastlingBitFlags.WHITE_QUEENSIDE.color()][CastlingBitFlags.WHITE_QUEENSIDE
				.side()] = Bitboard.from(0xeL);
		castlingEmptyMask[CastlingBitFlags.WHITE_KINGSIDE.color()][CastlingBitFlags.WHITE_KINGSIDE
				.side()] = Bitboard.from(0x60L);
		castlingEmptyMask[CastlingBitFlags.BLACK_QUEENSIDE.color()][CastlingBitFlags.BLACK_QUEENSIDE
				.side()] = Bitboard.from(0xe00000000000000L);
		castlingEmptyMask[CastlingBitFlags.BLACK_KINGSIDE.color()][CastlingBitFlags.BLACK_KINGSIDE
				.side()] = Bitboard.from(0x6000000000000000L);
	}

	private static Comparator<Pair<Integer, Integer>> moveSorter =
			new Comparator<Pair<Integer, Integer>>() {

				@Override
				public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
					return Integer.compare(o2.second(), o1.second());
				}

			};

	/**
	 * Returns all legal moves for the given position, taking into account
	 * whether or not the search is quiescent
	 * 
	 * @param position
	 *            the position to generate the moves for
	 * @param quiescent
	 *            flag telling the move generation whether or not to ignore
	 *            certain moves
	 * @return all legal moves for the given position, taking into account
	 *         whether or not the search is quiescent
	 */
	public static ArrayList<Integer> getMoves(ChessBoard position, boolean quiescent) {
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for (Pair<Integer, Integer> moveValue : getMoveValues(position, quiescent)) {
			moves.add(moveValue.first());
		}

		return moves;
	}

	private static ArrayList<Pair<Integer, Integer>> getMoveValues(ChessBoard position,
			boolean quiescent) {
		boolean isCheck = position.isCheck();
		ArrayList<Pair<Integer, Integer>> moves = getMoves(position);

		if (!quiescent) {
			if (!isCheck) {
				getCastlingMoves(moves, position);
			}
		} else {
			// If quiescent remove all moves that don't capture
			Iterator<Pair<Integer, Integer>> iter = moves.iterator();
			Pair<Integer, Integer> move = null;
			while (iter.hasNext()) {
				move = iter.next();
				if (Move.getEndPiece(move.first()) != ChessPiece.NULL_PIECE) {
					iter.remove();
				}
			}
		}

		Collections.sort(moves, moveSorter);
		return moves;
	}

	private static ArrayList<Pair<Integer, Integer>> getMoves(ChessBoard position) {
		ArrayList<Pair<Integer, Integer>> moves = new ArrayList<Pair<Integer, Integer>>();

		// Get queen moves
		for (Integer pos : position.getPieces(position.getActiveColor(), PieceType.QUEEN.value())) {
			getMovesSliding(moves, pos, queenDirections, position);
		}

		// Get king moves
		for (Integer pos : position.getPieces(position.getActiveColor(), PieceType.KING.value())) {
			getMovesNonSliding(moves, pos, kingOffsets, position);
		}

		// Get rook moves
		for (Integer pos : position.getPieces(position.getActiveColor(), PieceType.ROOK.value())) {
			getMovesSliding(moves, pos, rookDirections, position);
		}

		// Get bishop moves
		for (Integer pos : position.getPieces(position.getActiveColor(),
				PieceType.BISHOP.value())) {
			getMovesSliding(moves, pos, bishopDirections, position);
		}

		// Get knight moves

		for (Integer pos : position.getPieces(position.getActiveColor(),
				PieceType.KNIGHT.value())) {
			getMovesNonSliding(moves, pos, knightOffsets, position);
		}

		// Get pawn moves
		for (Integer pos : position.getPieces(position.getActiveColor(), PieceType.PAWN.value())) {
			getPawnMoves(moves, pos, position);
		}

		return moves;
	}

	private static void getMovesSliding(Collection<Pair<Integer, Integer>> moves, int startPos,
			int[] directions, ChessBoard position) {
		int startPiece = position.get(startPos);
		for (int direction : directions) {
			int endPos = startPos + direction;
			while (Position.isValid(endPos)) {
				int endPiece = position.get(endPos);
				if (ChessPiece.NULL_PIECE == endPiece) {
					int flags = Move.Flags.QUIET.value();
					int move = Move.value(startPiece, endPiece, startPos, endPos, flags,
							PieceType.NULL_PROMOTION);
					addMove(moves, position, move);
				} else if (ChessPiece.getColor(endPiece) != ChessPiece.getColor(startPiece)) {
					int flags = Move.Flags.CAPTURE.value();
					int move = Move.value(startPiece, endPiece, startPos, endPos, flags,
							PieceType.NULL_PROMOTION);
					addMove(moves, position, move);

					break;
				} else {
					break;
				}
				endPos += direction;
			}
		}
	}

	private static void getMovesNonSliding(Collection<Pair<Integer, Integer>> moves, int startPos,
			int[] offsets, ChessBoard position) {
		int startPiece = position.get(startPos);
		for (int offset : offsets) {
			int endPos = startPos + offset;
			if (Position.isValid(endPos)) {
				int endPiece = position.get(endPos);
				if (ChessPiece.NULL_PIECE == endPiece) {
					int flags = Move.Flags.QUIET.value();
					int move = Move.value(startPiece, endPiece, startPos, endPos, flags,
							PieceType.NULL_PROMOTION);
					addMove(moves, position, move);
				} else if (ChessPiece.getColor(endPiece) != ChessPiece.getColor(startPiece)) {
					int flags = Move.Flags.CAPTURE.value();
					int move = Move.value(startPiece, endPiece, startPos, endPos, flags,
							PieceType.NULL_PROMOTION);
					addMove(moves, position, move);
				}
			}
		}
	}

	private static void getPawnMoves(Collection<Pair<Integer, Integer>> moves, int startPos,
			final ChessBoard position) {

		// The bitboard shifting techniques used below would be better used with
		// whole populations of pawns, however I coded just as a place to store
		// the code given the event of needing it again. Plus it's fairly
		// efficient either way.
		Bitboard pawnPosition = new Bitboard();
		pawnPosition.set(startPos);

		final Bitboard enemyOccPlusEnPass =
				position.getOccupany(ChessColor.opposite(position.getActiveColor()));
		if (position.getEnPassantPosition() != Position.NULL_POSITION)
			enemyOccPlusEnPass.set(position.getEnPassantPosition());
		Bitboard pawnAttacks = new Bitboard();
		for (int i = 1; i < pawnOffsets[position.getActiveColor()].length; i++) {
			int endPos = startPos + pawnOffsets[position.getActiveColor()][i];
			if (Position.isValid(endPos) && position.get(endPos) != ChessPiece.NULL_PIECE) {
				pawnAttacks.set(endPos);
			}
		}
		pawnAttacks = Bitboard.and(pawnAttacks, enemyOccPlusEnPass);

		Bitboard pawnSingleMoves = pawnPosition.clone();
		pawnSingleMoves.operate(new Bitboard.BitboardOperation() {

			@Override
			public long operate(long board) {
				if (position.getActiveColor() == ChessColor.WHITE.value()) {
					board <<= 8;
				} else {
					board >>>= 8;
				}
				board &= ~position.getOccupany(ChessBoard.BOTH_COLOR).value();
				return board;
			}

		});

		Bitboard pawnDoubleMoves = pawnPosition.clone();
		pawnDoubleMoves.operate(new Bitboard.BitboardOperation() {

			@Override
			public long operate(long board) {
				long pawnHome = (position.getActiveColor() == ChessColor.WHITE.value())
						? Position.Rank.R_2.board().value() : Position.Rank.R_7.board().value();
				board &= pawnHome;
				for (int i = 0; i < 2; i++) {
					if (position.getActiveColor() == ChessColor.WHITE.value()) {
						board <<= 8;
					} else {
						board >>>= 8;
					}
					board &= ~position.getOccupany(ChessBoard.BOTH_COLOR).value();
				}
				return board;
			}

		});

		Bitboard promoteRank = (position.getActiveColor() == ChessColor.WHITE.value())
				? Position.Rank.R_8.board().opposite() : Position.Rank.R_1.board().opposite();

		for (int endPos : pawnAttacks) {
			int flags = Move.Flags.CAPTURE.value();
			if (endPos == position.getEnPassantPosition()) {
				flags = Move.Flags.EN_PASSANT.value();
			}

			addPawnMoves(moves, startPos, position, promoteRank, flags, endPos);
		}

		for (int endPos : pawnSingleMoves) {
			int flags = Move.Flags.QUIET.value();
			addPawnMoves(moves, startPos, position, promoteRank, flags, endPos);
		}

		for (int endPos : pawnDoubleMoves) {
			int flags = Move.Flags.DOUBLE_PAWN_PUSH.value();
			addPawnMoves(moves, startPos, position, promoteRank, flags, endPos);
		}
	}

	private static void addPawnMoves(Collection<Pair<Integer, Integer>> moves, int startPos,
			final ChessBoard position, Bitboard toPromote, int flags, int endPos) {
		if (!toPromote.check(endPos)) {
			int endPosShift = 0;
			if (flags == Move.Flags.EN_PASSANT.value()) {
				endPosShift = (position.getActiveColor() == ChessColor.WHITE.value() ? Position.S
						: Position.N);
			}

			int move = Move.value(position.get(startPos), position.get(endPos + endPosShift),
					startPos, endPos, flags, PieceType.NULL_PROMOTION);
			addMove(moves, position, move);
		} else {
			flags = Move.Flags.PROMOTION.value();
			int moveKnight = Move.value(position.get(startPos), position.get(endPos), startPos,
					endPos, flags, PieceType.KNIGHT.value());
			int moveBishop = Move.value(position.get(startPos), position.get(endPos), startPos,
					endPos, flags, PieceType.BISHOP.value());
			int moveRook = Move.value(position.get(startPos), position.get(endPos), startPos,
					endPos, flags, PieceType.ROOK.value());
			int moveQueen = Move.value(position.get(startPos), position.get(endPos), startPos,
					endPos, flags, PieceType.QUEEN.value());

			addMove(moves, position, moveKnight);
			addMove(moves, position, moveBishop);
			addMove(moves, position, moveRook);
			addMove(moves, position, moveQueen);
		}
	}

	private static void getCastlingMoves(Collection<Pair<Integer, Integer>> moves,
			ChessBoard position) {
		for (CastlingBitFlags flag : CastlingBitFlags.from(position.getCastling())) {
			if (flag.color() == position.getActiveColor()
					&& position.isEmptyMask(castlingEmptyMask[flag.color()][flag.side()])
					&& !position.isAttacked(flag.getMidPosition(),
							ChessColor.opposite(position.getActiveColor()))) {
				int kingPos = initialKingPos[position.getActiveColor()];
				int kingPiece = position.get(kingPos);
				int move = Move.value(kingPiece, ChessPiece.NULL_PIECE, kingPos,
						flag.getEndPosition(), Move.Flags.CASTLE.value(), PieceType.NULL_PROMOTION);

				addMove(moves, position, move);
			}
		}
	}

	/**
	 * Returns all legal moves for the given position, sorted into the position
	 * they originate from
	 * 
	 * @param position
	 *            the position to generate moves for
	 * @return all legal moves for the given position, sorted into the position
	 *         they originate from
	 */
	public static HashMap<Integer, ArrayList<Move>> getSortedMoves(ChessBoard position) {
		HashMap<Integer, ArrayList<Move>> moveMap = new HashMap<Integer, ArrayList<Move>>();
		ArrayList<Pair<Integer, Integer>> moves = getMoveValues(position, false);

		for (Pair<Integer, Integer> moveValue : moves) {
			Move move = Move.from(moveValue.first());
			if (!moveMap.containsKey(move.getStartPosition())) {
				moveMap.put(move.getStartPosition(), new ArrayList<Move>());
			}

			moveMap.get(move.getStartPosition()).add(move);
		}

		return moveMap;
	}

	private static void addMove(Collection<Pair<Integer, Integer>> moves, ChessBoard position,
			int move) {
		int checkColor = ChessPiece.getColor(Move.getStartPiece(move));

		position.move(move);
		boolean isCheck = position.isCheck(checkColor);
		position.unmove(move);

		if (!isCheck) {
			moves.add(new Pair<Integer, Integer>(move, position.staticExchangeEvaluation(move)));
		}
	}
}
