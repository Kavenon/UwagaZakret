//:uwaga.zakret.model.commands.client.Submitname.java
package uwaga.zakret.model.commands.client;

import java.util.Random;

import uwaga.zakret.model.Player;
import uwaga.zakret.model.commands.ActionHandler;

/**
 * Action that handles SUBMITNAME command
 */
public class SubmitnameAction extends ActionHandler {

	/**
	 * Instantiates a new submitname action.
	 *
	 * @param msg the msg
	 */
	public SubmitnameAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {
		// generate username
		Random generator = new Random();
		String name = "Player" + generator.nextInt(50);
		
		// create player
		playerController.setPlayer(new Player(name));

		// send username to server
		connection.send("REGISTER#" + name);
	}
}///!~
