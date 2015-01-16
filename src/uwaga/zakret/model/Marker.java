package uwaga.zakret.model;

import java.awt.Color;

public class Marker {

	private Position currentPosition;

	private Position previousPosition;

	private boolean isWriting;

	private Color color;

	private int radius;

	private double direction;

	private long lastTimeToggle;

	public Position getPreviousPosition() {
		return previousPosition;
	}

	public void setPreviousPosition(Position previousPosition) {
		this.previousPosition = previousPosition;
	}

	public Marker() {
	}

	public Position getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

	public boolean isWriting() {
		return isWriting;
	}

	public void setWriting(boolean isWriting) {
		this.isWriting = isWriting;
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

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public long getLastTimeToggle() {
		return lastTimeToggle;
	}

	public void setLastTimeToggle(long lastTimeToggle) {
		this.lastTimeToggle = lastTimeToggle;
	}

}
