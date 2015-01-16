package uwaga.zakret.model.commands.client;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.commands.ActionHandler;

public class OtherpositionAction extends ActionHandler {
	public OtherpositionAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {
		String[] splfirst = msg.split("#");

		for (int i = 1; i < splfirst.length; i++) {

			String[] splsec = splfirst[i].split(",");

			for (PlayerController plc : board.getPlayers()) {
				if (plc.getPlayer().getUsername().equals(splsec[0])) {

					Position previousPosition = plc.getPlayer()
							.getMarkerController().getMarker()
							.getCurrentPosition();
					plc.getPlayer().getMarkerController().getMarker()
							.setPreviousPosition(previousPosition);

					Position newPosition = new Position(
							Double.parseDouble(splsec[1]),
							Double.parseDouble(splsec[2]));
					plc.getPlayer().getMarkerController().getMarker()
							.setCurrentPosition(newPosition);

					plc.getPlayer().getMarkerController().getMarker()
							.setWriting(Boolean.parseBoolean(splsec[3]));

					break;
				}
			}
		}
	}
}
