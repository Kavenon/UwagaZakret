//:uwaga.zakret.model.commands.client.ColAction.java
package uwaga.zakret.model.commands.client;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.commands.ActionHandler;

/**
 * Action that handles COL#username action
 */
public class ColAction extends ActionHandler {
	
	/**
	 * Instantiates a new col action.
	 *
	 * @param msg the msg
	 */
	public ColAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {

		if (!board.isPlaying())
			return;

		String[] splfirst = msg.split("#");
		for (PlayerController plc : board.getPlayers()) {
			if (plc.getPlayer().getUsername().equals(splfirst[1])) {
				plc.getPlayer().setAlive(false);
			}
		}

		board.decRemainingPlayers();

		// add points
		if (board.getRemainingPlayers() <= 1) {

			for (PlayerController plc : board.getPlayers()) {
				if (plc.getPlayer().isAlive())
					plc.getPlayer().setPoints(plc.getPlayer().getPoints() + 1);
			}

			board.createMap();

			// send reset command
			connection.send("RESET");

		}
	}
}///!~
