package core;

import java.util.Arrays;
import java.util.EnumSet;

import core.Bitboard.BitboardOperation;
import core.Position.File;
import core.Position.Rank;
import engine.MoveGeneration;

public class ChessBoard {

	public static final int BOTH_COLOR = 2;

	private final int[] board; // indexed by position
	private final int[] materialScore; // indexed by color;
	private final Bitboard[][] pieces; // Indexed by color and type
	private final Bitboard[] occupancy; // Indexed by color plus one for both

	private final State[] savedStates;
	private int stateIndex;

	private int castlingPermissions;
	private int enPassantPosition;
	private int activeColor;
	private int halfMoveClock;

	private class State {
		public final int castlingPermissions;
		public final int enPassantPosition;
		public final int halfMoveClock;

		public State(int castling, int enPassant, int clock) {
			this.castlingPermissions = castling;
			this.enPassantPosition = enPassant;
			this.halfMoveClock = clock;
		}
	}

	public ChessBoard() {
		this.board = new int[Position.NUM_TOTAL_VALUES];
		Arrays.fill(this.board, ChessPiece.NULL_PIECE);

		this.materialScore = new int[ChessColor.values().length];
		this.pieces = new Bitboard[ChessColor.values().length][PieceType.values().length];
		for (ChessColor color : ChessColor.values()) {
			for (PieceType type : PieceType.values()) {
				pieces[color.value()][type.value()] = new Bitboard();
			}
		}

		this.occupancy = new Bitboard[ChessColor.values().length + 1];
		for (int i = 0; i < occupancy.length; i++) {
			occupancy[i] = new Bitboard();
		}

		// TODO Implement a standard for this and a class with this as a
		// constant
		this.savedStates = new State[2048];
		this.stateIndex = 0;

		this.castlingPermissions = CastlingBitFlags.NO_CASTLING;
		this.enPassantPosition = Position.NULL_POSITION;
		this.activeColor = ChessColor.WHITE.value();
		this.halfMoveClock = 0;
	}

	public int getCastling() {
		return castlingPermissions;
	}

	public int getEnPassantPosition() {
		return enPassantPosition;
	}

	public int getActiveColor() {
		return activeColor;
	}

	public int getHalfTurnClock() {
		return halfMoveClock;
	}

	public int getScore(int color) {
		assert ChessColor.isValid(color);

		return materialScore[color];
	}

	public ChessPiece getObject(int position) {
		assert Position.isValid(position);

		int piece = board[position];

		return (piece != ChessPiece.NULL_PIECE) ? ChessPiece.from(piece) : null;
	}

	public int get(int position) {
		assert Position.isValid(position);

		return board[position];
	}

	public Bitboard getPieces(int color, int type) {
		assert ChessColor.isValid(color);
		assert PieceType.isValid(type);

		return pieces[color][type].clone();
	}

	public Bitboard getOccupany(int color) {
		assert ChessColor.isValid(color) || color == BOTH_COLOR;

		return occupancy[color].clone();
	}

	private void set(int position, int piece) {
		assert Position.isValid(position);
		assert ChessPiece.isValid(piece);
		assert isEmpty(position);

		materialScore[ChessPiece.getColor(piece)] -= ChessPiece.getScore(piece);
		pieces[ChessPiece.getColor(piece)][ChessPiece.getPieceType(piece)].set(position);
		occupancy[ChessPiece.getColor(piece)].set(position);
		occupancy[BOTH_COLOR].set(position);

		board[position] = piece;
	}

	private int clear(int position) {
		assert Position.isValid(position);
		assert !isEmpty(position);

		int oldPiece = board[position];

		materialScore[ChessPiece.getColor(oldPiece)] -= ChessPiece.getScore(oldPiece);
		pieces[ChessPiece.getColor(oldPiece)][ChessPiece.getPieceType(oldPiece)].clear(position);
		occupancy[ChessPiece.getColor(oldPiece)].clear(position);
		occupancy[BOTH_COLOR].clear(position);

		board[position] = ChessPiece.NULL_PIECE;

		return oldPiece;
	}

