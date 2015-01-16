package uwaga.zakret.model.commands.client;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.commands.ActionHandler;

public class ColAction extends ActionHandler {
	public ColAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {

		if (!board.isPlaying())
			return;

		String[] splfirst = msg.split("#");
		for (PlayerController plc : board.getPlayers()) {
			if (plc.getPlayer().getUsername().equals(splfirst[1])) {
				plc.getPlayer().setAlive(false);
			}
		}

		board.decRemainingPlayers();

		if (board.getRemainingPlayers() <= 1) {

			for (PlayerController plc : board.getPlayers()) {
				if (plc.getPlayer().isAlive())
					plc.getPlayer().setPoints(plc.getPlayer().getPoints() + 1);
			}

			board.createMap();

			connection.send("RESET");

		}
	}
}
