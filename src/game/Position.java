package game;

import java.awt.Point;

public class Position {
	
	private final Point position;

	public Position(Point p) {
		if(p.x > 8 || p.x < 1 || p.y > 8 || p.y < 1) {
			throw new IllegalArgumentException("Position out of chess bounds.");
		}
		
		this.position = p;
	}

	public Point getPosition() {
		return position;
	}

	/**
	 * @return
	 * @see java.awt.Point#getX()
	 */
	public double getX() {
		return position.getX();
	}

	/**
	 * @return
	 * @see java.awt.Point#getY()
	 */
	public double getY() {
		return position.getY();
	}
	
	public double distance(Position other) {
		return this.position.distance(other.position);
	}

	/**
	 * @param obj
	 * @return
	 * @see java.awt.Point#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return position.equals(obj);
	}

	/**
	 * @return
	 * @see java.awt.Point#toString()
	 */
	public String toString() {
		return position.toString();
	}

	public Object clone() {
		return new Position((Point) position.clone());
	}

	/**
	 * @return
	 * @see java.awt.geom.Point2D#hashCode()
	 */
	public int hashCode() {
		return position.hashCode();
	}

}