	public boolean isEmpty(int position) {
		assert Position.isValid(position);

		return board[position] == ChessPiece.NULL_PIECE;
	}

	public boolean isEmptyMask(Bitboard board) {
		return isEmptyOfColorMask(board, BOTH_COLOR);
	}

	public boolean isEmptyOfColorMask(Bitboard board, int psuedoColor) {
		return Bitboard.and(board, occupancy[psuedoColor]).size() == 0;
	}

	public boolean isCheck() {
		return isCheck(activeColor);
	}

	public boolean isCheck(int color) {
		return isAttacked(pieces[color][PieceType.KING.value()].getSingle(),
				ChessColor.opposite(color));
	}

	public int staticExchangeEvaluation(int move) {
		return 1; // TODO (wait until chess engine started) implement static
					// exchange evaluation
	}

	public int evaluate() {
		int material = materialScore[activeColor];
		int mobility = 0;

		final Bitboard enemyOccupancy = occupancy[ChessColor.opposite(activeColor)];
		final Bitboard friendOccupancy = occupancy[activeColor];

		Bitboard pawnMobility = pieces[activeColor][PieceType.PAWN.value()].clone();
		pawnMobility.operate(new BitboardOperation() {

			@Override
			public long operate(long board) {
				long attacks = 0L;
				if (activeColor == ChessColor.WHITE.value()) {
					long nw = (board << 7) & ~Position.File.F_H.board().value();
					long ne = (board << 9) & ~Position.File.F_A.board().value();
					attacks = nw | ne;
				} else {
					long ne = (board << 7) & ~Position.File.F_A.board().value();
					long nw = (board << 9) & ~Position.File.F_H.board().value();
					attacks = nw | ne;
				}
				attacks &= enemyOccupancy.value();

				long moves = 0L;
				if (activeColor == ChessColor.WHITE.value()) {
					long singleMove = (board << 8);
					long doubleMove = ((singleMove & ~enemyOccupancy.value()) << 8);
					moves = singleMove | doubleMove;
				} else {
					long singleMove = (board >>> 8);
					long doubleMove = ((singleMove & ~enemyOccupancy.value()) >>> 8);
					moves = singleMove | doubleMove;
				}
				moves &= ~enemyOccupancy.value();

				return moves | attacks;
			}

		});

		Bitboard knightMobility = pieces[activeColor][PieceType.KNIGHT.value()].clone();
		knightMobility.operate(new BitboardOperation() {

			private final int[] shifts = { 8 - 1 - 1, 8 + 8 - 1, 8 + 8 + 1, 8 + 1 + 1 };

			private final long[] maskOff =
					{ Position.File.F_A.board().value() | Position.File.F_B.board().value(),
							Position.File.F_G.board().value() | Position.File.F_H.board().value() };

			@Override
			public long operate(long board) {
				long[] results = new long[8];
				for (int i = 0; i < shifts.length; i++) {
					results[i] |= (board << shifts[i]) & ~maskOff[i / 2];
					results[i + 4] |= (board >>> shifts[i]) & ~maskOff[(i / 2) ^ 1];
				}

				long result = 0L;
				for (int i = 0; i < results.length; i++) {
					result |= results[i];
				}

				return result & ~friendOccupancy.value();
			}
		});

		Bitboard kingMobility = pieces[activeColor][PieceType.KNIGHT.value()].clone();
		kingMobility.operate(new BitboardOperation() {

			@Override
			public long operate(long board) {
				long[] results = new long[8];
				results[0] = board << 8;
				results[1] = board >> 8;
				results[2] = (board << 1) & ~Position.File.F_A.board().value();
				results[3] = (board >> 1) & ~Position.File.F_H.board().value();

				results[4] = (board << 9) & ~Position.File.F_A.board().value();
				results[5] = (board >> 7) & ~Position.File.F_H.board().value();
				results[6] = (board << 7) & ~Position.File.F_A.board().value();
				results[7] = (board >> 9) & ~Position.File.F_H.board().value();

				long result = 0L;
				for (int i = 0; i < results.length; i++) {
					result |= results[i];
				}

				return result & ~friendOccupancy.value();
			}

		});

		Bitboard rookMobility = new Bitboard();
		for (Integer pos : pieces[activeColor][PieceType.ROOK.value()].clone()) {
			for (int direction : MoveGeneration.rookDirections) {
				int position = pos + direction;
				while (Position.isValid(position)) {
					if (ChessPiece.isValid(this.get(position))) {
						if (ChessPiece.getColor(this.get(position)) != activeColor) {
							rookMobility.set(position);
						}

						break;
					} else {
						rookMobility.set(position);
						position += direction;
					}
				}
			}
		}

		Bitboard bishopMobility = new Bitboard();
		for (Integer pos : pieces[activeColor][PieceType.BISHOP.value()].clone()) {
			for (int direction : MoveGeneration.bishopDirections) {
				int position = pos + direction;
				while (Position.isValid(position)) {
					if (ChessPiece.isValid(this.get(position))) {
						if (ChessPiece.getColor(this.get(position)) != activeColor) {
							bishopMobility.set(position);
						}

						break;
					} else {
						bishopMobility.set(position);
						position += direction;
					}
				}
			}
		}

		Bitboard queenMobility = new Bitboard();
		for (Integer pos : pieces[activeColor][PieceType.QUEEN.value()].clone()) {
			for (int direction : MoveGeneration.queenDirections) {
				int position = pos + direction;
				while (Position.isValid(position)) {
					if (ChessPiece.isValid(this.get(position))) {
						if (ChessPiece.getColor(this.get(position)) != activeColor) {
							queenMobility.set(position);
						}

						break;
					} else {
						queenMobility.set(position);
						position += direction;
					}
				}
			}
		}

		mobility = pawnMobility.size() + knightMobility.size() + kingMobility.size()
				+ rookMobility.size() + bishopMobility.size() + queenMobility.size();

		return (material + mobility) * (activeColor == ChessColor.WHITE.value() ? 1 : -1);
	}

