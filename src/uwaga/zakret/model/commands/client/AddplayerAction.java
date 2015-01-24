//:uwaga.zakret.model.commands.client.AddplayerAction.java
package uwaga.zakret.model.commands.client;

import java.awt.Color;
import java.awt.event.KeyEvent;

import uwaga.zakret.controller.MarkerController;
import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Control;
import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.Settings;
import uwaga.zakret.model.commands.ActionHandler;

/**
 * Action that handes ADDPLAYER#username command
 */
public class AddplayerAction extends ActionHandler {
	
	/**
	 * Instantiates a new addplayer action.
	 *
	 * @param msg the msg
	 */
	public AddplayerAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {		

		String[] split = msg.split("#");

		if (split[1].equals(playerController.getPlayer().getUsername()))
			return;

		for (PlayerController pcont : board.getPlayers()) {
			Player px = pcont.getPlayer();
			if (px.getUsername().equals(split[1])) {
				return;
			}
		}

		board.incRemainingPlayers();

		// create new marker for player
		Marker newMarker = new Marker();

		newMarker.setCurrentPosition(new Position(Double.parseDouble(split[2]),
				Double.parseDouble(split[3])));
		newMarker.setDirection(Double.parseDouble(split[4]));
		newMarker.setWriting(true);
		newMarker.setColor(new Color(Integer.parseInt(split[5])));
		newMarker.setRadius(Settings.circleRadius);

		MarkerController newMarkerController = new MarkerController();

		newMarkerController.setMarker(newMarker);

		// create new player
		Player newPlayer = new Player(split[1]);
		newPlayer.setMarkerController(newMarkerController);
		newPlayer.setPoints(Integer.parseInt(split[6]));
		newPlayer.setControl(new Control(KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT));
		newPlayer.setReady(false);

		PlayerController newController = new PlayerController();

		newController.setPlayer(newPlayer);
		
		// add player to board
		board.setPlayers(newController);
	}
}///!~
