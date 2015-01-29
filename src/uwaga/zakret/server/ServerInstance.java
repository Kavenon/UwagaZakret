//:uwaga.zakret.server.ServerInstance.java
package uwaga.zakret.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uwaga.zakret.model.Board;
import uwaga.zakret.model.Settings;

/**
 * Main ServerInstance, has own board and game state, creates thread with game loop and thread for each player
 */
public class ServerInstance {

	/** The board. */
	private static Board board = new Board(10, 10, 640 - 215, 480 - 20);

	/** The writers. */
	private static HashSet<DataOutputStream> writers = new HashSet<DataOutputStream>();

	/** Is running */
	private boolean running;
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(ServerInstance.class);

	/**
	 * Instantiates a new server instance.
	 *
	 * @param port the port
	 */
	public ServerInstance(int port) {
		running = true;
		logger.info("Server running");
		System.out.println("---The server is running.---");
	}

	/**
	 * Gets the players connected.
	 *
	 * @return the players connected
	 */
	public static int getPlayersConnected() {
		return board.getPlayers().size();
	}

	/**
	 * Run.
	 */
	public void run() {

		try (ServerSocket listener = new ServerSocket(Settings.port)) {

			// start server loop
			logger.info("Server loop initialized");
			new ServerLoop(board).start();

			while (running) {

				// create new thread for player handling
				if (getPlayersConnected() < Settings.maxPlayers) {
					Socket incoming = listener.accept();
					new ClientHandler(incoming, board).start();
					logger.info("New client connected");
				}

			}

		} catch (IOException e) {
			logger.error(e.toString());		
		}

	}
	
	public void stop() throws IOException {
		for (DataOutputStream writer : writers) {
			writer.close();
		}		
		running = false;
	}

	/**
	 * Broadcast message to all clients connected
	 *
	 * @param message the message
	 */
	public static void broadcastMessage(String message) {

		for (DataOutputStream writer : writers) {
			sendToClient(writer, message);
		}

	}

	/**
	 * Send to single client.
	 *
	 * @param out the out
	 * @param message the message
	 */
	public static void sendToClient(DataOutputStream out, String message) {
		try {
			out.writeUTF(message);			
			out.flush();
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}

	/**
	 * Gets the writers.
	 *
	 * @return the writers
	 */
	public static HashSet<DataOutputStream> getWriters() {
		return writers;
	}
}///!~
