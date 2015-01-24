//:uwaga.zakret.controller.PlayController.java
package uwaga.zakret.controller;

import java.awt.Graphics2D;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uwaga.zakret.GameEngine;
import uwaga.zakret.model.Board;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.commands.ActionChain;
import uwaga.zakret.model.commands.client.AddplayerAction;
import uwaga.zakret.model.commands.client.ColAction;
import uwaga.zakret.model.commands.client.DisconnectAction;
import uwaga.zakret.model.commands.client.GamestartedAction;
import uwaga.zakret.model.commands.client.InitboardAction;
import uwaga.zakret.model.commands.client.InitplayerAction;
import uwaga.zakret.model.commands.client.MyresetAction;
import uwaga.zakret.model.commands.client.OtherpositionAction;
import uwaga.zakret.model.commands.client.ResponseOthersResetAction;
import uwaga.zakret.model.commands.client.SubmitnameAction;
import uwaga.zakret.model.commands.client.WinnerAction;
import uwaga.zakret.view.BoardView;
import uwaga.zakret.view.ImageView;
import uwaga.zakret.view.StatsView;

/**
 * Play game state
 */
public class PlayController extends Controller {

	/** The board view. */
	private BoardView boardView;
	
	/** The stats view. */
	private StatsView statsView;

	/** The footer view. */
	private ImageView logoView, footerView;

	/** The board. */
	private Board board;

	/** The marker controller. */
	private MarkerController markerController;
	
	/** The player controller. */
	private PlayerController playerController;

	/** The last writing. */
	private String lastWriting;
	
	/** The random generator */
	private Random gen = new Random();

	/** The action chain. */
	private ActionChain actionChain;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(PlayController.class);

	/**
	 * Instantiates a new play controller.
	 *
	 * @param engine the engine
	 */
	public PlayController(GameEngine engine) {
		this.engine = engine;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#init()
	 */
	public void init() {
		
		board = new Board();

		playerController = new PlayerController(engine);
		markerController = new MarkerController(engine);
		boardView = new BoardView(board);
		statsView = new StatsView(board.getPlayers());

		logoView = new ImageView("/logo.jpg");
		footerView = new ImageView("/footer.jpg");

		/** Chain of responsibility **/
		actionChain = new ActionChain();
		actionChain.setBoard(board);
		actionChain.setMarkerController(markerController);
		actionChain.setPlayerController(playerController);
		actionChain.setConnection(engine.getConn());

		/** possible commands parsed by client **/
		actionChain.add(new OtherpositionAction("OTHERSPOSITION"));
		actionChain.add(new ColAction("COL"));
		actionChain.add(new WinnerAction("WINNER"));
		actionChain.add(new GamestartedAction("GAMESTARTED"));
		actionChain.add(new SubmitnameAction("SUBMITNAME"));
		actionChain.add(new InitboardAction("INITBOARD"));
		actionChain.add(new InitplayerAction("INITPLAYER"));
		actionChain.add(new AddplayerAction("ADDPLAYER"));
		actionChain.add(new MyresetAction("MYRESET"));
		actionChain.add(new ResponseOthersResetAction("RESPONSE_OTHERS_RESET"));
		actionChain.add(new DisconnectAction("DISCONNECT"));

	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#update()
	 */
	public void update() {

		// check if it's time to put down or up marker
		toggleMarkerWriting();

		try {

			String line = engine.getConn().read();
			logger.debug("SERV: " + line);

			if (line == null)
				return;

			// parse command received from server
			actionChain.start(line);

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

	/**
	 * Toggle marker writing.
	 */
	private void toggleMarkerWriting() {
		if (!board.isPlaying())
			return;
		long currentTime = System.currentTimeMillis();
		long lastTimeToggle = playerController.getPlayer()
				.getMarkerController().getMarker().getLastTimeToggle();
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

					playerController.getPlayer().getMarkerController()
							.getMarker()
							.setLastTimeToggle(currentTime + randomNum);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#setError(java.lang.String)
	 */
	public void setError(String msg) {
		error = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g) {

		boardView.draw(g);
		statsView.draw(g);
		logoView.draw(g, 460, 20, 156, 54);

		// draw if admin
		if (board != null && playerController != null
				&& playerController.getPlayer() != null
				&& board.getAdmin() != null) {
			if (board.getAdmin().equals(
					playerController.getPlayer().getUsername()))
				footerView.draw(g, 460, 414 - 10, 156, 54);
		}

	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#keyTyped(char)
	 */
	public void keyTyped(char k) {
		if (playerController != null)
			playerController.keyTyped(k);
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#keyPressed(int)
	 */
	public void keyPressed(int k) {
		if (playerController != null)
			playerController.keyPressed(k);
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#keyReleased(int)
	 */
	public void keyReleased(int k) {
		if (playerController != null)
			playerController.keyReleased(k);
	}
}///!~
