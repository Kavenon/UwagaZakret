package uwaga.zakret.controller;

import uwaga.zakret.GameEngine;

public abstract class Controller {

	protected GameEngine engine;
	
	protected String error; 
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(java.awt.Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	public abstract void keyTyped(char k);
	
	public abstract void setError(String msg);
	
}
