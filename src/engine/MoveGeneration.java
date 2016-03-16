package engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import core.Bitboard;
import core.CastlingBitFlags;
import core.ChessBoard;
import core.ChessColor;
import core.ChessPiece;
import core.Move;
import core.PieceType;
import core.Position;
import util.Pair;

public class MoveGeneration {

	public static final int[] rookDirections = { Position.N, Position.S, Position.E, Position.W };
	public static final int[] bishopDirections =
			{ Position.NE, Position.NW, Position.SE, Position.SW };
	public static final int[] knightOffsets = { Position.NNE, Position.NEE, Position.SEE,
			Position.SSE, Position.SSW, Position.SWW, Position.NWW, Position.NNW };
	public static final int[] kingOffsets = { Position.N, Position.S, Position.E, Position.W,
			Position.NE, Position.NW, Position.SE, Position.SW };
	public static final int[] queenDirections = { Position.N, Position.S, Position.E, Position.W,
			Position.NE, Position.NW, Position.SE, Position.SW };
	public static final int[][] pawnOffsets =
			{ { Position.N, Position.NE, Position.NW }, { Position.S, Position.SE, Position.SW } };
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
					return Integer.compare(o1.second(), o2.second());
				}

			};

	public static TreeSet<Pair<Integer, Integer>> getMoves(ChessBoard position, boolean quiescent) {
		boolean isCheck = position.isCheck();
		TreeSet<Pair<Integer, Integer>> moves = getMoves(position);

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

		return moves;
	}

	private static TreeSet<Pair<Integer, Integer>> getMoves(ChessBoard position) {
		TreeSet<Pair<Integer, Integer>> moves = new TreeSet<Pair<Integer, Integer>>(moveSorter);

		// Get queen moves
		int startPos =
				position.getPieces(position.getActiveColor(), PieceType.QUEEN.value()).getSingle();
		getMovesSliding(moves, startPos, queenDirections, position);

		// Get king moves
		startPos =
				position.getPieces(position.getActiveColor(), PieceType.KING.value()).getSingle();
		getMovesNonSliding(moves, startPos, kingOffsets, position);

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
		Bitboard pawnAttacks = pawnPosition.clone();
		pawnAttacks.operate(new Bitboard.BitboardOperation() {

			@Override
			public long operate(long board) {
				if (position.getActiveColor() == ChessColor.WHITE.value()) {
					long nwBoard = (board << 9) & ~Position.File.F_H.board().value();
					long neBoard = (board << 7) & ~Position.File.F_A.board().value();
					board = (nwBoard | neBoard);
				} else {
					long nwBoard = (board >>> 9) & ~Position.File.F_A.board().value();
					long neBoard = (board >>> 7) & ~Position.File.F_H.board().value();
					board = nwBoard | neBoard;
				}
				board &= enemyOccPlusEnPass.value();
				return board;
			}

		});

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

		Bitboard toPromote = Bitboard.or(pawnAttacks, pawnSingleMoves, pawnDoubleMoves,
				(position.getActiveColor() == ChessColor.WHITE.value())
						? Position.Rank.R_8.board().opposite()
						: Position.Rank.R_1.board().opposite());

		for (int endPos : pawnAttacks) {
			int flags = Move.Flags.CAPTURE.value();
			if (endPos == position.getEnPassantPosition()) {
				flags = Move.Flags.EN_PASSANT.value();
			}

			addPawnMoves(moves, startPos, position, toPromote, flags, endPos);
		}

		for (int endPos : pawnSingleMoves) {
			int flags = Move.Flags.QUIET.value();
			addPawnMoves(moves, startPos, position, toPromote, flags, endPos);
		}

		for (int endPos : pawnDoubleMoves) {
			int flags = Move.Flags.DOUBLE_PAWN_PUSH.value();
			addPawnMoves(moves, startPos, position, toPromote, flags, endPos);
		}
	}

	private static void addPawnMoves(Collection<Pair<Integer, Integer>> moves, int startPos,
			final ChessBoard position, Bitboard toPromote, int flags, int endPos) {
		assert startPos != endPos;
		if (!toPromote.check(endPos)) {
			int move = Move.value(position.get(startPos), position.get(endPos), startPos, endPos,
					flags, PieceType.NULL_PROMOTION);
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

	public static HashMap<Integer, ArrayList<Move>> getSortedMoves(ChessBoard position) {
		HashMap<Integer, ArrayList<Move>> moves = new HashMap<Integer, ArrayList<Move>>();
		ArrayList<Pair<Integer, Integer>> queenMoves = new ArrayList<Pair<Integer, Integer>>();
		ArrayList<Pair<Integer, Integer>> kingMoves = new ArrayList<Pair<Integer, Integer>>();
		ArrayList<Pair<Integer, Integer>> rookMoves = new ArrayList<Pair<Integer, Integer>>();
		ArrayList<Pair<Integer, Integer>> bishopMoves = new ArrayList<Pair<Integer, Integer>>();
		ArrayList<Pair<Integer, Integer>> knightMoves = new ArrayList<Pair<Integer, Integer>>();
		ArrayList<Pair<Integer, Integer>> pawnMoves = new ArrayList<Pair<Integer, Integer>>();

		// Get queen moves
		int startPos =
				position.getPieces(position.getActiveColor(), PieceType.QUEEN.value()).getSingle();
		getMovesSliding(queenMoves, startPos, queenDirections, position);
		moves.put(startPos, convertMoveValueToMove(queenMoves));

		// Get king moves
		startPos =
				position.getPieces(position.getActiveColor(), PieceType.KING.value()).getSingle();
		getMovesNonSliding(kingMoves, startPos, kingOffsets, position);
		moves.put(startPos, convertMoveValueToMove(kingMoves));

		// Get rook moves
		for (Integer pos : position.getPieces(position.getActiveColor(), PieceType.ROOK.value())) {
			getMovesSliding(rookMoves, pos, rookDirections, position);
			moves.put(pos, convertMoveValueToMove(rookMoves));
		}

		// Get bishop moves
		for (Integer pos : position.getPieces(position.getActiveColor(),
				PieceType.BISHOP.value())) {
			getMovesSliding(bishopMoves, pos, bishopDirections, position);
			moves.put(pos, convertMoveValueToMove(bishopMoves));
		}

		// Get knight moves
		for (Integer pos : position.getPieces(position.getActiveColor(),
				PieceType.KNIGHT.value())) {
			getMovesNonSliding(knightMoves, pos, knightOffsets, position);
			moves.put(pos, convertMoveValueToMove(knightMoves));
		}

		// Get pawn moves
		for (Integer pos : position.getPieces(position.getActiveColor(), PieceType.PAWN.value())) {
			getPawnMoves(pawnMoves, pos, position);
			moves.put(pos, convertMoveValueToMove(pawnMoves));
		}

		return moves;
	}

	private static void addMove(Collection<Pair<Integer, Integer>> moves, ChessBoard position,
			int move) {
		position.move(move);
		boolean make = !position.isCheck(ChessColor.opposite(position.getActiveColor()));
		position.unmove(move);
		if (make) {
			moves.add(new Pair<Integer, Integer>(move, position.staticExchangeEvaluation(move)));
		}
	}

	private static ArrayList<Move> convertMoveValueToMove(ArrayList<Pair<Integer, Integer>> moves) {
		ArrayList<Move> newMoves = new ArrayList<Move>();

		for (Pair<Integer, Integer> value : moves) {
			newMoves.add(Move.from(value.first()));
		}

		return newMoves;
	}
}
