package uwaga.zakret.model;

import uwaga.zakret.controller.MarkerController;

public class Player {

	private MarkerController marker;

	private String username;

	private int points;

	private Control control;

	private boolean ready;

	private boolean alive;

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public Player(String username) {
		this.username = username;
		this.alive = true;
	}

	public Player(MarkerController marker, String username, int points,
			Control control, boolean ready) {

		this.marker = marker;
		this.username = username;
		this.points = points;
		this.control = control;
		this.ready = ready;
		this.alive = true;
	}

	public Control getControl() {
		return control;
	}

	public void setControl(Control control) {
		this.control = control;
	}

	public MarkerController getMarkerController() {
		return marker;
	}

	public void setMarkerController(MarkerController marker) {
		this.marker = marker;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
