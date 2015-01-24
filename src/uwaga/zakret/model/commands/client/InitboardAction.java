//:uwaga.zakret.model.commands.client.InitboardAction.java
package uwaga.zakret.model.commands.client;

import uwaga.zakret.model.commands.ActionHandler;

/**
 * Action that handles INITBOARD#x#y#width#height command
 */
public class InitboardAction extends ActionHandler {
	
	/**
	 * Instantiates a new initboard action.
	 *
	 * @param msg the msg
	 */
	public InitboardAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
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
}///!~
