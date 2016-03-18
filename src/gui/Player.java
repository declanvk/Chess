package gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayDeque;

import core.Move;

/**
 * Represent a player in the chess game. Responsible for handling getting moves
 * for the GameThread
 * 
 * @author declan
 *
 * @param <I>
 *            player input
 */
public abstract class Player<I> {

	private final PropertyChangeSupport mPcs = new PropertyChangeSupport(this);
	private final ArrayDeque<Move> movesMade = new ArrayDeque<Move>();

	protected final String name;
	protected final int color; // 0 -> white, 1 -> black

	protected I input;

	/**
	 * Constructs a new Player
	 * 
	 * @param name
	 *            the name of the Player
	 * @param color
	 *            the color of the Player
	 */
	public Player(String name, int color) {
		this.name = name;
		this.color = color;
	}

	/**
	 * Returns the name of the Player
	 * 
	 * @return the name of the Player
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the color of the Player
	 * 
	 * @return the color of the Player
	 */
	public int getColor() {
		return color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Attach a listener that fires when a player submits a Move
	 * 
	 * @param listener
	 *            a listener that is called when a new move is submitted
	 */
	public void addMoveListener(PropertyChangeListener listener) {
		mPcs.addPropertyChangeListener(listener);
	}

	/**
	 * Remove a listener from the Player
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeMoveListener(PropertyChangeListener listener) {
		mPcs.removePropertyChangeListener(listener);
	}

	/**
	 * Start the Player's turn with the provided input. The input is only valid
	 * for the duration of the turn
	 * 
	 * @param input
	 *            the input that the player requires
	 */
	public void startTurn(I input) {
		this.input = input;
		this.startTurnProtected();
	}

	/**
	 * End the Player's turn. After calling this, the input passed in startTurn
	 * is no longer valid.
	 */
	public void endTurn() {
		this.endTurnProtected();
		this.input = null;
	}

	/**
	 * Called internally by any Player subclass to indicate that it has a move
	 * 
	 * @param m
	 *            the move to submit
	 */
	protected void submitMove(Move m) {
		mPcs.firePropertyChange("move", movesMade.peekLast(), m);
		this.endTurn();
		movesMade.add(m);
	}

	/**
	 * Update the internal state of the Player with the latest Move. Internal
	 * state is not a requirement for a Player
	 * 
	 * @param m
	 *            the move to update the internal representation with
	 */
	public abstract void updateWith(Move m);

	/**
	 * Called upon the start of the Player's turn
	 */
	protected abstract void startTurnProtected();

	/**
	 * Called upon the end of the Player's turn
	 */
	protected abstract void endTurnProtected();
}
