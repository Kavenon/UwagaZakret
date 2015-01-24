//:uwaga.zakret.model.Marker.java
package uwaga.zakret.model;

import java.awt.Color;

/**
 * Marker model
 */
public class Marker {

	/** The current position. */
	private Position currentPosition;

	/** The previous position. */
	private Position previousPosition;

	/** The is writing. */
	private boolean isWriting;

	/** The color. */
	private Color color;

	/** The radius. */
	private int radius;

	/** The direction. */
	private double direction;

	/** The last time toggle. */
	private long lastTimeToggle;

	/**
	 * Gets the previous position.
	 *
	 * @return the previous position
	 */
	public Position getPreviousPosition() {
		return previousPosition;
	}

	/**
	 * Sets the previous position.
	 *
	 * @param previousPosition the new previous position
	 */
	public void setPreviousPosition(Position previousPosition) {
		this.previousPosition = previousPosition;
	}

	/**
	 * Instantiates a new marker.
	 */
	public Marker() {
	}

	/**
	 * Gets the current position.
	 *
	 * @return the current position
	 */
	public Position getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * Sets the current position.
	 *
	 * @param currentPosition the new current position
	 */
	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

	/**
	 * Checks if is writing.
	 *
	 * @return true, if is writing
	 */
	public boolean isWriting() {
		return isWriting;
	}

	/**
	 * Sets the writing.
	 *
	 * @param isWriting the new writing
	 */
	public void setWriting(boolean isWriting) {
		this.isWriting = isWriting;
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 *
	 * @param color the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Gets the radius.
	 *
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Sets the radius.
	 *
	 * @param radius the new radius
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	public double getDirection() {
		return direction;
	}

	/**
	 * Sets the direction.
	 *
	 * @param direction the new direction
	 */
	public void setDirection(double direction) {
		this.direction = direction;
	}

	/**
	 * Gets the last time toggle.
	 *
	 * @return the last time toggle
	 */
	public long getLastTimeToggle() {
		return lastTimeToggle;
	}

	/**
	 * Sets the last time toggle.
	 *
	 * @param lastTimeToggle the new last time toggle
	 */
	public void setLastTimeToggle(long lastTimeToggle) {
		this.lastTimeToggle = lastTimeToggle;
	}

}///!~
