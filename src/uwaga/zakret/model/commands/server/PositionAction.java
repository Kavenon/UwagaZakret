package uwaga.zakret.model.commands.server;

import uwaga.zakret.model.Player;
import uwaga.zakret.model.commands.ActionHandler;

public class PositionAction extends ActionHandler {
	public PositionAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {

		String[] split = msg.split("#");

		Double turn = Double.parseDouble(split[1]);
		Player pl = playerController.getPlayer();

		if (turn != 0) {
			pl.getMarkerController().startTurn(turn);
		} else {
			pl.getMarkerController().stopTurn();
		}

	}
}