	// fast return, returns as soon as it finds an attacker
	public boolean isAttacked(int position, int attackerColor) {
		int attackingPawn = ChessPiece.fromRaw(attackerColor, PieceType.PAWN.value());
		int attackingKnight = ChessPiece.fromRaw(attackerColor, PieceType.KNIGHT.value());
		int attackingKing = ChessPiece.fromRaw(attackerColor, PieceType.KING.value());
		int attackingBishop = ChessPiece.fromRaw(attackerColor, PieceType.BISHOP.value());
		int attackingRook = ChessPiece.fromRaw(attackerColor, PieceType.ROOK.value());
		int attackingQueen = ChessPiece.fromRaw(attackerColor, PieceType.QUEEN.value());

		for (int i = 1; i < MoveGeneration.pawnOffsets[attackerColor].length; i++) {
			int pawnPos = position - MoveGeneration.pawnOffsets[attackerColor][i];
			if (Position.isValid(pawnPos) && this.get(pawnPos) == attackingPawn) {
				return true;
			}
		}

		for (int i = 0; i < MoveGeneration.knightOffsets.length; i++) {
			int knightPos = position + MoveGeneration.knightOffsets[i];
			if (Position.isValid(knightPos) && this.get(knightPos) == attackingKnight) {
				return true;
			}
		}

		for (int i = 0; i < MoveGeneration.rookDirections.length; i++) {
			int rookPos = position + MoveGeneration.rookDirections[i];
			while (Position.isValid(rookPos)) {
				if (this.get(rookPos) != ChessPiece.NULL_PIECE) {
					if (this.get(rookPos) == attackingRook) {
						return true;
					} else {
						break;
					}
				}
				rookPos += MoveGeneration.rookDirections[i];
			}
		}

		for (int i = 0; i < MoveGeneration.bishopDirections.length; i++) {
			int bishopPos = position + MoveGeneration.bishopDirections[i];
			while (Position.isValid(bishopPos)) {
				if (this.get(bishopPos) != ChessPiece.NULL_PIECE) {
					if (this.get(bishopPos) == attackingBishop) {
						return true;
					} else {
						break;
					}
				}
				bishopPos += MoveGeneration.bishopDirections[i];
			}
		}

		for (int i = 0; i < MoveGeneration.queenDirections.length; i++) {
			int queenPos = position + MoveGeneration.queenDirections[i];
			while (Position.isValid(queenPos)) {
				if (this.get(queenPos) != ChessPiece.NULL_PIECE) {
					if (this.get(queenPos) == attackingQueen) {
						return true;
					} else {
						break;
					}
				}
				queenPos += MoveGeneration.queenDirections[i];
			}
		}

		for (int i = 0; i < MoveGeneration.kingOffsets.length; i++) {
			int kingPos = position + MoveGeneration.kingOffsets[i];
			if (Position.isValid(kingPos) && this.get(kingPos) == attackingKing) {
				return true;
			}
		}

		return false;
	}

