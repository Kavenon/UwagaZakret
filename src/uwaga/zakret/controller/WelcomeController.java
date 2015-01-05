package uwaga.zakret.controller;

import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import uwaga.zakret.GameEngine;
import uwaga.zakret.GamePanel;
import uwaga.zakret.view.BackgroundView;

public class WelcomeController extends Controller {
	private BackgroundView backgroundView;
	
	private float alpha = 0f;
	
	private boolean fadeOut;
	
	public WelcomeController(GameEngine engine) {
		this.engine = engine;
	}
	
	public  void init(){
		backgroundView = new BackgroundView("/welcome-background.jpg");
		

	};
	public  void update(){
		
		 if(!fadeOut) alpha += 0.05f;
		 else alpha -= 0.05f;
		 
		 if (alpha >= 1.0f) 
			 alpha = 1.0f;
		 else if(alpha <= 0f)
			 alpha = 0f;
		 
		 if(fadeOut && alpha == 0f){
			 engine.setState(GameEngine.CONNECT_FORM);
		 }
		
	};
	public  void draw(java.awt.Graphics2D g){
		g.clearRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setComposite(AlphaComposite.getInstance(
		          AlphaComposite.SRC_OVER, alpha));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		backgroundView.draw(g);
	};
	public  void keyPressed(int k){
		if(KeyEvent.VK_SPACE == k){
			fadeOut = true;
		}
	};
	public  void keyReleased(int k){
		
	};
	public  void keyTyped(char k){
		
	};
}
