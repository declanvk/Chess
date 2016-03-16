package gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayDeque;

import core.Move;

public abstract class Player<I> {

	private final PropertyChangeSupport			mPcs		= new PropertyChangeSupport(this);
	private final ArrayDeque<Move>				movesMade	= new ArrayDeque<Move>();

	protected final String						name;
	protected final int							color; //0 -> white, 1 -> black

	protected I									input;

	public Player(String name, int color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public int getColor() {
		return color;
	}

	public void addMoveListener(PropertyChangeListener listener) {
		System.err.println("Player: Adding listener to " + this.name);
		mPcs.addPropertyChangeListener(listener);
	}

	public void removeMoveListener(PropertyChangeListener listener) {
		System.err.println("Player: Removing listener from " + this.name);
		mPcs.removePropertyChangeListener(listener);
	}

	public void startTurn(I input) {
		System.err.println("Player: Starting " + this.name + "'s turn");
		this.input = input;
		this.startTurnProtected();
	}

	public void endTurn() {
		this.endTurnProtected();
		this.input = null;
		System.err.println("Player: Ending " + this.name + "'s turn");
	}

	protected void submitMove(Move m) {
		System.err.println("Player: Submitting " + this.name + "'s move");
		mPcs.firePropertyChange("move", movesMade.peekLast(), m);
		this.endTurn();
		movesMade.add(m);
	}

	public abstract void updateWith(Move m);

	protected abstract void startTurnProtected();

	protected abstract void endTurnProtected();
}
