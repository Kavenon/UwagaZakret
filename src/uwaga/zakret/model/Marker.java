package uwaga.zakret.model;

import java.awt.Color;
import java.util.ArrayList;

public class Marker {

	private Position currentPosition;
	private Position previousPosition;
	public Position getPreviousPosition() {
		return previousPosition;
	}
	
	public void setPreviousPosition(Position previousPosition) {
		this.previousPosition = previousPosition;
	}

	public Marker(Position currentPosition, double direction,
			boolean isWriting, Color color, int radius, int velocity) {

		this.currentPosition = currentPosition;
		this.direction = direction;

		this.isWriting = isWriting;
		this.color = color;
		this.radius = radius;
		this.velocity = velocity;
	}

	private Position velocityVector;

	private ArrayList<Position> path;

	private boolean isWriting;

	private Color color;

	private int radius;

	private int velocity;

	private double direction;

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public Position getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

	public ArrayList<Position> getPath() {
		return path;
	}

	public void setPath(Position position) {
		path.add(position);
	}

	public boolean isWriting() {
		return isWriting;
	}

	public void setWriting(boolean isWriting) {
		this.isWriting = isWriting;
	}
	public void toggleWriting(){
		if(isWriting)
			isWriting = false;
		else
			isWriting = true;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Position getVelocityVector() {
		return velocityVector;
	}

	public void setVelocityVector(Position velocityVector) {
		this.velocityVector = velocityVector;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

}
