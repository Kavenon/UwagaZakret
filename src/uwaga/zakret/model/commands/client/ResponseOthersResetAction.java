package uwaga.zakret.model.commands.client;

import uwaga.zakret.controller.MarkerController;
import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.commands.ActionHandler;

public class ResponseOthersResetAction extends ActionHandler {
	public ResponseOthersResetAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {

		String[] splfirst = msg.split("#");

		for (int i = 0; i < splfirst.length; i++) {

			String[] splsec = splfirst[i].split(",");
			if (!splsec[0].equals(playerController.getPlayer().getUsername())) {
				for (PlayerController plc : board.getPlayers()) {
					if (plc.getPlayer().getUsername().equals(splsec[0])) {
						plc.getPlayer().setAlive(true);

						MarkerController m = plc.getPlayer()
								.getMarkerController();

						m.getMarker().setWriting(true);
						m.getMarker().setPreviousPosition(null);
						m.getMarker().setCurrentPosition(
								new Position(Double.parseDouble(splsec[1]),
										Double.parseDouble(splsec[2])));
						m.getMarker().setDirection(
								Double.parseDouble(splsec[3]));
						break;

					}
				}
			}

		}
	}
}
