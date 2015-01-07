package uwaga.zakret.server;

import java.awt.Color;
import java.awt.Event;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import uwaga.zakret.controller.MarkerController;
import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Board;
import uwaga.zakret.model.Control;
import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.Settings;

public class ServerInstance {

	private static Board board = new Board(10, 10, 640 - 215, 480 - 20);

	private static HashSet<DataOutputStream> writers = new HashSet<DataOutputStream>();

	private static int FPS = 30;
	private static long targetTime = 1000 / FPS;

	/*
	 * private static final Logger logger = LoggerFactory
	 * .getLogger(ServerInstance.class);
	 */

	private String collisionDetected;

	public ServerInstance(int port) {
		// logger.info("Server running");
		System.out.println("---The server is running.---");
	}

	private static int getPlayersConnected() {
		return board.getPlayers().size();
	}

	public void run() {

		try (ServerSocket listener = new ServerSocket(Settings.port)) {

			// logger.info("Server loop initialized");
			new ServerLoop().start();

			while (true) {

				if (getPlayersConnected() < Settings.maxPlayers) {
					Socket incoming = listener.accept();
					new Handler(incoming).start();
					// logger.info("New client connected");
				}

			}

		} catch (IOException e) {
			// logger.error(e.toString());
			e.printStackTrace();
		}

	}

	private static void broadcastMessage(String message) {

		for (DataOutputStream writer : writers) {
			sendToClient(writer, message);
		}

	}

	private static void sendToClient(DataOutputStream out, String message) {
		try {
			out.writeUTF(message);
			out.flush();
		} catch (IOException e) {
			// logger.error(e.toString());
		}
	}

	public class ServerLoop extends Thread {

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
					broadcastMessage("COL#" + collisionDetected);
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
						// break;
					}

