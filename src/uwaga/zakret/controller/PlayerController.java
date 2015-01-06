package uwaga.zakret.controller;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import uwaga.zakret.GameEngine;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Settings;
import uwaga.zakret.view.PlayerView;

public class PlayerController extends Controller {

	private Player player;
	
	private PlayerView playerView;

	private boolean pressed;

	public PlayerController(GameEngine engine) {
		this.engine = engine;	
	}

	public void setError(String msg) {
		error = msg;
	}

	public PlayerController() {
		
	}

	public void setPlayer(Player player) {
		this.player = player;
		playerView = new PlayerView(player);
	}

	public Player getPlayer() {
		return player;
	}

	public void init() {

	}

	public void update() {
		MarkerController markCont = player.getMarkerController();
		
		if (markCont.isTurning()) {
			markCont.turn();
		}

		markCont.move();
	}

	public void draw(Graphics2D g) {	
		playerView.draw(g);
	}

	public void keyPressed(int k) {

		if (k == player.getControl().getLeft()) {
			if (!pressed) {
				engine.getConn().send(
						"POSITION#" + -1 * Settings.turnRadius + "#");
				pressed = true;
			}
		} else if (k == player.getControl().getRight()) {
			if (!pressed) {
				engine.getConn().send("POSITION#" + Settings.turnRadius + "#");
				pressed = true;
			}
		} else if (k == KeyEvent.VK_SPACE) {
			engine.getConn().send("START");
		}
	}

	public void keyReleased(int k) {

		if (player == null || player.getControl() == null)
			return;

		if (k == player.getControl().getLeft()
				|| k == player.getControl().getRight()) {

			engine.getConn().send("POSITION#0.0#");
			pressed = false;

		}
	}

	public void keyTyped(char k) {

	}

}
