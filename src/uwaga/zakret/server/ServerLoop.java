package uwaga.zakret.server;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Board;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.Settings;

public class ServerLoop extends Thread {

	private Board board;

	private String collisionDetected;

	private static int FPS = 30;
	private static long targetTime = 1000 / FPS;

	public ServerLoop(Board board) {
		this.board = board;
	}

	public void run() {
		long start;
		long elapsed;
		long wait;

		while (true) {

			start = System.nanoTime();

			if (board.isPlaying())
				update();

			if (collisionDetected != null) {
				// logger.debug("COLLIDE" + collisionDetected);
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
				// logger.error(e.toString());
				e.printStackTrace();
			}
		}
	}

	private void update() {

		for (PlayerController playerController : board.getPlayers()) {

			Position current = playerController.getPlayer()
					.getMarkerController().getMarker().getCurrentPosition();

			int plX = (int) current.getX();
			int plY = (int) current.getY();

			int mapX = (board.getX() + board.getWidth());
			int mapY = (board.getY() + board.getHeight());

			if (playerController.getPlayer().isAlive()) {
				if (isCollision(playerController)) {
					playerController.getPlayer().setAlive(false);

					collisionDetected = playerController.getPlayer()
							.getUsername();
					board.decRemainingPlayers();
				}

				if (plX < mapX && plY < mapY && plX > 0 && plY > 0) {
					if (playerController.getPlayer().getMarkerController()
							.getMarker().isWriting()
							&& playerController.getPlayer().isAlive()) {
						board.markMap(plX, plY, playerController.getPlayer());
					}
				}
			}
			if (playerController.getPlayer().isAlive()) {
				playerController.update();
			}
		}

		if (board.getRemainingPlayers() <= 1 && board.isPlaying()) {
			PlayerController winner = findWinner();
			if (winner != null) {
				int prevPoints = winner.getPlayer().getPoints();
				winner.getPlayer().setPoints(prevPoints + 1);

				if (checkGameEnd(prevPoints + 1)) {
					resetPoints();
					ServerInstance.broadcastMessage("WINNER#"
							+ winner.getPlayer().getUsername());
				}
			}

			board.createMap();
			board.setPlaying(false);
		}

	}

	private boolean checkGameEnd(int points) {
		return points == Settings.goal;
	}

	private PlayerController findWinner() {
		for (PlayerController playerController : board.getPlayers()) {
			if (playerController.getPlayer().isAlive()) {
				return playerController;
			}
		}
		return null;
	}

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

		if (plX >= board.getX() + board.getWidth() - Settings.boardStroke
				|| plY >= board.getY() + board.getHeight()
						- Settings.boardStroke
				|| plX <= board.getX() + Settings.boardStroke
				|| plY <= board.getY() + Settings.boardStroke) {
			return true;
		}

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

	private void resetPoints() {
		for (PlayerController playerController : board.getPlayers()) {
			playerController.getPlayer().setPoints(0);
		}
	}

	private void send() {

		String data = "OTHERSPOSITION#";
		for (PlayerController playerController : board.getPlayers()) {

			Player pl = playerController.getPlayer();

			Position current = pl.getMarkerController().getMarker()
					.getCurrentPosition();
			data = data + pl.getUsername() + "," + (int) current.getX() + ","
					+ (int) current.getY() + ","
					+ pl.getMarkerController().getMarker().isWriting() + "#";
		}

		ServerInstance.broadcastMessage(data);

	}

	public void setBoard(Board board) {
		this.board = board;
	}

}
