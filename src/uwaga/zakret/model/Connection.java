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

	private static Connection instance;

	public DataInputStream getReader() {
		return in;
	}

	public DataOutputStream getWriter() {
		return out;
	}

	private Connection() {
	}

	public static Connection getConnection() {
		if (instance == null) {
			instance = new Connection();
			return instance;
		}
		return instance;
	}

	public boolean create(String address) {
		try {		
			socket = new Socket(address, Settings.port);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public void send(String string){
		try {
			out.writeUTF(string);
			out.flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}	
	}
	
	public String read() throws EOFException, IOException{
		
		String line = null;	
		line = in.readUTF();
		
		return line;
	}
}