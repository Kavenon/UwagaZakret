//:uwaga.zakret.model.commands.server.StartAction.java
package uwaga.zakret.model.commands.server;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.commands.ActionHandler;
import uwaga.zakret.server.ServerInstance;

/**
 * Server action that handles START# command 
 */
public class StartAction extends ActionHandler {
	
	/**
	 * Instantiates a new start action.
	 *
	 * @param msg the msg
	 */
	public StartAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {

		// do only if admin requested
		if (playerController.getPlayer().getUsername().equals(board.getAdmin())
				&& !board.isPlaying() && board.getPlayers().size() > 1) {

			// check if all players are ready
			boolean allPlayersReady = true;
			int i = 0;
			for (PlayerController playerController : board.getPlayers()) {
				Player player = playerController.getPlayer();
				if (!player.isReady() || !player.isAlive()) {
					allPlayersReady = false;
					break;
				}
				i++;
			}

			if (allPlayersReady) {
				// boradcast gamestarted
				ServerInstance.broadcastMessage("GAMESTARTED");

				// start game
				board.setRemainingPlayers(i);
				board.setPlaying(true);				

			}
		}
	}
}///!~
