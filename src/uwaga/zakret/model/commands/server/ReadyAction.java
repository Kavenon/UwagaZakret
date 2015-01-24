//:uwaga.zakret.model.commands.server.ReadyAction.java
package uwaga.zakret.model.commands.server;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.commands.ActionHandler;
import uwaga.zakret.server.ServerInstance;

/**
 * Server Action that handles READY# command
 */
public class ReadyAction extends ActionHandler {
	
	/**
	 * Instantiates a new ready action.
	 *
	 * @param msg the msg
	 */
	public ReadyAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {

		// set ready
		playerController.getPlayer().setReady(true);

		// broad cast ADDPLAYER to others
		for (PlayerController playerController : board.getPlayers()) {

			Player player = playerController.getPlayer();

			String data = "";

			data = "ADDPLAYER#"
					+ player.getUsername()
					+ "#"
					+ player.getMarkerController().getMarker()
							.getCurrentPosition().getX()
					+ "#"
					+ player.getMarkerController().getMarker()
							.getCurrentPosition().getY()
					+ "#"
					+ player.getMarkerController().getMarker().getDirection()
					+ "#"
					+ player.getMarkerController().getMarker().getColor()
							.getRGB() + "#" + player.getPoints();

			ServerInstance.broadcastMessage(data);
		}
	}
}///!~
