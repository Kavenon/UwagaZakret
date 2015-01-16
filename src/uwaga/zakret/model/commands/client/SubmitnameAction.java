package uwaga.zakret.model.commands.client;

import java.util.Random;

import uwaga.zakret.model.Player;
import uwaga.zakret.model.commands.ActionHandler;

public class SubmitnameAction extends ActionHandler {

	public SubmitnameAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {
		Random generator = new Random();
		String name = "Player" + generator.nextInt(50);
		playerController.setPlayer(new Player(name));

		connection.send("REGISTER#" + name);
	}
}
