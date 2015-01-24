//:uwaga.zakret.server.ServerLoop.java
package uwaga.zakret.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Board;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.Settings;

/**
 * Server loop to provide own game state, coherent for all players
 */
public class ServerLoop extends Thread {

	/** The board. */
	private Board board;

	/** The collision detected. */
	private String collisionDetected;

	/** The fps. */
	private static int FPS = 30;
	
	/** The target time. */
	private static long targetTime = 1000 / FPS;
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(ServerLoop.class);

	/**
	 * Instantiates a new server loop.
	 *
	 * @param board the board
	 */
	public ServerLoop(Board board) {
		this.board = board;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		long start;
		long elapsed;
		long wait;

		while (true) {

			start = System.nanoTime();

			if (board.isPlaying())
				update();

			if (collisionDetected != null) {
				logger.debug("COLLIDE" + collisionDetected);
				ServerInstance.broadcastMessage("COL#" + collisionDetected);
				collisionDetected = null;
			}

			if (board.isPlaying())
				send();

			elapsed = System.nanoTime() - start;

			wait = targetTime - elapsed / 1000000;

			if (wait < 0)
				wait = 1;

			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				logger.error(e.toString());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Update game state
	 */
	private void update() {

		for (PlayerController playerController : board.getPlayers()) {

			Position current = playerController.getPlayer()
					.getMarkerController().getMarker().getCurrentPosition();

			int plX = (int) current.getX();
			int plY = (int) current.getY();

			int mapX = (board.getX() + board.getWidth());
			int mapY = (board.getY() + board.getHeight());

			if (playerController.getPlayer().isAlive()) {
				// action if collision detected
				if (isCollision(playerController)) {
					playerController.getPlayer().setAlive(false);

					collisionDetected = playerController.getPlayer()
							.getUsername();
					board.decRemainingPlayers();
				}

				// if nothing bad happend (player out of map, crash) markMap that he visited this area
				if (plX < mapX && plY < mapY && plX > 0 && plY > 0) {
					if (playerController.getPlayer().getMarkerController()
							.getMarker().isWriting()
							&& playerController.getPlayer().isAlive()) {
						board.markMap(plX, plY, playerController.getPlayer());
					}
				}
			}
			// update player (move to next field)
			if (playerController.getPlayer().isAlive()) {
				playerController.update();
			}
		}

		// if only one player left, look for winner
		if (board.getRemainingPlayers() <= 1 && board.isPlaying()) {
			PlayerController winner = findWinner();
			if (winner != null) {
				// increment winner points
				int prevPoints = winner.getPlayer().getPoints();
				winner.getPlayer().setPoints(prevPoints + 1);

				// if game ended (point limit reached), send winner to others and reset points
				if (checkGameEnd(prevPoints + 1)) {
					resetPoints();
					ServerInstance.broadcastMessage("WINNER#"
							+ winner.getPlayer().getUsername());
				}
			}

			// reset map
			board.createMap();
			board.setPlaying(false);
		}

	}

	/**
	 * Check if game ended
	 *
	 * @param points the points
	 * @return true, if successful
	 */
	private boolean checkGameEnd(int points) {
		return points == Settings.goal;
	}

	/**
	 * Find winner (last alive)
	 *
	 * @return the player controller
	 */
	private PlayerController findWinner() {
		for (PlayerController playerController : board.getPlayers()) {
			if (playerController.getPlayer().isAlive()) {
				return playerController;
			}
		}
		return null;
	}

	/**
	 * Checks if is collision.
	 *
	 * @param plcont the plcont
	 * @return true, if is collision
	 */
	private boolean isCollision(PlayerController plcont) {

		Position current = plcont.getPlayer().getMarkerController().getMarker()
				.getCurrentPosition();
		Position previous = plcont.getPlayer().getMarkerController()
				.getMarker().getPreviousPosition();

		int plX = (int) current.getX();
		int plY = (int) current.getY();
		int prevPlX = 0;
		int prevPlY = 0;

		if (previous != null) {
			prevPlX = (int) previous.getX();
			prevPlY = (int) previous.getY();
		}

		Player[][] map = board.getMap();

		// check if player went out of map
		if (plX >= board.getX() + board.getWidth() - Settings.boardStroke
				|| plY >= board.getY() + board.getHeight()
						- Settings.boardStroke
				|| plX <= board.getX() + Settings.boardStroke
				|| plY <= board.getY() + Settings.boardStroke) {
			return true;
		}

		// check fields and fields nearby.
		if (previous != null) {
			if (plX != prevPlX && plY != prevPlY) {
				if (map != null) {
					if (map[plX - 1][plY] != null || map[plX][plY - 1] != null
							|| map[plX + 1][plY] != null
							|| map[plX][plY + 1] != null
							|| map[plX][plY] != null)
						return true;
				}
			} else if ((plX != prevPlX && plY == prevPlY)
					|| (plX == prevPlX && plY != prevPlY)) {
				if (map != null) {
					if (map[plX][plY] != null) {
						return true;
					}
				}
			}

		}

		return false;

	}

	/**
	 * Reset points.
	 */
	private void resetPoints() {
		for (PlayerController playerController : board.getPlayers()) {
			playerController.getPlayer().setPoints(0);
		}
	}

	/**
	 * Send current position of all players
	 */
	private void send() {
		// prepare data
		String data = "OTHERSPOSITION#";
		for (PlayerController playerController : board.getPlayers()) {

			Player pl = playerController.getPlayer();

			Position current = pl.getMarkerController().getMarker()
					.getCurrentPosition();
		
			data = data + pl.getUsername() + "," + (int) current.getX() + ","
					+ (int) current.getY() + ","
					+ pl.getMarkerController().getMarker().isWriting() + "#";
		}
		// broadcast to all players
		ServerInstance.broadcastMessage(data);

	}

	/**
	 * Sets the board.
	 *
	 * @param board the new board
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

}///!~
