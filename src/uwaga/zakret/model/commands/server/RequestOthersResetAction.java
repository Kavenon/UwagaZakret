package uwaga.zakret.model.commands.server;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.commands.ActionHandler;

public class RequestOthersResetAction extends ActionHandler {
	public RequestOthersResetAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {
		String data = "RESPONSE_OTHERS_RESET#";

		for (PlayerController playerControllerOth : board.getPlayers()) {

			Player player = playerControllerOth.getPlayer();
			Position m = player.getMarkerController().getMarker()
					.getCurrentPosition();

			data += player.getUsername() + "," + m.getX() + "," + m.getY()
					+ ","
					+ player.getMarkerController().getMarker().getDirection()
					+ "#";

		}
		connection.send(data);
	}
}
