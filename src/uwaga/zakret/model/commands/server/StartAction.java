package uwaga.zakret.model.commands.server;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.commands.ActionHandler;
import uwaga.zakret.server.ServerInstance;

public class StartAction extends ActionHandler {
	public StartAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {

		if (playerController.getPlayer().getUsername().equals(board.getAdmin())
				&& !board.isPlaying() && board.getPlayers().size() > 1) {
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

				ServerInstance.broadcastMessage("GAMESTARTED");

				board.setRemainingPlayers(i);
				board.setPlaying(true);
				// logger.info("Gamestarted");

			}
		}
	}
}
