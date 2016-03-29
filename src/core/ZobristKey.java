package core;

import java.security.SecureRandom;

/**
 * A wrapper for a long Zobrist hash key, defining operations on the key.
 * Zobrist hashing takes into account the color, piece type, and position of all
 * the chess pieces on the board, the currently active color, the current
 * castling rights for both sides, any enPassant Squares to create a unique key
 * for each position in the game space.
 * 
 * @author declan
 *
 */
public class ZobristKey {

	private static final byte[] seed = { b(0x58), b(0x22), b(0xbc), b(0x5b), b(0x04), b(0x53),
			b(0x88), b(0xa7), b(0xbc), b(0x1f), b(0x2c), b(0xda), b(0xea), b(0x28), b(0x1b),
			b(0x56), b(0xa2), b(0x5a), b(0x56), b(0x8e), b(0x9e), b(0x48), b(0x15), b(0x05),
			b(0x01), b(0xfb), b(0xcf), b(0xab), b(0xa9), b(0xa9), b(0xe6), b(0x02), b(0x07),
			b(0x72), b(0x06), b(0x0b), b(0xb0), b(0xc3), b(0xbe), b(0xbd), b(0x5f), b(0x45),
			b(0x9a), b(0xff), b(0x8e), b(0x1e), b(0x52), b(0xde), b(0x23), b(0x8e), b(0x56),
			b(0xa3), b(0xf7), b(0xc5), b(0xcc), b(0x9c), b(0x4e), b(0xcf), b(0x40), b(0x8d),
			b(0x12), b(0xda), b(0x63), b(0xb5), b(0xd4), b(0x5f), b(0x46), b(0x5f), b(0x74),
			b(0xf1), b(0x43), b(0xaf), b(0xff), b(0x24), b(0x6a), b(0xc4), b(0x50), b(0xd0),
			b(0x69), b(0xf4), b(0x51), b(0xbb), b(0x31), b(0x85), b(0xfb), b(0xb1), b(0x78),
			b(0x3f), b(0xbb), b(0x72), b(0xb4), b(0x41), b(0xf5), b(0x63), b(0x48), b(0x1b),
			b(0x38), b(0x86), b(0x2c), b(0xc4) };

	private static final SecureRandom gen = new SecureRandom(seed);

	private static byte b(int i) {
		return (byte) i;
	}

	private static long next() {
		return gen.nextLong();
	}

	private static final long[][] boardValues =
			new long[ChessPiece.values().length][Position.NUM_TOTAL_VALUES];
	// the extra two are for combinations of rights
	private static final long[] castlingRights = new long[1 << CastlingBitFlags.values().length];
	private static final long[] enPassantSquare = new long[Position.NUM_TOTAL_VALUES];
	private static final long activeColor = next();

	static {
		for (int i = 0; i < boardValues.length; i++) {
			for (int j = 0; j < boardValues[i].length; j++) {
				boardValues[i][j] = next();
			}
		}

		castlingRights[CastlingBitFlags.WHITE_KINGSIDE.value()] = next();
		castlingRights[CastlingBitFlags.WHITE_QUEENSIDE.value()] = next();
		castlingRights[CastlingBitFlags.BLACK_KINGSIDE.value()] = next();
		castlingRights[CastlingBitFlags.BLACK_QUEENSIDE.value()] = next();
		castlingRights[CastlingBitFlags.WHITE_KINGSIDE.value()
				| CastlingBitFlags.WHITE_QUEENSIDE.value()] =
						castlingRights[CastlingBitFlags.WHITE_KINGSIDE.value()]
								^ castlingRights[CastlingBitFlags.WHITE_QUEENSIDE.value()];
		castlingRights[CastlingBitFlags.BLACK_KINGSIDE.value()
				| CastlingBitFlags.BLACK_QUEENSIDE.value()] =
						castlingRights[CastlingBitFlags.BLACK_KINGSIDE.value()]
								^ castlingRights[CastlingBitFlags.BLACK_QUEENSIDE.value()];

		for (int i = 0; i < enPassantSquare.length; i++) {
			enPassantSquare[i] = next();
		}
	}

	private long key;

	/**
	 * Construct a new ZobristKey with a value of zero.
	 */
	public ZobristKey() {
		this.key = 0L;
	}

	/**
	 * Alter the key to reflect a change at the given position with the given
	 * piece
	 * 
	 * @param position
	 *            the position that was altered
	 * @param piece
	 *            the piece at the given position
	 */
	public void toggleBoard(int position, int piece) {
		assert Position.isValid(position);
		assert ChessPiece.isValid(piece);

		key ^= boardValues[piece][position];
	}

	/**
	 * Alter the key to reflect a change in the position's castling rights
	 * 
	 * @param castlingBitFlag
	 *            the castling rights to update
	 */
	public void toggleCastlingRights(int castlingBitFlag) {
		assert CastlingBitFlags.isValid(castlingBitFlag)
				|| castlingBitFlag == (CastlingBitFlags.BLACK_KINGSIDE.value()
						| CastlingBitFlags.BLACK_QUEENSIDE.value())
				|| castlingBitFlag == (CastlingBitFlags.WHITE_KINGSIDE.value()
						| CastlingBitFlags.WHITE_QUEENSIDE.value());

		key ^= castlingRights[castlingBitFlag];
	}

	/**
	 * Alter the key to reflect a change in the en passant position
	 * 
	 * @param position
	 *            the en passant position
	 */
	public void toggleEnPassantSquare(int position) {
		assert Position.isValid(position);

		key ^= enPassantSquare[position];
	}

	/**
	 * Alter the key to reflect a change in the active color
	 */
	public void toggleActiveColor() {
		key ^= activeColor;
	}

	/**
	 * Returns the raw value of the ZobristKey
	 * 
	 * @return the raw value of the ZobristKey
	 */
	public long getKey() {
		return key;
	}

	/**
	 * Set the underlying key value of this ZobristKey
	 * 
	 * @param value
	 *            the value to set the key to
	 */
	public void setKey(long value) {
		this.key = value;
	}

	/**
	 * Constructs a new ZobristKey from the given value
	 * 
	 * @param value
	 *            the value to construct a ZobristKey from
	 * @return a new ZobristKey constructed form the given value
	 */
	public static ZobristKey from(long value) {
		ZobristKey key = new ZobristKey();
		key.key = value;
		return key;
	}

}
