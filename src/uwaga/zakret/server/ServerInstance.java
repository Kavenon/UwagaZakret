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

public class ServerInstance {

	private static Board board = new Board(10, 10, 640 - 215, 480 - 20);

	private static HashSet<DataOutputStream> writers = new HashSet<DataOutputStream>();

	private static final Logger logger = LoggerFactory
			.getLogger(ServerInstance.class);

	public ServerInstance(int port) {
		logger.info("Server running");
		System.out.println("---The server is running.---");
	}

	public static int getPlayersConnected() {
		return board.getPlayers().size();
	}

	public void run() {

		try (ServerSocket listener = new ServerSocket(Settings.port)) {

			logger.info("Server loop initialized");
			new ServerLoop(board).start();

			while (true) {

				if (getPlayersConnected() < Settings.maxPlayers) {
					Socket incoming = listener.accept();
					new ClientHandler(incoming, board).start();
					logger.info("New client connected");
				}

			}

		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}

	}

	public static void broadcastMessage(String message) {

		for (DataOutputStream writer : writers) {
			sendToClient(writer, message);
		}

	}

	public static void sendToClient(DataOutputStream out, String message) {
		try {
			out.writeUTF(message);
			out.flush();
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}

	public static HashSet<DataOutputStream> getWriters() {
		return writers;
	}
}
