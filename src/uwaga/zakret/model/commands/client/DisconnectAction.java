//:uwaga.zakret.model.commands.client.DisconnectAction.java
package uwaga.zakret.model.commands.client;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.commands.ActionHandler;
/**
 * Action that handles DISCONNECT#username#newadmin
 */
public class DisconnectAction extends ActionHandler {
	
	/**
	 * Instantiates a new disconnect action.
	 *
	 * @param msg the msg
	 */
	public DisconnectAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {
		String[] spl = msg.split("#");
		
		// delete player from board
		for (PlayerController pcont : board.getPlayers()) {
			if (pcont.getPlayer().getUsername().equals(spl[1])) {
				board.getPlayers().remove(pcont);
				break;
			}
		}
		
		// set new admin
		board.setAdmin(spl[2]);
	}
}///!~
