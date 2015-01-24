//:uwaga.zakret.model.commands.client.ResponseOthersResetAction.java
package uwaga.zakret.model.commands.client;

import uwaga.zakret.controller.MarkerController;
import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.commands.ActionHandler;

/**
 * Action that handles RESPONSE_OTHERS_RESET#name,x,y,direction#name2,... command
 */
public class ResponseOthersResetAction extends ActionHandler {
	
	/**
	 * Instantiates a new response others reset action.
	 *
	 * @param msg the msg
	 */
	public ResponseOthersResetAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {

		String[] splfirst = msg.split("#");

		// for every player
		for (int i = 0; i < splfirst.length; i++) {

			String[] splsec = splfirst[i].split(",");
			
			// for every data received, if not current player
			if (!splsec[0].equals(playerController.getPlayer().getUsername())) {
				for (PlayerController plc : board.getPlayers()) {
					if (plc.getPlayer().getUsername().equals(splsec[0])) {
						
						plc.getPlayer().setAlive(true);

						// edit marker 
						MarkerController m = plc.getPlayer()
								.getMarkerController();

						m.getMarker().setWriting(true);
						m.getMarker().setPreviousPosition(null);
						m.getMarker().setCurrentPosition(
								new Position(Double.parseDouble(splsec[1]),
										Double.parseDouble(splsec[2])));
						m.getMarker().setDirection(
								Double.parseDouble(splsec[3]));
						break;

					}
				}
			}
		}
	}
}///!~
