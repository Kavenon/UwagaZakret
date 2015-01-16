package uwaga.zakret.model.commands.client;

import java.awt.Color;
import java.awt.event.KeyEvent;

import uwaga.zakret.model.Control;
import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.Settings;
import uwaga.zakret.model.commands.ActionHandler;

public class InitplayerAction extends ActionHandler {
	public InitplayerAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {
		String[] split = msg.split("#");

		Marker initMarker = new Marker();

		initMarker.setCurrentPosition(new Position(
				Double.parseDouble(split[1]), Double.parseDouble(split[2])));
		initMarker.setDirection(Double.parseDouble(split[3]));
		initMarker.setWriting(true);
		initMarker.setColor(new Color(Integer.parseInt(split[4])));
		initMarker.setRadius(Settings.circleRadius);

		markerController.setMarker(initMarker);

		Player player = playerController.getPlayer();

		player.setMarkerController(markerController);
		player.setPoints(0);
		player.setControl(new Control(KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT));
		player.setReady(false);

		board.incRemainingPlayers();

		playerController.setPlayer(player);

		// if (board != null)
		board.setPlayers(playerController);

		connection.send("READY[" + player.getUsername() + "]");
	}
}
