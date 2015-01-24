//:uwaga.zakret.controller.MarkerController.java
package uwaga.zakret.controller;

import java.awt.Graphics2D;

import uwaga.zakret.GameEngine;
import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Position;

/**
 * Controls player marker
 */
public class MarkerController extends Controller {

	/** The marker. */
	private Marker marker;

	/** The turn. */
	private double turn = 0;

	/**
	 * Instantiates a new marker controller.
	 */
	public MarkerController() {

	}

	/**
	 * Instantiates a new marker controller.
	 *
	 * @param engine the game engine
	 */
	public MarkerController(GameEngine engine) {
		this.engine = engine;
	}

	/**
	 * Gets the turn.
	 *
	 * @return the turn
	 */
	public double getTurn() {
		return turn;
	}

	/**
	 * Start turn.
	 *
	 * @param direction the direction
	 */
	public void startTurn(double direction) {
		this.turn = direction;
	}

	/**
	 * Stop turn.
	 */
	public void stopTurn() {
		turn = 0;
	}

	/**
	 * Checks if is turning.
	 *
	 * @return true, if is turning
	 */
	public boolean isTurning() {
		return turn != 0;
	}

	/**
	 * Gets the marker.
	 *
	 * @return the marker
	 */
	public Marker getMarker() {
		return marker;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#setError(java.lang.String)
	 */
	public void setError(String msg) {
		error = msg;
	}

	/**
	 * Sets the marker.
	 *
	 * @param marker the new marker
	 */
	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	/**
	 * Move marker to next position
	 *
	 * @return true, if successful
	 */
	public boolean move() {

		marker.setPreviousPosition(marker.getCurrentPosition());

		double x = marker.getCurrentPosition().getX() + 1.2
				* Math.cos(marker.getDirection());

		double y = marker.getCurrentPosition().getY() + 1.2
				* Math.sin(marker.getDirection());

		Position newPosition = new Position(x, y);

		marker.setCurrentPosition(newPosition);

		return true;
	}

	/**
	 * Turn.
	 */
	public void turn() {
		marker.setDirection(marker.getDirection() + turn);
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#init()
	 */
	public void init() {
	};

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#update()
	 */
	public void update() {
	};

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g) {
	};

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#keyPressed(int)
	 */
	public void keyPressed(int k) {
	};

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#keyReleased(int)
	 */
	public void keyReleased(int k) {
	};

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#keyTyped(char)
	 */
	public void keyTyped(char k) {
	};

}
