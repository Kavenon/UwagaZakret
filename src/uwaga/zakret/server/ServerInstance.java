package uwaga.zakret.server;

import java.awt.Color;
import java.awt.Event;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static ArrayList<Socket> sock = new ArrayList<Socket>();
	private static Map<String, PlayerController> names = new HashMap<String, PlayerController>();
	
	private static int FPS = 30;
	private static long targetTime = 1000 / FPS;
	
	/*private static final Logger logger = LoggerFactory
			.getLogger(ServerInstance.class);*/

	private String collisionDetected;

	public ServerInstance(int port) {
		//logger.info("Server running");
		System.out.println("---The server is running.---");
	}
	private static int getPlayersConnected(){
		return sock.size();
	}
	public void run() {

		try (ServerSocket listener = new ServerSocket(Settings.port)){
			
			//logger.info("Server loop initialized");
			new ServerLoop().start();

			while (true) {

				if (getPlayersConnected() < Settings.maxPlayers) {
					Socket incoming = listener.accept();
					sock.add(incoming);
					new Handler(incoming).start();
					//logger.info("New client connected");					
				} 
				
			}

		} catch (IOException e) {
			//logger.error(e.toString());		
			e.printStackTrace();
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
					//logger.debug("COLLIDE" + collisionDetected);		
					sendCollision();
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
					//logger.error(e.toString());	
					e.printStackTrace();
				}
			}

		}

		private void update() {
			
			for (Entry<String, PlayerController> entry : names.entrySet()) {
				PlayerController plcont = entry.getValue();

				Position current = plcont.getPlayer().getMarkerController()
						.getMarker().getCurrentPosition();
				

				int plX = (int) current.getX();
				int plY = (int) current.getY();
			
				int mapX = (board.getX() + board.getWidth());
				int mapY = (board.getY() + board.getHeight());

				if (plcont.getPlayer().isAlive()) {
					if (isCollision(plcont)) {
						plcont.getPlayer().setAlive(false);

						System.err.println("COL");
						collisionDetected = plcont.getPlayer().getUsername();
						board.decRemainingPlayers();
						break;
					}
					
					if (plX < mapX && plY < mapY && plX > 0 && plY > 0) {
						if (plcont.getPlayer().getMarkerController()
								.getMarker().isWriting()
								&& plcont.getPlayer().isAlive()) {
							board.markMap(plX, plY, plcont.getPlayer());
							
						}

					}

				}
			

				if (plcont.getPlayer().isAlive()) {				
					plcont.update();
				}
			}

			if (board.getRemainingPlayers() <= 1) {
				for (Entry<String, PlayerController> entry : names.entrySet()) {
					PlayerController plcont = entry.getValue();
					if (plcont.getPlayer().isAlive()) {
						int prevPoints = plcont.getPlayer().getPoints();
						plcont.getPlayer().setPoints(prevPoints + 1);
						if (prevPoints + 1 == Settings.goal) {
							System.err.println("WINNER");
							sendWinner(plcont.getPlayer().getUsername());
						}
						break;
					}
				}
				board.createMap();
				board.setPlaying(false);
			}
			for (Entry<String, PlayerController> entry : names.entrySet()) {

				PlayerController plcont = entry.getValue();
				System.err.print(plcont.getPlayer().getUsername() + "+"
						+ plcont.getPlayer().getPoints() + "("
						+ plcont.getPlayer().isAlive() + "):");

				// plcont.getPlayer().setPoints(0);
			}
			System.err.println();

		}

		private boolean isCollision(PlayerController plcont) {

			Position current = plcont.getPlayer().getMarkerController()
					.getMarker().getCurrentPosition();
			Position previous = plcont.getPlayer().getMarkerController()
					.getMarker().getPreviousPosition();

			// System.err.println(current.getX());
			int plX = (int) current.getX();
			int plY = (int) current.getY();
			int prevPlX = 0;
			int prevPlY = 0;
			if (previous != null) {
				prevPlX = (int) previous.getX();
				prevPlY = (int) previous.getY();
			}

			Player[][] map = board.getMap();

			

			if (plX >= board.getX() + board.getWidth()-2
					|| plY >= board.getY() + board.getHeight()-2
					|| plX <= board.getX()+2 || plY <= board.getY()+2) {
				return true;
			}

			if(previous != null ){
			if ( plX != prevPlX && plY != prevPlY) {
				if (map != null) {
					if (map[plX - 1][plY] != null || map[plX][plY - 1] != null
							|| map[plX + 1][plY] != null
							|| map[plX][plY + 1] != null
							|| map[plX][plY] != null)
						return true;
				}
			}
			else if((plX != prevPlX && plY == prevPlY ) || (plX == prevPlX && plY != prevPlY)){
				if (map != null) {
					if (map[plX][plY] != null){
						return true;
					}
				}
				
			}
			
			}

			return false;

		}

		private void sendWinner(String username) {
			for (Entry<String, PlayerController> entry : names.entrySet()) {

				PlayerController plcont = entry.getValue();
				System.err.println(plcont.getPlayer().getPoints());
				plcont.getPlayer().setPoints(0);
			}
			try {
				for (DataOutputStream writer : writers) {
					String data = "WINNER#" + username;

					writer.writeUTF(data);
					writer.flush();
				}
			}

			catch (IOException e) {

			}

		}

		private void sendCollision() {

			try {
				for (DataOutputStream writer : writers) {
					String data = "COL#" + collisionDetected;

					writer.writeUTF(data);
					writer.flush();
				}
			}

			catch (IOException e) {

			}
		}

		private void send() {

			try {
				for (DataOutputStream writer : writers) {
					String data = "OTHERSPOSITION#";
					for (Entry<String, PlayerController> entry : names
							.entrySet()) {
						PlayerController plcont = entry.getValue();
						Player pl = plcont.getPlayer();

						Position current = pl.getMarkerController().getMarker()
								.getCurrentPosition();
						data = data
								+ pl.getUsername()
								+ ","
								+ (int) current.getX()
								+ ","
								+ (int) current.getY()
								+ ","
								+ pl.getMarkerController().getMarker()
										.isWriting() + "#";

					}
					// System.out.println(data);
					writer.writeUTF(data);
					writer.flush();
				}
			}

			catch (IOException e) {

			}
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

		public synchronized void run() {

			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());

				
				while (true) {
					if(board.isPlaying()) socket.close();
					out.writeUTF("SUBMITNAME");
					name = in.readUTF();
					if (name == null) {
						return;
					}
					synchronized (names) {
						if (!names.containsKey(name)) {
							if (board.getAdmin() == null) {
							//	adminSocket = socket;
								board.setAdmin(name);
							}
							System.out.println("BEFORE");
							int[] position = board.generatePosition();
							System.out.println("AFER");
							int x = position[0];
							int y = position[1];
							int direction = position[2];
							
							Color color = board.generateColor();

							Marker marker = new Marker();
							
							marker.setCurrentPosition( new Position(x,y));
							marker.setDirection(direction);
							marker.setWriting(true);
							marker.setColor(color);
							marker.setRadius(Settings.circleRadius);
					

							MarkerController newMarkerController = new MarkerController();
							newMarkerController.setMarker(marker);

							Player newPlayer = new Player(newMarkerController,
									name, 0, new Control(Event.LEFT,
											Event.RIGHT), false);

							PlayerController newPlayerController = new PlayerController();
							newPlayerController.setPlayer(newPlayer);

							currentPlayer = newPlayerController;
							names.put(name, newPlayerController);

							String initBoard = "INITBOARD#" + board.getX()
									+ "#" + board.getY() + "#"
									+ board.getWidth() + "#"
									+ board.getHeight() + "#" + board.getAdmin() + "#" ;
							
							
							board.setPlayers(newPlayerController);
							out.writeUTF(initBoard);

							String initPlayer = "INITPLAYER#" + (int)x + "#" + (int)y + "#"
									+ (int) direction + "#"
									+ color.getRGB() + "#";
						
							out.writeUTF(initPlayer);

							out.flush();
							System.out.println("PLAYER REGISTERED:" + name);

							break;
						}
					}
				}
				// player registered
				out.writeUTF("NAMEACCEPTED");
				writers.add(out);

				// communicate with client
				while (true) {
					// System.err.println("TRYING READ");
					String input = in.readUTF();
					// System.err.println(":" + input + "-");
					if (input == null) {
						return;
					}

					// synchronized (names) {
					if (input.startsWith("READY")) {

						PlayerController found = names.get(name);
						found.getPlayer().setReady(true);
						for (Entry<String, PlayerController> entry : names
								.entrySet()) {
							for (DataOutputStream writer : writers) {
								Player value = entry.getValue().getPlayer();

								writer.writeUTF("ADDPLAYER#"
										+ value.getUsername()
										+ "#"
										+  value.getMarkerController()
												.getMarker()
												.getCurrentPosition().getX()
										+ "#"
										+  value.getMarkerController()
												.getMarker()
												.getCurrentPosition().getY()
										+ "#"
										+ value.getMarkerController()
												.getMarker().getDirection()
										+ "#"
										+ value.getMarkerController()
												.getMarker().getColor()
												.getRGB() + "#" + value.getPoints());
								writer.flush();

							}
						}

					} else if (input.startsWith("START")) {

						if (name.equals(board.getAdmin()) && !board.isPlaying()) {
							boolean allPlayersReady = true;

							int i = 0;
							for (Entry<String, PlayerController> entry : names
									.entrySet()) {

								Player value = entry.getValue().getPlayer();
								if (!value.isReady() || !value.isAlive()) {
									allPlayersReady = false;
									break;
								}
								i++;
							}
							if (allPlayersReady) {
								System.out.println("START FIRED");
								board.setRemainingPlayers(i);
								System.err.println("allready");

								for (DataOutputStream writer : writers) {
									writer.writeUTF("GAMESTARTED");
									writer.flush();
								}
								board.setPlaying(true);
								// break;
							}
						}
					} else if (input.startsWith("POSITION")) {

						Pattern p = Pattern
								.compile("POSITION\\[(-?\\d+\\.?\\d+)\\]");
						Matcher m = p.matcher(input);
						if (m.find()) {
							Double turn = Double.parseDouble(m.group(1));

							System.out.println("T:" + turn);
							// moze byc szybciej

							Player pl = currentPlayer.getPlayer();

							if (turn != 0) {
								pl.getMarkerController().startTurn(turn);
							} else {
								pl.getMarkerController().stopTurn();
							}

						}

					} else if (input.startsWith("RESET")) {
						// zamienic na funkcje losujaca fajna
						int[] position = board.generatePosition();

						int x = position[0];
						int y = position[1];
						int direction = position[2];

						Marker marker = currentPlayer.getPlayer()
								.getMarkerController().getMarker();
						marker.setCurrentPosition(new Position(x, y));
						marker.setDirection(direction);

						currentPlayer.getPlayer().setAlive(true);

						out.writeUTF("MYRESET#" + x + "#" + y + "#" + direction);

					} else if (input.startsWith("MARKER#")) {
						String[] spl = input.split("#");
						// baaaaaaaaaaaaaad
						if (spl[1].equals("UP")) {
							currentPlayer.getPlayer().getMarkerController()
									.getMarker().setWriting(true);
						} else if (spl[1].equals("DOWN")) {
							currentPlayer.getPlayer().getMarkerController()
									.getMarker().setWriting(false);
						}
					} else if (input.startsWith("REQUEST_OTHERS_RESET")) {
						
						for (Entry<String, PlayerController> entry : names
								.entrySet()) {
							
								Player value = entry.getValue().getPlayer();
								Position m = value.getMarkerController()
										.getMarker().getCurrentPosition();

								out.writeUTF("RESPONSE_OTHERS_RESET#"
										+ value.getUsername()
										+ "#"
										+ m.getX()
										+ "#"
										+ m.getY()
										+ "#"
										+ value.getMarkerController()
												.getMarker().getDirection());
							
						}
					} else {
						out.writeUTF("\r\n");
						out.flush();
						// break;
						// if (!socket.equals(adminSocket)) {break;}
					}

					// }

				}

				// to jest lipne

			} catch (IOException e) {

				if (name != null) {
					names.remove(name);
				}
				if (out != null) {
					writers.remove(out);
				}
				synchronized (sock) {
					sock.remove(socket);
				}

				if(name != null){
					board.decRemainingPlayers();
					
					System.err.println("IMPORTANTE:" + board.getPlayers().size());
					
					board.getPlayers().remove(currentPlayer);
	
					if (name.equals(board.getAdmin())) {
						if (getPlayersConnected() > 0) {
							board.setAdmin(board.getPlayers().get(0).getPlayer().getUsername());
						
						} else {
							board.setAdmin(null);
						}
					}
	
					for (DataOutputStream writer : writers) {
						try {
							writer.writeUTF("DISCONNECT#" + name + "#" + board.getAdmin());
							writer.flush();
						} catch (IOException ec) {
	
						}
	
					}
				}

			} finally {
				// This client is going down! Remove its name and its print
				// writer from the sets, and close its socket.
				if (name != null) {
					names.remove(name);
				}
				if (out != null) {
					writers.remove(out);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}

	}
}
