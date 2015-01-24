//:uwaga.zakret.controller.Controller.java
package uwaga.zakret.controller;

import uwaga.zakret.GameEngine;

/**
 * Abstract class for controller
 */
public abstract class Controller {

	/** The engine. */
	protected GameEngine engine;

	/** The error. */
	protected String error;

	/**
	 * Inits
	 */
	public abstract void init();

	/**
	 * Update.
	 */
	public abstract void update();

	/**
	 * Draw.
	 *
	 * @param g the g
	 */
	public abstract void draw(java.awt.Graphics2D g);

	/**
	 * Key pressed.
	 *
	 * @param k the k
	 */
	public abstract void keyPressed(int k);

	/**
	 * Key released.
	 *
	 * @param k the k
	 */
	public abstract void keyReleased(int k);

	/**
	 * Key typed.
	 *
	 * @param k the k
	 */
	public abstract void keyTyped(char k);

	/**
	 * Sets the error.
	 *
	 * @param msg the new error
	 */
	public abstract void setError(String msg);

}///!~