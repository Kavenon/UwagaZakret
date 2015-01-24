//:uwaga.zakret.model.Connection.java
package uwaga.zakret.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/**
 * Model of connection client->server, server->client
 */
public class Connection {

	/** The in. */
	private DataInputStream in;
	
	/** The out. */
	private DataOutputStream out;
	
	/** The socket. */
	private Socket socket;

	/**
	 * Gets the reader.
	 *
	 * @return the reader
	 */
	public DataInputStream getReader() {
		return in;
	}

	/**
	 * Gets the writer.
	 *
	 * @return the writer
	 */
	public DataOutputStream getWriter() {
		return out;
	}

	/**
	 * Instantiates a new connection.
	 */
	public Connection() {
	}

	/**
	 * Creates the socket.
	 *
	 * @param address the address
	 * @return true, if successful
	 */
	public boolean createSocket(String address) {
		try {
			socket = new Socket(address, Settings.port);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Sets the socket.
	 *
	 * @param _socket the new socket
	 */
	public void setSocket(Socket _socket) {
		socket = _socket;
	}

	/**
	 * Creates the data streams.
	 *
	 * @return true, if successful
	 */
	public boolean createDataStreams() {
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Creates the.
	 *
	 * @param address the address
	 * @return true, if successful
	 */
	public boolean create(String address) {
		return createSocket(address) && createDataStreams();
	}

	/**
	 * Send.
	 *
	 * @param string the string
	 */
	public void send(String string) {
		try {
			out.writeUTF(string);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read.
	 *
	 * @return the string
	 * @throws EOFException the EOF exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String read() throws EOFException, IOException {

		String line = null;
		line = in.readUTF();

		return line;
	}

	/**
	 * Close connection
	 */
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}///!~