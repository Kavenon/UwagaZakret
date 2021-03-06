//:uwaga.zakret.server.ClientHandler.java
package uwaga.zakret.server;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Board;
import uwaga.zakret.model.Connection;
import uwaga.zakret.model.commands.ActionChain;
import uwaga.zakret.model.commands.server.MarkerAction;
import uwaga.zakret.model.commands.server.PositionAction;
import uwaga.zakret.model.commands.server.ReadyAction;
import uwaga.zakret.model.commands.server.RegisterAction;
import uwaga.zakret.model.commands.server.RequestOthersResetAction;
import uwaga.zakret.model.commands.server.ResetAction;
import uwaga.zakret.model.commands.server.StartAction;

/**
 * Class that handles request from single client
 */
public class ClientHandler extends Thread {

	/** The socket. */
	private Socket socket;

	/** The current player. */
	private PlayerController currentPlayer;

	/** The action chain. */
	private ActionChain actionChain;

	/** The connection with client */
	private Connection conn;

	/** The board. */
	private Board board;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(ClientHandler.class);

	/**
	 * Instantiates a new client handler.
	 * 
	 * @param socket
	 *            the socket
	 * @param board
	 *            the board
	 */
	public ClientHandler(Socket socket, Board board) {

		this.socket = socket;
		this.board = board;

		// create connection
		conn = new Connection();
		conn.setSocket(socket);
		conn.createDataStreams();

		currentPlayer = new PlayerController();

		// create chain of responsibility
		actionChain = new ActionChain();
		actionChain.setBoard(board);
		actionChain.setPlayerController(currentPlayer);
		actionChain.setConnection(conn);

		// commands that are understood
		actionChain.add(new RegisterAction("REGISTER"));
		actionChain.add(new ResetAction("RESET"));
		actionChain.add(new RequestOthersResetAction("REQUEST_OTHERS_RESET"));
		actionChain.add(new PositionAction("POSITION"));
		actionChain.add(new ReadyAction("READY"));
		actionChain.add(new StartAction("START"));
		actionChain.add(new MarkerAction("MARKER"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {

		try {

			if (board.isPlaying())
				socket.close();

			conn.send("SUBMITNAME");

			while (true) {

				String input = conn.read();

				logger.debug("CLIENT:" + input);

				if (input == null) {
					return;
				}

				// look for apropiate link from chain
				actionChain.start(input);

			}

		} catch (IOException e) {
			logger.info("Connection lost");
		} finally {

			disconnectPlayer();

			try {
				socket.close();
			} catch (IOException e) {
				logger.error(e.toString());
			}

		}
	}

	/**
	 * Method that runs after client disconnected/connection lost
	 */
	private void disconnectPlayer() {
		if (conn.getWriter() != null) {
			ServerInstance.getWriters().remove(conn.getWriter());
		}

		if (board != null && currentPlayer != null
				&& currentPlayer.getPlayer() != null
				&& currentPlayer.getPlayer().getUsername() != null) {

			logger.info("Player " + currentPlayer.getPlayer().getUsername()
					+ " disconnected");
			board.decRemainingPlayers();
			board.getPlayers().remove(currentPlayer);

			// stop game if only one player left
			if (ServerInstance.getPlayersConnected() == 1) {
				board.setPlaying(false);
			}

			// if player was admin, change admin to null or next player
			if (currentPlayer.getPlayer().getUsername()
					.equals(board.getAdmin())) {
				if (ServerInstance.getPlayersConnected() > 0) {
					board.setAdmin(board.getPlayers().get(0).getPlayer()
							.getUsername());

				} else {
					board.setAdmin(null);
				}
			}

			// inform others that someone disconnected
			ServerInstance.broadcastMessage("DISCONNECT#"
					+ currentPlayer.getPlayer().getUsername() + "#"
					+ board.getAdmin());

		}
	}
}// /!~
