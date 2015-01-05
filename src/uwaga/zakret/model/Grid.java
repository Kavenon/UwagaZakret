package uwaga.zakret.model;

import java.util.ArrayList;
import java.util.List;

public class Grid {

	// The actual grid array


	private List<Position>[][] grid = null;
	private List<Position> retrieveList = new ArrayList<Position>();


	// The rows and columns in the grid
	private int rows, cols;

	// The cell size
	private int cellSize;

	public Grid(int mapWidth, int mapHeight, int cellSize) {
		this.cellSize = cellSize;
		// Calculate rows and cols
		rows = (mapHeight + cellSize - 1) / cellSize;
		cols = (mapWidth + cellSize - 1) / cellSize;
		// Create the grid
	//	grid = new ArrayList<Position>[cols][rows];
	}

	public void clear() {
		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				grid[x][y].clear();
			}
		}
	}

	public void addEntity(Position e) {
		int cellX = (int) e.getX() / cellSize;
		int cellY = (int) e.getY() / cellSize;

		int topLeftX = Math.max(0, (int) e.getX() / cellSize);
		int topLeftY = Math.max(0, (int) e.getY() / cellSize);
		int bottomRightX = Math.min(cols - 1,
				((int) e.getX() + (int) e.getWidth() - 1) / cellSize);
		int bottomRightY = Math.min(rows - 1,
				((int) e.getY() + (int) e.getHeight() - 1) / cellSize);

		for (int x = topLeftX; x <= bottomRightX; x++) {
			for (int y = topLeftY; y <= bottomRightY; y++) {
				grid[x][y].add(e);
			}
		}

	}

	public List<Position> retrieve(Position e) {
		retrieveList.clear();
		// Calculate the positions again
		int topLeftX = Math.max(0, (int) e.getX() / cellSize);
		int topLeftY = Math.max(0, (int) e.getY() / cellSize);
		int bottomRightX = Math.min(cols - 1,
				((int) e.getX() + (int) e.getWidth() - 1) / cellSize);
		int bottomRightY = Math.min(rows - 1,
				((int) e.getY() + (int) e.getHeight() - 1) / cellSize);
		for (int x = topLeftX; x <= bottomRightX; x++) {
			for (int y = topLeftY; y <= bottomRightY; y++) {
				// List<Position> cell = grid[x][y];
				// Add every entity in the cell to the list
				// for (int i=0; i<cell.size(); i++)
				{
					// Position retrieved = cell.get(i);
					// Avoid duplicate entries
					// if (!retrieveList.contains(retrieved))
					// retrieveList.add(retrieved);
				}
			}
		}
		return retrieveList;
	}

}