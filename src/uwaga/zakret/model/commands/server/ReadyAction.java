package uwaga.zakret.model.commands.server;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.commands.ActionHandler;
import uwaga.zakret.server.ServerInstance;

public class ReadyAction extends ActionHandler {
	public ReadyAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {

		playerController.getPlayer().setReady(true);

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
}
