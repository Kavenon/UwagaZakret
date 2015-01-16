package uwaga.zakret.model.commands.client;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.commands.ActionHandler;

public class DisconnectAction extends ActionHandler {
	public DisconnectAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {
		String[] spl = msg.split("#");
		for (PlayerController pcont : board.getPlayers()) {
			if (pcont.getPlayer().getUsername().equals(spl[1])) {
				board.getPlayers().remove(pcont);
				break;
			}
		}
		board.setAdmin(spl[2]);
	}
}
