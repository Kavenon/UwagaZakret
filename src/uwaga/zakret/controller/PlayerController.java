//:uwaga.zakret.controller.PlayerController.java
package uwaga.zakret.controller;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import uwaga.zakret.GameEngine;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Settings;
import uwaga.zakret.view.PlayerView;

/**
 * Controls the player
 */
public class PlayerController extends Controller {

	/** The player. */
	private Player player;

	/** The player view. */
	private PlayerView playerView;

	/** The pressed. */
	private boolean pressed;

	/**
	 * Instantiates a new player controller.
	 *
	 * @param engine the engine
	 */
	public PlayerController(GameEngine engine) {
		this.engine = engine;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#setError(java.lang.String)
	 */
	public void setError(String msg) {
		error = msg;
	}

	/**
	 * Instantiates a new player controller.
	 */
	public PlayerController() {

	}

	/**
	 * Sets the player.
	 *
	 * @param player the new player
	 */
	public void setPlayer(Player player) {
		this.player = player;
		playerView = new PlayerView(player);
	}

	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#init()
	 */
	public void init() {

	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#update()
	 */
	public void update() {
		// update player marker
		MarkerController markCont = player.getMarkerController();

		if (markCont.isTurning()) {
			markCont.turn();
		}

		markCont.move();
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g) {
		playerView.draw(g);
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#keyPressed(int)
	 */
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
		} else if (k == KeyEvent.VK_ESCAPE) {
			engine.getConn().close();
		}
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#keyReleased(int)
	 */
	public void keyReleased(int k) {

		if (player == null || player.getControl() == null)
			return;

		if (k == player.getControl().getLeft()
				|| k == player.getControl().getRight()) {

			engine.getConn().send("POSITION#0.0#");
			pressed = false;

		}
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#keyTyped(char)
	 */
	public void keyTyped(char k) {

	}

}///!~
