//:uwaga.zakret.model.Position.java
package uwaga.zakret.model;

/**
 * Model position
 */
public class Position {

	/** The x. */
	private double x;
	
	/** The y. */
	private double y;

	/**
	 * Instantiates a new position.
	 *
	 * @param _x the _x
	 * @param _y the _y
	 */
	public Position(double _x, double _y) {
		x = _x;
		y = _y;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(double y) {
		this.y = y;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (!(other instanceof Position)) {
			return false;
		}

		Position that = (Position) other;

		return (int) x == (int) that.x && (int) y == (int) that.y;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + (int) x;
		hash = 71 * hash + (int) y;
		return hash;
	}

}///!~
