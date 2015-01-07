package uwaga.zakret.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uwaga.zakret.GameEngine;
import uwaga.zakret.model.Board;
import uwaga.zakret.model.Control;
import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.Settings;
import uwaga.zakret.view.BoardView;
import uwaga.zakret.view.ImageView;
import uwaga.zakret.view.StatsView;


public class PlayController extends Controller {

	private BoardView boardView;
	private StatsView statsView;

	private ImageView logoView, footerView;

	private Board board;

	private MarkerController markerController;
	private PlayerController playerController;

	private boolean clearBoard;
	private long lastTimeToggle;

	private String lastWriting;
	private Random gen = new Random();
	
	private static final Logger logger = LoggerFactory
			.getLogger(PlayController.class);

	public PlayController(GameEngine engine) {
		this.engine = engine;
	}

	public void init() {
		
		logger.debug("Initialized");
		
		board = new Board();

		playerController = new PlayerController(engine);
		markerController = new MarkerController(engine);
		boardView = new BoardView(board);
		statsView = new StatsView(board.getPlayers());

		logoView = new ImageView("/logo.jpg");
		footerView = new ImageView("/footer.jpg");

	}

	public void update() {
		
		toggleMarkerWriting();

		try {

			String line = engine.getConn().read();
			logger.debug("SERV: " + line);
			if (line == null)
				return;

			if (line.startsWith("OTHERSPOSITION")) {
				
				String[] splfirst = line.split("#");

				for (int i = 1; i < splfirst.length; i++) {

					String[] splsec = splfirst[i].split(",");

					for (PlayerController plc : board.getPlayers()) {
						if (plc.getPlayer().getUsername().equals(splsec[0])) {

							Position previousPosition = plc.getPlayer()
									.getMarkerController().getMarker()
									.getCurrentPosition();
							plc.getPlayer().getMarkerController().getMarker()
									.setPreviousPosition(previousPosition);

							Position newPosition = new Position(
									Double.parseDouble(splsec[1]),
									Double.parseDouble(splsec[2]));
							plc.getPlayer().getMarkerController().getMarker()
									.setCurrentPosition(newPosition);

							plc.getPlayer()
									.getMarkerController()
									.getMarker()
									.setWriting(Boolean.parseBoolean(splsec[3]));

							break;
						}
					}
				}

			} else if (line.startsWith("COL")) {

				String[] splfirst = line.split("#");
				for (PlayerController plc : board.getPlayers()) {
					if (plc.getPlayer().getUsername().equals(splfirst[1])) {
						plc.getPlayer().setAlive(false);
					}
				}

				board.decRemainingPlayers();

				if (board.getRemainingPlayers() <= 1) {

					for (PlayerController plc : board.getPlayers()) {
						if (plc.getPlayer().isAlive())
							plc.getPlayer().setPoints(
									plc.getPlayer().getPoints() + 1);
					}

					board.createMap();

					engine.getConn().send("RESET");

				}

			} else if (line.startsWith("WINNER")) {

				String[] splfirst = line.split("#");

				board.setWinner(splfirst[1]);

			} else if (line.startsWith("MYRESET#")) {

				String[] spl = line.split("#");

				Marker marker = playerController.getPlayer()
						.getMarkerController().getMarker();

				playerController.getPlayer().setAlive(true);

				marker.setPreviousPosition(null);
				marker.setCurrentPosition(new Position(Double
						.parseDouble(spl[1]), Double.parseDouble(spl[2])));
				marker.setDirection(Double.parseDouble(spl[3]));
				marker.setWriting(true);

				lastTimeToggle = 0;

				engine.getConn().send("REQUEST_OTHERS_RESET");

			} else if (line.startsWith("RESPONSE_OTHERS_RESET#")) {

				String[] spl = line.split("#");

				if (!spl[1].equals(playerController.getPlayer().getUsername())) {
					for (PlayerController plc : board.getPlayers()) {
						if (plc.getPlayer().getUsername().equals(spl[1])) {

							plc.getPlayer().setAlive(true);

							MarkerController m = plc.getPlayer()
									.getMarkerController();

							m.getMarker().setWriting(true);
							m.getMarker().setPreviousPosition(null);
							m.getMarker().setCurrentPosition(
									new Position(Double.parseDouble(spl[2]),
											Double.parseDouble(spl[3])));
							m.getMarker().setDirection(
									Double.parseDouble(spl[4]));

						}
					}
				}
			} else if (line.startsWith("SUBMITNAME")) {
				Random generator = new Random();
				String name = "Player" + generator.nextInt(50);
				playerController.setPlayer(new Player(name));

				engine.getConn().send(name);

			} else if (line.startsWith("INITBOARD")) {

				String[] split = line.split("#");

				board.setX(Integer.parseInt(split[1]));
				board.setY(Integer.parseInt(split[2]));
				board.setWidth(Integer.parseInt(split[3]));
				board.setHeight(Integer.parseInt(split[4]));
				board.setPlaying(false);
				board.createMap();

				board.setAdmin(split[5]);

			} else if (line.startsWith("INITPLAYER")) {

				String[] split = line.split("#");

				Marker initMarker = new Marker();

				initMarker.setCurrentPosition(new Position(Double
						.parseDouble(split[1]), Double.parseDouble(split[2])));
				initMarker.setDirection(Double.parseDouble(split[3]));
				initMarker.setWriting(true);
				initMarker.setColor(new Color(Integer.parseInt(split[4])));
				initMarker.setRadius(Settings.circleRadius);

				markerController.setMarker(initMarker);

				Player player = playerController.getPlayer();

				player.setMarkerController(markerController);
				player.setPoints(0);
				player.setControl(new Control(KeyEvent.VK_LEFT,
						KeyEvent.VK_RIGHT));
				player.setReady(false);

				board.incRemainingPlayers();

				playerController.setPlayer(player);

				//if (board != null)
					board.setPlayers(playerController);

				engine.getConn().send("READY[" + player.getUsername() + "]");

			} else if (line.startsWith("ADDPLAYER")) {

				String[] split = line.split("#");

				if (split[1].equals(playerController.getPlayer().getUsername()))
					return;

				for (PlayerController pcont : board.getPlayers()) {
					Player px = pcont.getPlayer();
					if (px.getUsername().equals(split[1])) {
						return;
					}
				}

				board.incRemainingPlayers();

				Marker newMarker = new Marker();

				newMarker.setCurrentPosition(new Position(Double
						.parseDouble(split[2]), Double.parseDouble(split[3])));
				newMarker.setDirection(Double.parseDouble(split[4]));
				newMarker.setWriting(true);
				newMarker.setColor(new Color(Integer.parseInt(split[5])));
				newMarker.setRadius(Settings.circleRadius);

				MarkerController newMarkerController = new MarkerController(
						engine);

				newMarkerController.setMarker(newMarker);

				Player newPlayer = new Player(split[1]);
				newPlayer.setMarkerController(newMarkerController);
				newPlayer.setPoints(Integer.parseInt(split[6]));
				newPlayer.setControl(new Control(KeyEvent.VK_LEFT,
						KeyEvent.VK_RIGHT));
				newPlayer.setReady(false);

				PlayerController newController = new PlayerController(engine);

				newController.setPlayer(newPlayer);

				board.setPlayers(newController);

			} else if (line.startsWith("GAMESTARTED")) {

				clearBoard = true;

				board.setPlaying(true);

				playerController.getPlayer().setAlive(true);

				board.setRemainingPlayers(board.getPlayers().size());

				if (board.getWinner() != null) {
					for (PlayerController plc : board.getPlayers()) {
						plc.getPlayer().setPoints(0);
					}
					board.setWinner(null);
				}

			} else if (line.startsWith("DISCONNECT")) {
				String[] spl = line.split("#");
				for (PlayerController pcont : board.getPlayers()) {
					if (pcont.getPlayer().getUsername().equals(spl[1])) {
						board.getPlayers().remove(pcont);
						break;
					}
				}
				board.setAdmin(spl[2]);

			} else {
				return;
			}

		} catch (EOFException e) {
			logger.error(e.toString());
			engine.setState(GameEngine.CONNECT_FORM).setError(
					"Trwa rozgrywka, sprobuj pozniej.");
		} catch (SocketException e) {
			logger.error(e.toString());
			engine.setState(GameEngine.CONNECT_FORM).setError(
					"Utracono po³¹czenie z serwerem.");
		} catch (IOException e) {
			logger.error(e.toString());			
		}

	}

