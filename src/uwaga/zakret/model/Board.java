package uwaga.zakret.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import uwaga.zakret.controller.PlayerController;

public class Board {

	private ArrayList<PlayerController> players;
	
	private String admin; 
	
	private String winner;
	
	private int remainingPlayers;
	
	private int goal;

	private int x, y, width, height;

	private Player[][] map;
	
	private boolean playing;
	
	public Board(int x, int y, int width, int height) {
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		players = new ArrayList<PlayerController>();		
		this.playing = false;

		// set and clear map
		createMap();
	}
	public Board() {
		players = new ArrayList<PlayerController>();
		this.playing = false;
	}
	
	public Color generateColor(){
		boolean taken = true;
		Color color = null;
		Random gen = new Random();
		
		int delta = 15000; 
		
		while(taken){
			taken = false;
			color = Color.getHSBColor((float) gen.nextFloat(), 1, 1);
			for(PlayerController pcont : players){				
				if(Math.abs(pcont.getPlayer().getMarkerController().getMarker().getColor().getRGB() - color.getRGB()) < delta ){
					taken = true;	
					break;
				}
			}
		}
	
		return color;
	}
	public int[] generatePosition(){
	
		Random generator = new Random();
		
		int minX = (int)( x +  10*x);
		int maxX = (int)(width - 0.3 * width);
		
		int minY = (int)( y + 10*y);
		int maxY = (int)(height - 0.3 * height);
		
		int maxDir = 360;
		int minDir = 0;
		
		int delta = 30;
		
		int nx = 0;
		int ny = 0;
		int ndirection = 0;
		
		boolean taken = true;
						
		while(taken){
			nx = generator.nextInt((maxX - minX) + 1) + minX;
			ny = generator.nextInt((maxY - minY) + 1) + minY;
			ndirection = generator.nextInt((maxDir - minDir) + 1) + minDir;
			taken = false;
			for(PlayerController pcont : players){
				Marker m = pcont.getPlayer().getMarkerController().getMarker();
				Position p = m.getCurrentPosition();
				if(Math.abs(p.getX() - nx) < delta || Math.abs(p.getY() - ny) < delta){
					taken = true;
					break;
				}
			}			
		}
		
		int[] ret = new int[3];
		
		ret[0] = nx;
		ret[1] = ny;
		ret[2] = ndirection;
	
		return ret;		
	}
	
	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}
	
	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
	public int getRemainingPlayers() {
		return remainingPlayers;
	}		

	public void setRemainingPlayers(int i) {		
		remainingPlayers = i;
	}

	public void incRemainingPlayers() {
		remainingPlayers++;		
	}

	public void decRemainingPlayers() {
		remainingPlayers--;
	}

	public Player[][] getMap() {
		return map;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setPlayers(ArrayList<PlayerController> players) {
		this.players = players;
	}

	public void markMap(int x, int y, Player p) {
		if (map != null)
			map[x][y] = p;		
	}
	
	public void setMap(Player[][] map){
		this.map = map;
	}

	public void createMap() {
		int mapX = (int) (x + width)+1;
		int mapY = (int) (y + height) +1;
		map = new Player[mapX][mapY];
		for (int i = 0; i < mapX; i++) {
			for (int j = 0; j < mapY; j++) {
				if (i == (int) x - 1 || j == (int) y - 1 || i == mapX - 1
						|| j == mapY - 1) {
					map[i][j] = new Player("board");
				} else {
					map[i][j] = null;
				}

			}
		}
	}

	
	public ArrayList<PlayerController> getPlayers() {
		return players;
	}

	public void setPlayers(PlayerController player) {
		players.add(player);
	}

	public int getGoal() {
		return goal;
	}

	public void setGoal(int goal) {
		this.goal = goal;
	}

}
