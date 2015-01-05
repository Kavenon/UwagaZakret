package uwaga.zakret.controller;

import java.awt.Graphics2D;

import uwaga.zakret.GameEngine;
import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.Settings;

public class MarkerController extends Controller {

	private Marker marker;

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	private double turn = 0;

	public MarkerController(GameEngine engine) {
		this.engine = engine;
	}
	public MarkerController() {
		
	}

	public  void init(){};
	public  void update(){};
	public  void draw(Graphics2D g){};
	public  void keyPressed(int k){};
	public  void keyReleased(int k){};
	public  void keyTyped(char k){};
	
	public double getTurn(){
		return turn;
	}
	public void startTurn(double direction) {
		this.turn = direction;
	}

	public void stopTurn() {
		turn = 0;
	}

	public boolean isTurning() {
		return turn != 0;
	}

	public boolean move() {
		//System.err.println(""+marker.getCurrentPosition().getX() + " " + marker.getCurrentPosition().getY() + " " + marker.getDirection());
		marker.setPreviousPosition(marker.getCurrentPosition());
		double x = marker.getCurrentPosition().getX() + 1.2*Math.cos(marker.getDirection());
		double y = marker.getCurrentPosition().getY() + 1.2*Math.sin(marker.getDirection());
		
		Position newPosition = new Position(x,y);
		//if(!newPosition.equals(marker.getCurrentPosition()))
			marker.setCurrentPosition(newPosition);
			
		return true;
	}

	public void turn() {

		marker.setDirection(marker.getDirection() + turn);

	}

}
