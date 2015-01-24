//:uwaga.zakret.model.commands.server.PositionAciton.java

package uwaga.zakret.model.commands.server;

import uwaga.zakret.model.Player;
import uwaga.zakret.model.commands.ActionHandler;

/**
 * Action that handles POSITION#turn command
 */
public class PositionAction extends ActionHandler {
	
	/**
	 * Instantiates a new position action.
	 *
	 * @param msg the msg
	 */
	public PositionAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {

		String[] split = msg.split("#");

		// start or stop player turning
		Double turn = Double.parseDouble(split[1]);
		Player pl = playerController.getPlayer();

		if (turn != 0) {
			pl.getMarkerController().startTurn(turn);
		} else {
			pl.getMarkerController().stopTurn();
		}

	}
}///!~
