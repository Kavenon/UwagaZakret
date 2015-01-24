//:uwaga.zakret.model.commands.client.OtherpositionAction.java
package uwaga.zakret.model.commands.client;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.commands.ActionHandler;

/**
 * Action that handles OTHERPOSITION#name,x,y,isWriting#name2,.... command
 */
public class OtherpositionAction extends ActionHandler {
	
	/**
	 * Instantiates a new otherposition action.
	 *
	 * @param msg the msg
	 */
	public OtherpositionAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {
		String[] splfirst = msg.split("#");

		// for every player received
		for (int i = 1; i < splfirst.length; i++) {

			String[] splsec = splfirst[i].split(",");

			// for every data received
			for (PlayerController plc : board.getPlayers()) {
				if (plc.getPlayer().getUsername().equals(splsec[0])) {

					// set previous position
					Position previousPosition = plc.getPlayer()
							.getMarkerController().getMarker()
							.getCurrentPosition();
					plc.getPlayer().getMarkerController().getMarker()
							.setPreviousPosition(previousPosition);

					// set current position
					Position newPosition = new Position(
							Double.parseDouble(splsec[1]),
							Double.parseDouble(splsec[2]));
					plc.getPlayer().getMarkerController().getMarker()
							.setCurrentPosition(newPosition);

					// set if is writing or not
					plc.getPlayer().getMarkerController().getMarker()
							.setWriting(Boolean.parseBoolean(splsec[3]));

					break;
				}
			}
		}
	}
}///!~
