//:uwaga.zakret.model.commands.server.RegisterAction.java

package uwaga.zakret.model.commands.server;

import java.awt.Color;
import java.awt.Event;

import uwaga.zakret.controller.MarkerController;
import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Control;
import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.Settings;
import uwaga.zakret.model.commands.ActionHandler;
import uwaga.zakret.server.ServerInstance;

/**
 * Server Action that handles REGISTER command, sends first command to client
 */
public class RegisterAction extends ActionHandler {
	
	/**
	 * Instantiates a new register action.
	 *
	 * @param msg the msg
	 */
	public RegisterAction(String msg) {
		handledCommand = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.model.commands.ActionHandler#action(java.lang.String)
	 */
	protected void action(String msg) {

		String[] split = msg.split("#");
		String name = split[1];

		synchronized (board.getPlayers()) {
			if (!checkUsername(name)) {
				connection.send("SUBMITNAME");
				return;
			}

			if (board.getAdmin() == null) {
				board.setAdmin(name);
			}

			
			// create new marker 
			int[] position = board.generatePosition();
			int x = position[0];
			int y = position[1];
			int direction = position[2];
			Color color = board.generateColor();

			Marker marker = new Marker();

			marker.setCurrentPosition(new Position(x, y));
			marker.setDirection(direction);
			marker.setWriting(true);
			marker.setColor(color);
			marker.setRadius(Settings.circleRadius);

			MarkerController newMarkerController = new MarkerController();
			newMarkerController.setMarker(marker);

			// create new player
			Player newPlayer = new Player(newMarkerController, name, 0,
					new Control(Event.LEFT, Event.RIGHT), false);

			playerController.setPlayer(newPlayer);

			
			// prepare data to send
			String initBoard = "INITBOARD#" + board.getX() + "#" + board.getY()
					+ "#" + board.getWidth() + "#" + board.getHeight() + "#"
					+ board.getAdmin() + "#";

			// add player to board
			board.setPlayers(playerController);

			// send init board
			connection.send(initBoard);	

			// prepare initplayer data to send
			String initPlayer = "INITPLAYER#" + (int) x + "#" + (int) y + "#"
					+ (int) direction + "#" + color.getRGB() + "#";

			// send initplayer
			connection.send(initPlayer);
		
			// send nameaccepted to client
			connection.send("NAMEACCEPTED");

			// add writer to broadcast messages
			ServerInstance.getWriters().add(connection.getWriter());

		}
	}

	/**
	 * Check if username avaliable.
	 *
	 * @param username the username
	 * @return true, if successful
	 */
	private boolean checkUsername(String username) {
		boolean check = true;
		for (PlayerController playerController : board.getPlayers()) {
			if (playerController.getPlayer().getUsername().equals(username)) {
				check = false;
				break;
			}
		}
		return check;
	}
}///!~
