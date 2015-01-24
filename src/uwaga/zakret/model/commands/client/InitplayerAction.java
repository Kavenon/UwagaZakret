//:uwaga.zakret.model.commands.client.InitplayerAction.java
package uwaga.zakret.model.commands.client;

import java.awt.Color;
import java.awt.event.KeyEvent;

import uwaga.zakret.model.Control;
import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.Settings;
import uwaga.zakret.model.commands.ActionHandler;

/**
 * Action that handles INITPLAYER#x#y#direction#color command
 */
public class InitplayerAction extends ActionHandler {
	
	/**
	 * Instantiates a new initplayer action.
	 *
	 * @param msg the msg
	 */
	public InitplayerAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {
		String[] split = msg.split("#");

		// create new marker
		Marker initMarker = new Marker();

		initMarker.setCurrentPosition(new Position(
				Double.parseDouble(split[1]), Double.parseDouble(split[2])));
		initMarker.setDirection(Double.parseDouble(split[3]));
		initMarker.setWriting(true);
		initMarker.setColor(new Color(Integer.parseInt(split[4])));
		initMarker.setRadius(Settings.circleRadius);

		markerController.setMarker(initMarker);

		// clear player data
		Player player = playerController.getPlayer();

		player.setMarkerController(markerController);
		player.setPoints(0);
		player.setControl(new Control(KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT));
		player.setReady(false);

		board.incRemainingPlayers();

		playerController.setPlayer(player);
	
		// add player to board
		board.setPlayers(playerController);

		// send ready to server
		connection.send("READY[" + player.getUsername() + "]");
	}
}///!~