	public void move(int move) {
		assert Move.isValid(move);

		int startPos = Move.getStartPosition(move);
		int endPos = Move.getEndPosition(move);
		int startPiece = Move.getStartPiece(move);
		int endPiece = Move.getEndPiece(move);
		int flags = Move.getFlags(move);
		int promotionType = Move.getPromotionPieceType(move);

		move(startPos, endPos, startPiece, ChessPiece.getColor(startPiece), endPiece, flags,
				promotionType);
	}

	public void move(Move move) {
		int startPos = move.getStartPosition();
		int endPos = move.getEndPosition();
		int startPiece = move.getStartPiece();
		int endPiece = move.getEndPiece();
		int flags = move.getFlags();
		int promotionType = move.getPromotionPieceType();

		move(startPos, endPos, startPiece, ChessPiece.getColor(startPiece), endPiece, flags,
				promotionType);
	}

	private void move(int startPos, int endPos, int startPiece, int startColor, int endPiece,
			int flags, int promotionType) {
		savedStates[stateIndex++] =
				new State(this.castlingPermissions, this.enPassantPosition, this.halfMoveClock);

		// Check for capture, and remove from the board
		// Covers en passant captures
		if (endPiece != ChessPiece.NULL_PIECE) {
			int effectiveCapturePos = endPos;
			if (flags == Move.Flags.EN_PASSANT.value()) {
				effectiveCapturePos +=
						(startColor == ChessColor.WHITE.value() ? Position.S : Position.N);
			}
			clear(effectiveCapturePos);
			updateCastlingPerm(effectiveCapturePos);
		}

		// Move start piece to new position
		// Covers promotion and
		// Quiet moves
		clear(startPos);
		if (flags == Move.Flags.PROMOTION.value()) {
			set(endPos, ChessPiece.fromRaw(startColor, promotionType));
		} else {
			set(endPos, startPiece);
		}

		// implements castling moves
		if (flags == Move.Flags.CASTLE.value()) {
			int castlingRookStart = Position.NULL_POSITION;
			int castlingRookEnd = Position.NULL_POSITION;

			if (endPos == Position.from(File.F_G, Rank.R_1)) {
				castlingRookStart = Position.from(File.F_H, Rank.R_1);
				castlingRookEnd = Position.from(File.F_F, Rank.R_1);

			} else if (endPos == Position.from(File.F_C, Rank.R_1)) {
				castlingRookStart = Position.from(File.F_A, Rank.R_1);
				castlingRookEnd = Position.from(File.F_D, Rank.R_1);

			} else if (endPos == Position.from(File.F_G, Rank.R_8)) {
				castlingRookStart = Position.from(File.F_H, Rank.R_8);
				castlingRookEnd = Position.from(File.F_F, Rank.R_8);

			} else if (endPos == Position.from(File.F_G, Rank.R_8)) {
				castlingRookStart = Position.from(File.F_A, Rank.R_8);
				castlingRookEnd = Position.from(File.F_D, Rank.R_8);
			} else {
				assert false;
			}

			set(castlingRookEnd, clear(castlingRookStart));
		}

		updateCastlingPerm(startPos);

		// check for pawn double and update passant
		if (flags == Move.Flags.DOUBLE_PAWN_PUSH.value()) {
			this.enPassantPosition =
					endPos + (startColor == ChessColor.WHITE.value() ? Position.S : Position.N);
		} else {
			this.enPassantPosition = Position.NULL_POSITION;
		}

		// flip color and update half move clock
		this.activeColor = ChessColor.opposite(activeColor);
		if (endPiece != ChessPiece.NULL_PIECE
				|| ChessPiece.getPieceType(startPiece) == PieceType.PAWN.value()) {
			this.halfMoveClock = 0;
		} else {
			this.halfMoveClock++;
		}
	}

