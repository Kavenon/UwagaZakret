//:uwaga.zakret.model.commands.client.MyresetAction.java
package uwaga.zakret.model.commands.client;

import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.commands.ActionHandler;

/**
 * Action that handles MYRESET#x#y#direction command
 */
public class MyresetAction extends ActionHandler {
	
	/**
	 * Instantiates a new myreset action.
	 *
	 * @param msg the msg
	 */
	public MyresetAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {

		String[] spl = msg.split("#");

		Marker marker = playerController.getPlayer().getMarkerController()
				.getMarker();

		playerController.getPlayer().setAlive(true);

		marker.setPreviousPosition(null);
		marker.setCurrentPosition(new Position(Double.parseDouble(spl[1]),
				Double.parseDouble(spl[2])));
		marker.setDirection(Double.parseDouble(spl[3]));
		marker.setWriting(true);

		marker.setLastTimeToggle(0);

		// send request to server, to receive other player new data
		connection.send("REQUEST_OTHERS_RESET");
	}
}///!~
