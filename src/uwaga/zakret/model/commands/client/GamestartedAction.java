//:uwaga.zakret.model.commands.client.GamestartedAction.java
package uwaga.zakret.model.commands.client;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.commands.ActionHandler;

/**
 * Action that handles GAMESTARTED command
 */
public class GamestartedAction extends ActionHandler {
	
	/**
	 * Instantiates a new gamestarted action.
	 *
	 * @param msg the msg
	 */
	public GamestartedAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {
		// clear board
		board.setClear(false);

		// set playing
		board.setPlaying(true);

		playerController.getPlayer().setAlive(true);

		// set no. of alive players
		board.setRemainingPlayers(board.getPlayers().size());

		// clear points if there previous game finished
		if (board.getWinner() != null) {
			for (PlayerController plc : board.getPlayers()) {
				plc.getPlayer().setPoints(0);
			}
			board.setWinner(null);
		}
	}
}///!~
