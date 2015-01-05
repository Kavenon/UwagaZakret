package uwaga.zakret.controller;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import uwaga.zakret.GameEngine;
import uwaga.zakret.GamePanel;
import uwaga.zakret.view.BackgroundView;

public class ConnectFormController extends Controller {
	private BackgroundView backgroundView;
	
	private float alpha = 0f;
	
	private String ip = "";
	
	private boolean fadeOut;
	
	private boolean enterPressed;
	private boolean readyToMove;
	
	private boolean cleared;
	
	private String error;
	
	public ConnectFormController(GameEngine engine) {
		this.engine = engine;
	}
	
	public  void init(){
		backgroundView = new BackgroundView("/connectform-background.jpg");
		

	};
	public  void update(){		
		//if(readyToMove && cleared){
		if(enterPressed && !readyToMove){			
			if(engine.getConn().create(ip)){
				readyToMove = true;				
			}
			else {
				error = "Nie mo¿na po³¹czyæ z serwerem";
			}			
			enterPressed = false;
		}
		
		if(cleared){
			engine.setState(GameEngine.PLAY);
		}
		
	};
	public  void draw(java.awt.Graphics2D g){
		
		g.setComposite(AlphaComposite.getInstance(
		          AlphaComposite.SRC_OVER, 1));
		
		g.clearRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		if(!readyToMove){
			backgroundView.draw(g);
			if(error != null){
				g.setColor(Color.red);
			
				g.setFont(new Font("Arial", Font.BOLD, 24));
				g.drawString(error, 140, 233);	
			}
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.BOLD, 24));
			g.drawString(ip, 176, 273);			
		}
		else {
			g.clearRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			cleared = true;
		}
	};
	public  void keyPressed(int k){
		if(k == KeyEvent.VK_BACK_SPACE){
			if(ip.length() > 0)
			ip = ip.substring(0, ip.length()-1);
			
		}		
		if(k== KeyEvent.VK_ENTER && !readyToMove){
			enterPressed = true;			
		}
	};
	public  void keyReleased(int k){
		
	};
	public  void keyTyped(char k){
		if(ip.length() < 15)
		ip = (ip + k).trim();
	};
}
