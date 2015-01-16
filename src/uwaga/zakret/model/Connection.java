package uwaga.zakret.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class Connection {

	private DataInputStream in;
	private DataOutputStream out;
	private Socket socket;

	public DataInputStream getReader() {
		return in;
	}

	public DataOutputStream getWriter() {
		return out;
	}

	public Connection() {
	}

	public boolean createSocket(String address) {
		try {
			socket = new Socket(address, Settings.port);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public void setSocket(Socket _socket) {
		socket = _socket;
	}

	public boolean createDataStreams() {
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean create(String address) {
		return createSocket(address) && createDataStreams();
	}

	public void send(String string) {
		try {
			out.writeUTF(string);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String read() throws EOFException, IOException {

		String line = null;
		line = in.readUTF();

		return line;
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}