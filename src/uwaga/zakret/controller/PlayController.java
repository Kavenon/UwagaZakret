package uwaga.zakret.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uwaga.zakret.GameEngine;
import uwaga.zakret.model.Board;
import uwaga.zakret.model.Control;
import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.Settings;
import uwaga.zakret.view.BackgroundView;
import uwaga.zakret.view.BoardView;
import uwaga.zakret.view.ImageView;
import uwaga.zakret.view.StatsView;

public class PlayController extends Controller {

	private BoardView boardView;
	private StatsView statsView;
	private BackgroundView backgroundView;
	private ImageView logoView;

	private String winner;
	
	private int currentPlayer;
	private Board board;

	private MarkerController markerController;
	private PlayerController playerController;

	private boolean clearBoard;
	private long lastTimeToggle;
	
	private String lastWriting;
	private Random gen = new Random();
	
	
	public PlayController(GameEngine engine) {
		this.engine = engine;
	}

	public void init() {

		board = new Board();

		playerController = new PlayerController(engine);
		markerController = new MarkerController(engine);
		boardView = new BoardView(board);
		statsView = new StatsView(board.getPlayers());
				

		backgroundView = new BackgroundView("/gameplay-background.jpg");
		logoView = new ImageView("/logo.jpg");
		

	}

