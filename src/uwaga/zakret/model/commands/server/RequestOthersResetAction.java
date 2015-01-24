//:uwaga.zakret.model.commands.server.RequestOthersResetAction.java
package uwaga.zakret.model.commands.server;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.commands.ActionHandler;

/**
 * Server action that handles REQUEST_OTHERS_RESET# command
 */
public class RequestOthersResetAction extends ActionHandler {
	
	/**
	 * Instantiates a new request others reset action.
	 *
	 * @param msg the msg
	 */
	public RequestOthersResetAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {
		String data = "RESPONSE_OTHERS_RESET#";

		// prepare data to send
		for (PlayerController playerControllerOth : board.getPlayers()) {

			Player player = playerControllerOth.getPlayer();
			Position m = player.getMarkerController().getMarker()
					.getCurrentPosition();

			data += player.getUsername() + "," + m.getX() + "," + m.getY()
					+ ","
					+ player.getMarkerController().getMarker().getDirection()
					+ "#";

		}
		// send players data to client
		connection.send(data);
	}
}///!~
