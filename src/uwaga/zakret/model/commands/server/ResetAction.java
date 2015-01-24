//:uwaga.zakret.model.commands.server.ResetAction.java
package uwaga.zakret.model.commands.server;

import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.commands.ActionHandler;

/**
 * Server action that handles RESET# command
 */
public class ResetAction extends ActionHandler {
	
	/**
	 * Instantiates a new reset action.
	 *
	 * @param msg the msg
	 */
	public ResetAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {
		
		// generate new position
		int[] position = board.generatePosition();
		int x = position[0];
		int y = position[1];
		int direction = position[2];

		// update marker
		Marker marker = playerController.getPlayer().getMarkerController()
				.getMarker();
		marker.setCurrentPosition(new Position(x, y));
		marker.setDirection(direction);

		// set alive
		playerController.getPlayer().setAlive(true);

		// send generated data to client
		connection.send("MYRESET#" + x + "#" + y + "#" + direction);
	}
}///!~
