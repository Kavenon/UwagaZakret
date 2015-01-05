package uwaga.zakret;

import java.util.ArrayList;

import uwaga.zakret.controller.ConnectFormController;
import uwaga.zakret.controller.Controller;
import uwaga.zakret.controller.PlayController;
import uwaga.zakret.controller.WelcomeController;
import uwaga.zakret.model.Connection;

public class GameEngine {
	
	private ArrayList<Controller> controllers;
	private int currentState;
	
	private Connection conn;

	
	public static final int PLAY = 0;
	public static final int WELCOME = 1;
	public static final int CONNECT_FORM = 2;

	public GameEngine() {
		controllers = new ArrayList<Controller>();
		controllers.add(new PlayController(this));
		controllers.add(new WelcomeController(this));
		controllers.add(new ConnectFormController(this));
		
		conn = Connection.getConnection();
		
		//conn.create("localhost");
		
		
		
		setState(WELCOME);		
	}

	public Connection getConn(){
		return conn;
	}
	public Controller setState(int state) {

		currentState = state;
		controllers.get(currentState).init();
		return controllers.get(currentState);

	}
	
	public void setError(String msg){
		controllers.get(currentState).setError(msg);
	}

	public void update() {

		controllers.get(currentState).update();

	}

	public void draw(java.awt.Graphics2D g) {

		controllers.get(currentState).draw(g);

	}

	public void keyTyped(char k) {

		controllers.get(currentState).keyTyped(k);

	}

	public void keyPressed(int k) {
		
		controllers.get(currentState).keyPressed(k);

	}

	public void keyReleased(int k) {

		controllers.get(currentState).keyReleased(k);

	}

}
