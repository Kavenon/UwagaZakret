package uwaga.zakret.model.commands.client;

import uwaga.zakret.model.commands.ActionHandler;

public class InitboardAction extends ActionHandler {
	public InitboardAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {
		String[] split = msg.split("#");

		board.setX(Integer.parseInt(split[1]));
		board.setY(Integer.parseInt(split[2]));
		board.setWidth(Integer.parseInt(split[3]));
		board.setHeight(Integer.parseInt(split[4]));
		board.setPlaying(false);
		board.createMap();

		board.setAdmin(split[5]);
	}
}
