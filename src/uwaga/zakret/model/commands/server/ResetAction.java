package uwaga.zakret.model.commands.server;

import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.commands.ActionHandler;

public class ResetAction extends ActionHandler {
	public ResetAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {
		int[] position = board.generatePosition();
		int x = position[0];
		int y = position[1];
		int direction = position[2];

		Marker marker = playerController.getPlayer().getMarkerController()
				.getMarker();
		marker.setCurrentPosition(new Position(x, y));
		marker.setDirection(direction);

		playerController.getPlayer().setAlive(true);

		connection.send("MYRESET#" + x + "#" + y + "#" + direction);
	}
}
