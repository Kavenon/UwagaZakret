//:uwaga.zakret.model.Control.java
package uwaga.zakret.model;

/**
 * Model for player control keys
 */
public class Control {

	/** The left. */
	private int left;

	/** The right. */
	private int right;

	/**
	 * Instantiates a new control.
	 *
	 * @param left the left
	 * @param right the right
	 */
	public Control(int left, int right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Gets the left.
	 *
	 * @return the left
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * Sets the left.
	 *
	 * @param left the new left
	 */
	public void setLeft(int left) {
		this.left = left;
	}

	/**
	 * Gets the right.
	 *
	 * @return the right
	 */
	public int getRight() {
		return right;
	}

	/**
	 * Sets the right.
	 *
	 * @param right the new right
	 */
	public void setRight(int right) {
		this.right = right;
	}

}//!~
