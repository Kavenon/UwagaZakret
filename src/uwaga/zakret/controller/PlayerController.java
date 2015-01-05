package uwaga.zakret.controller;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;

import uwaga.zakret.GameEngine;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Settings;

public class PlayerController extends Controller {

	private Player player;
	
	private boolean pressed;

	public PlayerController(GameEngine engine) {
		this.engine = engine;
	}

	public PlayerController(){
		
	}
	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void init() {

	}

	public  void update() {

		if (player.getMarkerController().isTurning()) {
			player.getMarkerController().turn();			
		}

		player.getMarkerController().move();

		// engine.getConn().getWriter().println();

	}

	public void draw(Graphics2D g) {

	}

	public void keyPressed(int k) {
		
		if (k == player.getControl().getLeft()) {		
			
			if(!pressed){				
				
				//player.getMarkerController().startTurn(-1 * Settings.turnRadius);
				try {					
					engine.getConn().getWriter().writeUTF("POSITION["+
							-1*Settings.turnRadius+"]");	
					engine.getConn().getWriter().flush();
				}
				catch (IOException e){
					e.printStackTrace();
				}
				
			
			
				pressed = true;
			}
		} else if (k == player.getControl().getRight()) {
			if(!pressed){
				
				player.getMarkerController().startTurn(Settings.turnRadius);
				try {
					engine.getConn().getWriter().writeUTF("POSITION["+
							Settings.turnRadius+"]");	
					
					engine.getConn().getWriter().flush();
				}
				catch (IOException e){
					
				}
				
				pressed = true;
			}
		} else if (k == KeyEvent.VK_SPACE) {
			System.err.println("STARTPRESSED");
			try {
				engine.getConn().getWriter().writeUTF("START");			
				engine.getConn().getWriter().flush();
			}
			catch (IOException e){
				
			}
			
		}
	}

	public void keyReleased(int k) {
	  if(player == null) return ;
		if (k == player.getControl().getLeft()
				|| k == player.getControl().getRight()) {
			pressed = false;
			//player.getMarkerController().stopTurn();
			try {
				engine.getConn().getWriter().writeUTF("POSITION[0.0]");			
				engine.getConn().getWriter().flush();
			}
			catch (IOException e){
				
			}
			//System.err.println("STARTPRESSED");
		
			

			
		}
	}

	public void keyTyped(char k) {
		
	}

}