					if (plX < mapX && plY < mapY && plX > 0 && plY > 0) {
						if (playerController.getPlayer().getMarkerController()
								.getMarker().isWriting()
								&& playerController.getPlayer().isAlive()) {
							board.markMap(plX, plY,
									playerController.getPlayer());
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
						broadcastMessage("WINNER#"
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

			Position current = plcont.getPlayer().getMarkerController()
					.getMarker().getCurrentPosition();
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
						if (map[plX - 1][plY] != null
								|| map[plX][plY - 1] != null
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
				data = data + pl.getUsername() + "," + (int) current.getX()
						+ "," + (int) current.getY() + ","
						+ pl.getMarkerController().getMarker().isWriting()
						+ "#";
			}

			broadcastMessage(data);

		}

	}

	private static class Handler extends Thread {

		private String name;
		private Socket socket;
		private DataInputStream in;
		private DataOutputStream out;
		private PlayerController currentPlayer;

		public Handler(Socket socket) {
			this.socket = socket;
		}

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

		public void run() {

			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());

				while (true) {

					if (board.isPlaying())
						socket.close();

					out.writeUTF("SUBMITNAME");
					out.flush();
					name = in.readUTF();

					// logger.debug("CLIENT:" + name);
					if (name == null) {
						return;
					}

					synchronized (board.getPlayers()) {
						if (!checkUsername(name)) {
							continue;
						}

						if (board.getAdmin() == null) {
							board.setAdmin(name);
						}

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

						Player newPlayer = new Player(newMarkerController,
								name, 0, new Control(Event.LEFT, Event.RIGHT),
								false);

						PlayerController newPlayerController = new PlayerController();
						newPlayerController.setPlayer(newPlayer);

						currentPlayer = newPlayerController;

						String initBoard = "INITBOARD#" + board.getX() + "#"
								+ board.getY() + "#" + board.getWidth() + "#"
								+ board.getHeight() + "#" + board.getAdmin()
								+ "#";

						board.setPlayers(newPlayerController);
						
						sendToClient(out, initBoard);
					
						String initPlayer = "INITPLAYER#" + (int) x + "#"
								+ (int) y + "#" + (int) direction + "#"
								+ color.getRGB() + "#";

						sendToClient(out, initPlayer);
					
						// logger.info("Player.Registered : " + name);

						break;
					}

				}
			
				sendToClient(out, "NAMEACCEPTED");
			
				writers.add(out);

				// communicate with client
				while (true) {
				
					String input = in.readUTF();
					// logger.debug("CLIENT:" + input);
					
					if (input == null) {
						return;
					}

					if (input.startsWith("READY")) {

						currentPlayer.getPlayer().setReady(true);

						for (PlayerController playerController : board
								.getPlayers()) {

							Player player = playerController.getPlayer();

							String data = "";

							data = "ADDPLAYER#"
									+ player.getUsername()
									+ "#"
									+ player.getMarkerController().getMarker()
											.getCurrentPosition().getX()
									+ "#"
									+ player.getMarkerController().getMarker()
											.getCurrentPosition().getY()
									+ "#"
									+ player.getMarkerController().getMarker()
											.getDirection()
									+ "#"
									+ player.getMarkerController().getMarker()
											.getColor().getRGB() + "#"
									+ player.getPoints();

							broadcastMessage(data);

						}

					} else if (input.startsWith("START")) {

						if (name.equals(board.getAdmin()) && !board.isPlaying()
								&& board.getPlayers().size() > 1) {
							boolean allPlayersReady = true;

							int i = 0;
							for (PlayerController playerController : board
									.getPlayers()) {
								Player player = playerController.getPlayer();
								if (!player.isReady() || !player.isAlive()) {
									allPlayersReady = false;
									break;
								}
								i++;
							}

							if (allPlayersReady) {

								ServerInstance.broadcastMessage("GAMESTARTED");

								board.setRemainingPlayers(i);
								board.setPlaying(true);
								// logger.info("Gamestarted");

							}
						}
					} else if (input.startsWith("POSITION")) {

						String[] split = input.split("#");

						Double turn = Double.parseDouble(split[1]);
						Player pl = currentPlayer.getPlayer();

						if (turn != 0) {
							pl.getMarkerController().startTurn(turn);
						} else {
							pl.getMarkerController().stopTurn();
						}

					} else if (input.startsWith("RESET")) {

						int[] position = board.generatePosition();
						int x = position[0];
						int y = position[1];
						int direction = position[2];

						Marker marker = currentPlayer.getPlayer()
								.getMarkerController().getMarker();
						marker.setCurrentPosition(new Position(x, y));
						marker.setDirection(direction);

						currentPlayer.getPlayer().setAlive(true);
						
						sendToClient(out, "MYRESET#" + x + "#" + y + "#" + direction);
						
					} else if (input.startsWith("MARKER#")) {
						String[] spl = input.split("#");

						if (spl[1].equals("DOWN")) {
							currentPlayer.getPlayer().getMarkerController()
									.getMarker().setWriting(true);
						} else if (spl[1].equals("UP")) {
							currentPlayer.getPlayer().getMarkerController()
									.getMarker().setWriting(false);
						}

					} else if (input.startsWith("REQUEST_OTHERS_RESET")) {

						for (PlayerController playerController : board
								.getPlayers()) {

							Player player = playerController.getPlayer();
							Position m = player.getMarkerController()
									.getMarker().getCurrentPosition();

							String data = "RESPONSE_OTHERS_RESET#"
									+ player.getUsername()
									+ "#"
									+ m.getX()
									+ "#"
									+ m.getY()
									+ "#"
									+ player.getMarkerController().getMarker()
											.getDirection();
							
							sendToClient(out, data);
						
						}

					} else {
						sendToClient(out, "\r\n");						
					}

				}

			} catch (IOException e) {
				// logger.info("Connection lost");
			} finally {

				disconnectPlayer();

				try {
					socket.close();
				} catch (IOException e) {
					// logger.error(e.toString());
				}

			}
		}

		private void disconnectPlayer() {
			if (out != null) {
				writers.remove(out);
			}

			if (name != null) {
				// logger.info("Player " + name + " disconnected);
				board.decRemainingPlayers();
				board.getPlayers().remove(currentPlayer);

				if (getPlayersConnected() == 1) {
					board.setPlaying(false);
				}
				if (name.equals(board.getAdmin())) {
					if (getPlayersConnected() > 0) {
						board.setAdmin(board.getPlayers().get(0).getPlayer()
								.getUsername());

					} else {
						board.setAdmin(null);
					}
				}

				broadcastMessage("DISCONNECT#" + name + "#" + board.getAdmin());

			}
		}
	}
}
