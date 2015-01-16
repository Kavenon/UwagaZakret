package uwaga.zakret.model.commands.client;

import uwaga.zakret.model.commands.ActionHandler;

public class WinnerAction extends ActionHandler {
	public WinnerAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {
		String[] splfirst = msg.split("#");

		board.setWinner(splfirst[1]);
	}
}
