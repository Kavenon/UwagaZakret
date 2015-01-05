package uwaga.zakret.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import uwaga.zakret.controller.PlayerController;

public class Board {

	private ArrayList<PlayerController> players;
	
	private String admin; 
	
	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	private String winner;

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	private int remainingPlayers;

	public int getRemainingPlayers() {
		return remainingPlayers;
	}
	
	public Color generateColor(){
		boolean taken = true;
		Color color = null;
		Random gen = new Random();
		while(taken){
			taken = false;
			color = Color.getHSBColor((float) gen.nextFloat(), 1, 1);
			for(PlayerController pcont : players){				
				if(pcont.getPlayer().getMarkerController().getMarker().getColor().getRGB() == color.getRGB() ){
					taken = true;	
					break;
				}
			}
		}
	
		return color;
	}
	public int[] generatePosition(){
		boolean loop = true;
		Random generator = new Random();
		
		int minX = (int)( x +  10*x);
		int maxX = (int)(width - 0.3 * width);
		
		int minY = (int)( y + 10*y);
		int maxY = (int)(height - 0.3 * height);
		
		System.out.print("("+minX+","+maxX+")");
		System.out.println("("+minY+","+maxY+")");
		int maxDir = 360;
		int minDir = 0;
		
		int delta = 10;
		
		boolean taken = true;
		int nx = 0;
		int ny = 0;
		int ndirection =0;
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
		System.out.println(ret);
		return ret;
		
	}

	public void setRemainingPlayers(int i) {
		//remainingPlayers = 10;
		remainingPlayers = i;
	}

	public void incRemainingPlayers() {
		remainingPlayers++;
		//remainingPlayers = 10;
	}

	public void decRemainingPlayers() {
		remainingPlayers--;
	}

	private int goal;

	private double x, y, width, height;

	private Player[][] map;

	public Player[][] getMap() {
		return map;
	}

	public void setMap(Player[][] map) {
		this.map = map;
	}

	private Color color;

	private boolean playing;

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setPlayers(ArrayList<PlayerController> players) {
		this.players = players;
	}

	public Board(double x, double y, double width, double height) {
		players = new ArrayList<PlayerController>();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.playing = false;

		// set and clear map
		createMap();
	}

	public void markMap(int x, int y, Player p) {
		if (map != null)
			map[x][y] = p;
		
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

	public Board() {
		players = new ArrayList<PlayerController>();
		this.playing = false;
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
