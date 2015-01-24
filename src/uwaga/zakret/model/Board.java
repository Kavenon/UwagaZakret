//:uwaga.zakret.model.Board.java
package uwaga.zakret.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import uwaga.zakret.controller.PlayerController;

/**
 * Board model
 */
public class Board {

	/** The players playing. */
	private ArrayList<PlayerController> players;

	/** The admin of board. */
	private String admin;

	/** The winner. */
	private String winner;

	/** The remaining players. */
	private int remainingPlayers;

	/** The goal. */
	private int goal;

	/** The height. */
	private int x, y, width, height;

	/** The map of taken fields. */
	private Player[][] map;

	/** The playing state. */
	private boolean playing;

	/** The clear state. */
	private boolean clear;

	/**
	 * Checks if is clear.
	 *
	 * @return true, if is clear
	 */
	public boolean isClear() {
		return clear;
	}

	/**
	 * Sets the clear.
	 *
	 * @param b the new clear
	 */
	public void setClear(boolean b) {
		clear = b;
	}

	/**
	 * Instantiates a new board.
	 *
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 */
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

	/**
	 * Instantiates a new board.
	 */
	public Board() {
		players = new ArrayList<PlayerController>();
		this.playing = false;
	}

	/**
	 * Generate random color for player
	 *
	 * @return the color
	 */
	public Color generateColor() {
		boolean taken = true;
		Color color = null;
		Random gen = new Random();

		int delta = 15000;

		while (taken) {
			taken = false;
			color = Color.getHSBColor((float) gen.nextFloat(), 1, 1);
			for (PlayerController pcont : players) {
				if (Math.abs(pcont.getPlayer().getMarkerController()
						.getMarker().getColor().getRGB()
						- color.getRGB()) < delta) {
					taken = true;
					break;
				}
			}
		}

		return color;
	}

	/**
	 * Generate random start position
	 *
	 * @return the int[]
	 */
	public int[] generatePosition() {

		Random generator = new Random();

		int minX = (int) (x + 10 * x);
		int maxX = (int) (width - 0.3 * width);

		int minY = (int) (y + 10 * y);
		int maxY = (int) (height - 0.3 * height);

		int maxDir = 360;
		int minDir = 0;

		int delta = 30;

		int nx = 0;
		int ny = 0;
		int ndirection = 0;

		boolean taken = true;

		while (taken) {
			nx = generator.nextInt((maxX - minX) + 1) + minX;
			ny = generator.nextInt((maxY - minY) + 1) + minY;
			ndirection = generator.nextInt((maxDir - minDir) + 1) + minDir;
			taken = false;
			for (PlayerController pcont : players) {
				Marker m = pcont.getPlayer().getMarkerController().getMarker();
				Position p = m.getCurrentPosition();
				if (Math.abs(p.getX() - nx) < delta
						|| Math.abs(p.getY() - ny) < delta) {
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

	/**
	 * Gets the winner.
	 *
	 * @return the winner
	 */
	public String getWinner() {
		return winner;
	}

	/**
	 * Sets the winner.
	 *
	 * @param winner the new winner
	 */
	public void setWinner(String winner) {
		this.winner = winner;
	}

	/**
	 * Gets the admin.
	 *
	 * @return the admin
	 */
	public String getAdmin() {
		return admin;
	}

	/**
	 * Sets the admin.
	 *
	 * @param admin the new admin
	 */
	public void setAdmin(String admin) {
		this.admin = admin;
	}

	/**
	 * Gets the remaining players.
	 *
	 * @return the remaining players
	 */
	public int getRemainingPlayers() {
		return remainingPlayers;
	}

	/**
	 * Sets the remaining players.
	 *
	 * @param i the new remaining players
	 */
	public void setRemainingPlayers(int i) {
		remainingPlayers = i;
	}

	/**
	 * Inc remaining players.
	 */
	public void incRemainingPlayers() {
		remainingPlayers++;
	}

	/**
	 * Dec remaining players.
	 */
	public void decRemainingPlayers() {
		remainingPlayers--;
	}

	/**
	 * Gets the map.
	 *
	 * @return the map
	 */
	public Player[][] getMap() {
		return map;
	}

	/**
	 * Checks if is playing.
	 *
	 * @return true, if is playing
	 */
	public boolean isPlaying() {
		return playing;
	}

	/**
	 * Sets the playing.
	 *
	 * @param playing the new playing
	 */
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Sets the players.
	 *
	 * @param players the new players
	 */
	public void setPlayers(ArrayList<PlayerController> players) {
		this.players = players;
	}

	/**
	 * Mark map.
	 *
	 * @param x the x
	 * @param y the y
	 * @param p the p
	 */
	public void markMap(int x, int y, Player p) {
		if (map != null)
			map[x][y] = p;
	}

	/**
	 * Sets the map.
	 *
	 * @param map the new map
	 */
	public void setMap(Player[][] map) {
		this.map = map;
	}

	/**
	 * Creates map, and mark board border.
	 */
	public void createMap() {
		int mapX = (int) (x + width) + 1;
		int mapY = (int) (y + height) + 1;
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

	/**
	 * Gets the players.
	 *
	 * @return the players
	 */
	public ArrayList<PlayerController> getPlayers() {
		return players;
	}

	/**
	 * Sets the players.
	 *
	 * @param player the new players
	 */
	public void setPlayers(PlayerController player) {
		players.add(player);
	}

	/**
	 * Gets the goal.
	 *
	 * @return the goal
	 */
	public int getGoal() {
		return goal;
	}

	/**
	 * Sets the goal.
	 *
	 * @param goal the new goal
	 */
	public void setGoal(int goal) {
		this.goal = goal;
	}

}///!~
