package uwaga.zakret.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

	public boolean create(String address) {
		try {
			String serverAddress = address;
			socket = new Socket(serverAddress, Settings.port);
			in = new DataInputStream(
					socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			return true;
		} catch (IOException e) {
			return false;
			//e.printStackTrace();
		}
	}
}