	private void toggleMarkerWriting() {
		long currentTime = System.currentTimeMillis();

		if (currentTime >= lastTimeToggle && board.isPlaying()) {

			Player p = playerController.getPlayer();
			if (p != null) {
				MarkerController mark = p.getMarkerController();
				if (mark != null) {
					String opt = "";
					if (lastTimeToggle != 0) {

						if (lastWriting == null || lastWriting.equals("UP")) {
							opt = "DOWN";
						} else {
							opt = "UP";
						}

						lastWriting = opt;
						engine.getConn().send("MARKER#" + opt);

					}
					
					int min, max = 0;

					if (opt.equals("UP")) {					
						min = 300;
						max = 1000;
					} else {
						min = 2000;
						max = 4000;						
					}
					int randomNum = gen.nextInt((max - min) + 1) + min;
					
					lastTimeToggle = currentTime + randomNum;
				}
			}
		}
	}

	public void setError(String msg) {
		error = msg;
	}

	public void draw(Graphics2D g) {

		if (clearBoard) {
			boardView.clearBoard(g);
			clearBoard = false;
		}

		boardView.draw(g);
		statsView.draw(g);
		logoView.draw(g, 460, 20, 156, 54);

		if (board != null && playerController != null
				&& playerController.getPlayer() != null
				&& board.getAdmin() != null) {
			if (board.getAdmin().equals(
					playerController.getPlayer().getUsername()))
				footerView.draw(g, 460, 414 - 10, 156, 54);
		}

	}

	public void keyTyped(char k) {
		if(playerController != null) playerController.keyTyped(k);
	}

	public void keyPressed(int k) {
		if(playerController != null) playerController.keyPressed(k);
	}

	public void keyReleased(int k) {
		if(playerController != null) playerController.keyReleased(k);
	}
}
