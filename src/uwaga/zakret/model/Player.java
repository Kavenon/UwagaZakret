//:uwaga.zakret.model.Plyaer.java
package uwaga.zakret.model;

import uwaga.zakret.controller.MarkerController;

/**
 * Player model
 */
public class Player {

	/** The marker. */
	private MarkerController marker;

	/** The username. */
	private String username;

	/** The points. */
	private int points;

	/** The control. */
	private Control control;

	/** The ready to play. */
	private boolean ready;

	/** The alive. */
	private boolean alive;

	/**
	 * Checks if is alive.
	 *
	 * @return true, if is alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * Sets the alive.
	 *
	 * @param alive the new alive
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * Checks if is ready.
	 *
	 * @return true, if is ready
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * Sets the ready.
	 *
	 * @param ready the new ready
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}

	/**
	 * Instantiates a new player.
	 *
	 * @param username the username
	 */
	public Player(String username) {
		this.username = username;
		this.alive = true;
	}

	/**
	 * Instantiates a new player.
	 *
	 * @param marker the marker
	 * @param username the username
	 * @param points the points
	 * @param control the control
	 * @param ready the ready
	 */
	public Player(MarkerController marker, String username, int points,
			Control control, boolean ready) {

		this.marker = marker;
		this.username = username;
		this.points = points;
		this.control = control;
		this.ready = ready;
		this.alive = true;
	}

	/**
	 * Gets the control.
	 *
	 * @return the control
	 */
	public Control getControl() {
		return control;
	}

	/**
	 * Sets the control.
	 *
	 * @param control the new control
	 */
	public void setControl(Control control) {
		this.control = control;
	}

	/**
	 * Gets the marker controller.
	 *
	 * @return the marker controller
	 */
	public MarkerController getMarkerController() {
		return marker;
	}

	/**
	 * Sets the marker controller.
	 *
	 * @param marker the new marker controller
	 */
	public void setMarkerController(MarkerController marker) {
		this.marker = marker;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the points.
	 *
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Sets the points.
	 *
	 * @param points the new points
	 */
	public void setPoints(int points) {
		this.points = points;
	}

}///!~
