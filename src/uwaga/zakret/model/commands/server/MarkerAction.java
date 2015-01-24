//:uwaga.zakret.model.commands.server.MarkerAction.java
package uwaga.zakret.model.commands.server;

import uwaga.zakret.model.commands.ActionHandler;

/**
 * Server Action that handles MARKER#UP/DOWN command
 */
public class MarkerAction extends ActionHandler {
	
	/**
	 * Instantiates a new marker action.
	 *
	 * @param msg the msg
	 */
	public MarkerAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {
		String[] spl = msg.split("#");

		// put marker up or down
		if (spl[1].equals("DOWN")) {
			playerController.getPlayer().getMarkerController().getMarker()
					.setWriting(true);
		} else if (spl[1].equals("UP")) {
			playerController.getPlayer().getMarkerController().getMarker()
					.setWriting(false);
		}
	}
}///!~
