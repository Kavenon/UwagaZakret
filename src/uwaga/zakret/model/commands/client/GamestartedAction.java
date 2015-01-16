package uwaga.zakret.model.commands.client;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.commands.ActionHandler;

public class GamestartedAction extends ActionHandler {
	public GamestartedAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {
		board.setClear(false);

		board.setPlaying(true);

		playerController.getPlayer().setAlive(true);

		board.setRemainingPlayers(board.getPlayers().size());

		if (board.getWinner() != null) {
			for (PlayerController plc : board.getPlayers()) {
				plc.getPlayer().setPoints(0);
			}
			board.setWinner(null);
		}
	}
}
