//:uwaga.zakret.model.commands.client.WinnerAction.java
package uwaga.zakret.model.commands.client;

import uwaga.zakret.model.commands.ActionHandler;

/**
 * Action that handles WINNER#username command
 */
public class WinnerAction extends ActionHandler {
	
	/**
	 * Instantiates a new winner action.
	 *
	 * @param msg the msg
	 */
	public WinnerAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {
		String[] splfirst = msg.split("#");

		// set game winner
		board.setWinner(splfirst[1]);
	}
}///!~