	public void update() {
		long currentTime = System.currentTimeMillis();
		
		if(currentTime >= lastTimeToggle){
			
			Player p = playerController.getPlayer();
			if(p != null){				
				MarkerController mark = p.getMarkerController();
				if(mark != null){
					String opt = "";
					if(lastTimeToggle != 0){
						
						
						if(lastWriting == null || lastWriting.equals("DOWN")){
							opt = "UP";
						//	mark.getMarker().setWriting(false);
						}
						else {
							opt = "DOWN";
						//	mark.getMarker().setWriting(true);
						}
						try {
							lastWriting = opt;
							engine.getConn().getWriter().writeUTF("MARKER#"+opt);
							engine.getConn().getWriter().flush();
						}
						catch (IOException e){
							
						}
						//mark.getMarker().toggleWriting();
					}
					int min,max = 0;
					
					if(opt.equals("DOWN")){
						// dluzszy odstep
						min = 300;
						max = 1000;		
					}
					else {
						min = 2000;
						max = 4000;	
							
					
					
						
						
						// krotszy odstep						
					}
					int randomNum = gen.nextInt((max - min) + 1) + min;
					//System.out.println(randomNum);
					lastTimeToggle = currentTime + randomNum;
					
				}
			}
		}
		
		
		try {
			// System.err.println("BEFORE");
			String line = engine.getConn().getReader().readUTF();
			// System.err.println("AFTER");
			if (line == null)
				return;
			 //System.err.println("TEST: " + line + "after");
			if (line.startsWith("OTHERSPOSITION")) {
				// tutaj pobieram pozycje wszystkich graczy i ustawiam na
				// current position
			
				 //System.err.println(line);
				String[] splfirst = line.split("#");

				for (int i = 1; i < splfirst.length; i++) {

					String[] splsec = splfirst[i].split(",");
					// System.out.println("---"+splsec[0]);
					for (PlayerController plc : board.getPlayers()) {
						if (plc.getPlayer().getUsername().equals(splsec[0])) {
							/** WRITING EDIT **/
							
							Position previousPosition = plc.getPlayer().getMarkerController().getMarker().getCurrentPosition();
							plc.getPlayer().getMarkerController().getMarker().setPreviousPosition(previousPosition);
							
							
							Position newPosition = new Position(
									Double.parseDouble(splsec[1]),
									Double.parseDouble(splsec[2]));
							
							plc.getPlayer().getMarkerController().getMarker()
									.setCurrentPosition(newPosition);
							plc.getPlayer().getMarkerController().getMarker().setWriting(Boolean.parseBoolean(splsec[3]));
						//	System.err.println(	Double.parseDouble(splsec[1]));
							break;
						}
					}
				}
				//engine.getConn().getWriter().writeUTF("\r\n");
				//engine.getConn().getWriter().flush();
				return;

			} else if (line.startsWith("COL")) {
				//board.setPlaying(false);
				
				String[] splfirst = line.split("#");
				for (PlayerController plc : board.getPlayers()) {
					if(plc.getPlayer().getUsername().equals(splfirst[1])){
						plc.getPlayer().setAlive(false);
						
					}				
				}
				
				
					System.err.println(line);
					board.decRemainingPlayers();
					System.out.println(playerController.getPlayer().getUsername()+":"+board.getRemainingPlayers());
					if(board.getRemainingPlayers() <= 1){
						
						for (PlayerController plc : board.getPlayers()) 						
							if(plc.getPlayer().isAlive()) plc.getPlayer().setPoints(plc.getPlayer().getPoints()+1);
						board.createMap();
						engine.getConn().getWriter().writeUTF("RESET");
						engine.getConn().getWriter().flush();
					}
				
				
				
				return;
			} 
			else if (line.startsWith("WINNER")) {
				//board.setPlaying(false);
				
				String[] splfirst = line.split("#");
				
				// DRAW WINNER splitfirst[1]
								
				
				
				
				board.setWinner(splfirst[1]); 
			
				//board.createMap();
				//engine.getConn().getWriter().writeUTF("RESET");
				//engine.getConn().getWriter().flush();		
											
			
				
				
				return;
			} 
			else if (line.startsWith("MYRESET#")) {
				String[] spl = line.split("#");
				Marker marker = playerController.getPlayer().getMarkerController().getMarker();
				
				playerController.getPlayer().setAlive(true);
				//wrigin
				marker.setPreviousPosition(null);
				marker.setCurrentPosition(new Position(Double.parseDouble(spl[1]),Double.parseDouble(spl[2])));
				marker.setDirection(Double.parseDouble(spl[3]));
				marker.setWriting(true);
				lastTimeToggle = 0;
				
				engine.getConn().getWriter().writeUTF("REQUEST_OTHERS_RESET");
				engine.getConn().getWriter().flush();
			}
			
			else if (line.startsWith("RESPONSE_OTHERS_RESET#")) {
				String[] spl = line.split("#");
				if(!spl[1].equals(playerController.getPlayer().getUsername())){
					for (PlayerController plc : board.getPlayers()) {
						if (plc.getPlayer().getUsername().equals(spl[1])) {
							plc.getPlayer().setAlive(true);
							MarkerController m = plc.getPlayer().getMarkerController();
							//writing
							m.getMarker().setWriting(true);
							m.getMarker().setPreviousPosition(null);
							m.getMarker().setCurrentPosition(new Position(Double.parseDouble(spl[2]),Double.parseDouble(spl[3])));
							m.getMarker().setDirection(Double.parseDouble(spl[4]));
						}
					}
				}
			}
			else if (line.startsWith("SUBMITNAME")) {
				Random generator = new Random();
				String name = "Player" + generator.nextInt(50);
				playerController.setPlayer(new Player(name));

				engine.getConn().getWriter().writeUTF(name);
				engine.getConn().getWriter().flush();
			} else if (line.startsWith("INITBOARD")) {
				Pattern p = Pattern
						.compile("INITBOARD\\[(\\d+\\.\\d+)\\]\\[(\\d+\\.\\d+)\\]\\[(\\d+\\.\\d+)\\]\\[(\\d+\\.\\d+)\\]");
				Matcher m = p.matcher(line);
				if (m.find()) {

					board.setX(Double.parseDouble(m.group(1)));
					board.setY(Double.parseDouble(m.group(2)));
					board.setWidth(Double.parseDouble(m.group(3)));
					board.setHeight(Double.parseDouble(m.group(4)));
					board.setPlaying(false);
					board.createMap();
				}

			} else if (line.startsWith("INITPLAYER")) {
				Pattern p = Pattern
						.compile("INITPLAYER\\[(\\d+),(\\d+)\\]\\[(-?\\d+)\\]\\[(-?\\d+)\\]");
				Matcher m = p.matcher(line);
				System.out.println(line);
				if (m.find()) {

					Marker marker = new Marker(new Position(Integer.parseInt(m
							.group(1)), Integer.parseInt(m.group(2))),
							Integer.parseInt(m.group(3)), true, new Color(
									Integer.parseInt(m.group(4))), Settings.circleRadius, 0);

					markerController.setMarker(marker);

					Player player = playerController.getPlayer();
					player.setMarkerController(markerController);
					player.setPoints(0);
					player.setControl(new Control(KeyEvent.VK_LEFT,
							KeyEvent.VK_RIGHT));
					player.setReady(false);
					
					board.incRemainingPlayers();
					playerController.setPlayer(player);

					if (board != null)
						board.setPlayers(playerController);

					engine.getConn().getWriter()
							.writeUTF("READY[" + player.getUsername() + "]");
				}

			} else if (line.startsWith("ADDPLAYER")) {

				Pattern p = Pattern
						.compile("ADDPLAYER\\[([^\\]]+)\\]\\[(\\d+\\.\\d+),(\\d+\\.\\d+)\\]\\[(-?\\d+\\.\\d+)\\]\\[(-?\\d+)\\]");
				Matcher m = p.matcher(line);
				if (m.find()) {

					if (m.group(1).equals(
							playerController.getPlayer().getUsername()))
						return;
					
					for(PlayerController pcont : board.getPlayers()){
						Player px = pcont.getPlayer();
						if(px.getUsername().equals(m.group(1))){
							return;
						}
					}
					
					board.incRemainingPlayers();

					Marker newMarker = new Marker(new Position(
							Double.parseDouble(m.group(2)),
							Double.parseDouble(m.group(3))),
							Double.parseDouble(m.group(4)), true, new Color(
									Integer.parseInt(m.group(5))), Settings.circleRadius, 0);

					MarkerController newMarkerController = new MarkerController(
							engine);
					newMarkerController.setMarker(newMarker);

					Player newPlayer = new Player(newMarkerController,
							m.group(1), 0, new Control(KeyEvent.VK_LEFT,
									KeyEvent.VK_RIGHT), false);
					
					PlayerController newController = new PlayerController(
							engine);
					newController.setPlayer(newPlayer);
					board.setPlayers(newController);

				}
			} else if (line.startsWith("GAMESTARTED")) {
				clearBoard = true;
				board.setPlaying(true);
			    playerController.getPlayer().setAlive(true);
			    System.err.println(board.getRemainingPlayers());
				board.setRemainingPlayers(board.getPlayers().size());
				
				if(board.getWinner() != null){
					for (PlayerController plc : board.getPlayers()){
						plc.getPlayer().setPoints(0);
					}
					board.setWinner(null);
				}
				
				
				// engine.getConn().getWriter().writeUTF("POSITION[0.0]");
				// engine.getConn().getWriter().flush();
			} 
			else if (line.startsWith("DISCONNECT")) {
				String[] spl = line.split("#");
				for(PlayerController pcont : board.getPlayers()){
					if(pcont.getPlayer().getUsername().equals(spl[1])) {
						board.getPlayers().remove(pcont);
						break;
					}
				}
				
				// engine.getConn().getWriter().writeUTF("POSITION[0.0]");
				// engine.getConn().getWriter().flush();
			} else {
				return;
			}
			// engine.getConn().getWriter().writeUTF("\r\n");
			// engine.getConn().getWriter().flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void draw(Graphics2D g) {
	
		
		// move to boardView, create mehtod CLEAR BOARD
		if(clearBoard){
			g.clearRect((int) board.getX(), (int) board.getY(),
					(int) board.getWidth(), (int) board.getHeight());
			clearBoard = false;
		}
		
		boardView.draw(g);
		statsView.draw(g);
		logoView.draw(g, 460, 20, 156, 54);
		
	}

	public void keyTyped(char k) {
		playerController.keyTyped(k);
	}

	public void keyPressed(int k) {
		playerController.keyPressed(k);
	}

	public void keyReleased(int k) {
		playerController.keyReleased(k);
	}
}
