package uwaga.zakret.model.commands.client;

import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.commands.ActionHandler;

public class MyresetAction extends ActionHandler {
	public MyresetAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {

		String[] spl = msg.split("#");

		Marker marker = playerController.getPlayer().getMarkerController()
				.getMarker();

		playerController.getPlayer().setAlive(true);

		marker.setPreviousPosition(null);
		marker.setCurrentPosition(new Position(Double.parseDouble(spl[1]),
				Double.parseDouble(spl[2])));
		marker.setDirection(Double.parseDouble(spl[3]));
		marker.setWriting(true);

		marker.setLastTimeToggle(0);

		connection.send("REQUEST_OTHERS_RESET");
	}
}
