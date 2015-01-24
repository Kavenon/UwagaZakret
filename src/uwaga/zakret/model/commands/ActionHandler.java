
//:uwaga.zakret.model.commands.ActionHandler.java
package uwaga.zakret.model.commands;

import uwaga.zakret.controller.MarkerController;
import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Board;
import uwaga.zakret.model.Connection;

/**
 * Abstract class for actionChain link
 */
public abstract class ActionHandler {

	/** The board. */
	protected Board board;
	
	/** The marker controller. */
	protected MarkerController markerController;
	
	/** The player controller. */
	protected PlayerController playerController;
	
	/** The connection. */
	protected Connection connection;

	/** The handled command. */
	protected String handledCommand;

	/** The next. */
	protected ActionHandler next;

	/**
	 * Execute.
	 *
	 * @param msg the msg
	 * @return true, if successful
	 */
	public boolean execute(String msg) {
		if (msg.startsWith(handledCommand)) {
			action(msg);
			return true;
		}
		return false;
	}

	/**
	 * Action.
	 *
	 * @param msg the msg
	 */
	abstract protected void action(String msg);

	/**
	 * Sets the board.
	 *
	 * @param board the new board
	 */
	public void setBoard(Board board) {
		this.board = board;
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
