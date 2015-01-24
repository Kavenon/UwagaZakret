//:uwaga.zakret.model.commands.ActionChain.java
package uwaga.zakret.model.commands;

import java.util.LinkedList;

import uwaga.zakret.controller.MarkerController;
import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Board;
import uwaga.zakret.model.Connection;

/**
 * Action chain to determine which action use, depends on command received
 */
public class ActionChain {

	/** The board. */
	private Board board;
	
	/** The marker controller. */
	private MarkerController markerController;
	
	/** The player controller. */
	private PlayerController playerController;
	
	/** The connection. */
	private Connection connection;

	/** The handlers. */
	private LinkedList<ActionHandler> handlers;

	/**
	 * Instantiates a new action chain.
	 */
	public ActionChain() {
		handlers = new LinkedList<ActionHandler>();
	}

	/**
	 * Adds the.
	 *
	 * @param handler the handler
	 */
	public void add(ActionHandler handler) {

		handler.setBoard(board);
		handler.setPlayerController(playerController);
		handler.setMarkerController(markerController);
		handler.setConnection(connection);

		handlers.add(handler);

	}

	/**
	 * Start.
	 *
	 * @param msg the msg
	 * @return true, if successful
	 */
	public boolean start(String msg) {
		for (ActionHandler handler : handlers) {
			if (handler.execute(msg)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the board.
	 *
	 * @return the board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Sets the board.
	 *
	 * @param board the new board
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

	/**
	 * Gets the marker controller.
	 *
	 * @return the marker controller
	 */
	public MarkerController getMarkerController() {
		return markerController;
	}

	/**
	 * Sets the marker controller.
	 *
	 * @param markerController the new marker controller
	 */
	public void setMarkerController(MarkerController markerController) {
		this.markerController = markerController;
	}

	/**
	 * Gets the player controller.
	 *
	 * @return the player controller
	 */
	public PlayerController getPlayerController() {
		return playerController;
	}

	/**
	 * Sets the player controller.
	 *
	 * @param playerController the new player controller
	 */
	public void setPlayerController(PlayerController playerController) {
		this.playerController = playerController;
	}

	/**
	 * Sets the connection.
	 *
	 * @param connection the new connection
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}///!~