	public void unmove(int move) {
		assert Move.isValid(move);

		int startPos = Move.getStartPosition(move);
		int endPos = Move.getEndPosition(move);
		int startPiece = Move.getStartPiece(move);
		int endPiece = Move.getEndPiece(move);
		int moveType = Move.getFlags(move);
		int promotionPieceType = Move.getPromotionPieceType(move);

		unmove(startPos, endPos, startPiece, ChessPiece.getColor(startPiece), endPiece, moveType,
				promotionPieceType);
	}

	public void unmove(Move move) {
		int startPos = move.getStartPosition();
		int endPos = move.getEndPosition();
		int startPiece = move.getStartPiece();
		int endPiece = move.getEndPiece();
		int moveType = move.getFlags();
		int promotionPieceType = move.getPromotionPieceType();

		unmove(startPos, endPos, startPiece, ChessPiece.getColor(startPiece), endPiece, moveType,
				promotionPieceType);
	}

	private void unmove(int startPos, int endPos, int startPiece, int startColor, int endPiece,
			int moveType, int promotionType) {
		this.activeColor = ChessColor.opposite(activeColor);

		if (moveType == Move.Flags.CASTLE.value()) {
			int castlingRookStart = Position.NULL_POSITION;
			int castlingRookEnd = Position.NULL_POSITION;

			if (endPos == Position.from(File.F_G, Rank.R_1)) {
				castlingRookStart = Position.from(File.F_H, Rank.R_1);
				castlingRookEnd = Position.from(File.F_F, Rank.R_1);

			} else if (endPos == Position.from(File.F_C, Rank.R_1)) {
				castlingRookStart = Position.from(File.F_A, Rank.R_1);
				castlingRookEnd = Position.from(File.F_D, Rank.R_1);

			} else if (endPos == Position.from(File.F_G, Rank.R_8)) {
				castlingRookStart = Position.from(File.F_H, Rank.R_8);
				castlingRookEnd = Position.from(File.F_F, Rank.R_8);

			} else if (endPos == Position.from(File.F_G, Rank.R_8)) {
				castlingRookStart = Position.from(File.F_A, Rank.R_8);
				castlingRookEnd = Position.from(File.F_D, Rank.R_8);
			} else {
				assert false;
			}

			set(castlingRookStart, clear(castlingRookEnd));
		}

		clear(endPos);
		set(startPos, startPiece);

		if (endPiece != ChessPiece.NULL_PIECE) {
			int effectiveCapturePos = endPos;
			if (moveType == Move.Flags.EN_PASSANT.value()) {
				effectiveCapturePos +=
						(startColor == ChessColor.WHITE.value() ? Position.S : Position.N);
			}
			set(effectiveCapturePos, endPiece);
		}

		State saved = savedStates[--stateIndex];
		this.castlingPermissions = saved.castlingPermissions;
		this.enPassantPosition = saved.enPassantPosition;
		this.halfMoveClock = saved.halfMoveClock;
	}

