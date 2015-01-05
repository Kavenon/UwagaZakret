package uwaga.zakret.server;



import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Player;

public class Listener implements Runnable {

	private Thread thread;
	
	private boolean running;
	private PlayerController s;
	private DataInputStream in;
	private Socket sock;
	public Listener(PlayerController s, Socket sock) {

		this.s = s;
		this.sock = sock;
		thread = new Thread(this);
		thread.start();
	}



	public synchronized void run() {

		running = true;
	
		while (running) {
			try {
				in = new DataInputStream(sock.getInputStream());
			} catch (IOException e1) {
				running = false;
			}
			try {
			String input = in.readUTF();
			//System.out.println(input);
			if (input.startsWith("POSITION")) {
				
				Pattern p = Pattern
						.compile("POSITION\\[(-?\\d+\\.?\\d+)\\]");
				Matcher m = p.matcher(input);
				if (m.find()) {
					Double turn = Double.parseDouble(m.group(1));
					
					System.out.println("T:" + turn);
					// moze byc szybciej
					
					
						Player pl = s.getPlayer();
						
							
							if(turn != 0){
								pl.getMarkerController().startTurn(turn);
							}
							else {
								pl.getMarkerController().stopTurn();
							}
							
							
							

					}
					

				}
			
			}
			catch (IOException e){
				running = false;
			}
		}
	}
}
