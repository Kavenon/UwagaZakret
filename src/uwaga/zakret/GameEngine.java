//:uwaga.zakret.GameEngine.java
package uwaga.zakret;

import java.util.ArrayList;

import uwaga.zakret.controller.ConnectFormController;
import uwaga.zakret.controller.Controller;
import uwaga.zakret.controller.PlayController;
import uwaga.zakret.controller.WelcomeController;
import uwaga.zakret.model.Connection;

/**
 * Main Game Engine
 */
public class GameEngine {

	/** Possible game states */
	private ArrayList<Controller> controllers;
	
	/** The current state. */
	private int currentState;

	/** Connection with server */
	private Connection conn;

	/** The Constant PLAY. */
	public static final int PLAY = 0;
	
	/** The Constant WELCOME. */
	public static final int WELCOME = 1;
	
	/** The Constant CONNECT_FORM. */
	public static final int CONNECT_FORM = 2;

	/**
	 * Instantiates a new game engine.
	 */
	public GameEngine() {
		controllers = new ArrayList<Controller>();
		controllers.add(new PlayController(this));
		controllers.add(new WelcomeController(this));
		controllers.add(new ConnectFormController(this));

		conn = new Connection();

		setState(WELCOME);
	}

	/**
	 * Gets the conn.
	 *
	 * @return connection
	 */
	public Connection getConn() {
		return conn;
	}

	/**
	 * Sets the state.
	 *
	 * @param state the state
	 * @return the controller
	 */
	public Controller setState(int state) {

		currentState = state;
		controllers.get(currentState).init();
		return controllers.get(currentState);

	}

	/**
	 * Sets the error.
	 *
	 * @param msg the new error
	 */
	public void setError(String msg) {
		controllers.get(currentState).setError(msg);
	}

	/**
	 * Update game
	 */
	public void update() {

		controllers.get(currentState).update();

	}

	/**
	 * Draw.
	 *
	 * @param g graphics
	 */
	public void draw(java.awt.Graphics2D g) {

		controllers.get(currentState).draw(g);

	}

	/**
	 * Handles keyTyped
	 *
	 * @param k keyTyped
	 */
	public void keyTyped(char k) {

		controllers.get(currentState).keyTyped(k);

	}

	/**
	 * Handles keyPressed.
	 *
	 * @param k keyPressed
	 */
	public void keyPressed(int k) {

		controllers.get(currentState).keyPressed(k);

	}

	/**
	 * Handles keyReleased.
	 *
	 * @param k keyReleased
	 */
	public void keyReleased(int k) {

		controllers.get(currentState).keyReleased(k);

	}

}///!~