	private void updateCastlingPerm(int position) {
		int updatedPermissions = this.castlingPermissions;

		if (position == Position.from(File.F_A, Rank.R_1)) {
			updatedPermissions &= ~CastlingBitFlags.WHITE_QUEENSIDE.value();
		} else if (position == Position.from(File.F_A, Rank.R_8)) {
			updatedPermissions &= ~CastlingBitFlags.BLACK_QUEENSIDE.value();
		} else if (position == Position.from(File.F_H, Rank.R_1)) {
			updatedPermissions &= ~CastlingBitFlags.WHITE_KINGSIDE.value();
		} else if (position == Position.from(File.F_H, Rank.R_8)) {
			updatedPermissions &= ~CastlingBitFlags.BLACK_KINGSIDE.value();
		} else if (position == Position.from(File.F_E, Rank.R_1)) {
			updatedPermissions &= ~(CastlingBitFlags.WHITE_QUEENSIDE.value()
					| CastlingBitFlags.WHITE_KINGSIDE.value());
		} else if (position == Position.from(File.F_E, Rank.R_8)) {
			updatedPermissions &= ~(CastlingBitFlags.BLACK_QUEENSIDE.value()
					| CastlingBitFlags.BLACK_KINGSIDE.value());
		} else {
			return;
		}

		this.castlingPermissions = updatedPermissions;
	}

	public static class ChessBoardFactory {

		public static ChessBoard startingBoard() {
			ChessBoard board = new ChessBoard();

			board.castlingPermissions =
					CastlingBitFlags.value(EnumSet.allOf(CastlingBitFlags.class));

			for (File f : Position.File.values()) {
				board.set(Position.from(f, Rank.R_2), ChessPiece.WHITE_PAWN.value());
				board.set(Position.from(f, Rank.R_7), ChessPiece.BLACK_PAWN.value());
			}

			board.set(Position.from(File.F_A, Rank.R_1), ChessPiece.WHITE_ROOK.value());
			board.set(Position.from(File.F_H, Rank.R_1), ChessPiece.WHITE_ROOK.value());
			board.set(Position.from(File.F_A, Rank.R_8), ChessPiece.BLACK_ROOK.value());
			board.set(Position.from(File.F_H, Rank.R_8), ChessPiece.BLACK_ROOK.value());

			board.set(Position.from(File.F_B, Rank.R_1), ChessPiece.WHITE_KNIGHT.value());
			board.set(Position.from(File.F_G, Rank.R_1), ChessPiece.WHITE_KNIGHT.value());
			board.set(Position.from(File.F_B, Rank.R_8), ChessPiece.BLACK_KNIGHT.value());
			board.set(Position.from(File.F_G, Rank.R_8), ChessPiece.BLACK_KNIGHT.value());

			board.set(Position.from(File.F_C, Rank.R_1), ChessPiece.WHITE_BISHOP.value());
			board.set(Position.from(File.F_F, Rank.R_1), ChessPiece.WHITE_BISHOP.value());
			board.set(Position.from(File.F_C, Rank.R_8), ChessPiece.BLACK_BISHOP.value());
			board.set(Position.from(File.F_F, Rank.R_8), ChessPiece.BLACK_BISHOP.value());

			board.set(Position.from(File.F_D, Rank.R_1), ChessPiece.WHITE_QUEEN.value());
			board.set(Position.from(File.F_D, Rank.R_8), ChessPiece.BLACK_QUEEN.value());

			board.set(Position.from(File.F_E, Rank.R_1), ChessPiece.WHITE_KING.value());
			board.set(Position.from(File.F_E, Rank.R_8), ChessPiece.BLACK_KING.value());

			return board;
		}

		// TODO (Optional) Implement chessboard from fen string
		public static ChessBoard fromFEN(String fen) {
			throw new UnsupportedOperationException();
		}

	}
}
