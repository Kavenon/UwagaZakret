package uwaga.zakret.model;

public class Position {

	private double x;
	private double y;
	private double width;
	private double height;
	
	private Player occupiedBy;
	
	public boolean equals(Object other) {
		if (!(other instanceof Position)) {
	        return false;
	    }

		Position that = (Position) other;

	    return (int) x == (int) that.x  && (int) y == (int) that.y;
	}
	
	public Player getOccupiedBy() {
		return occupiedBy;
	}

	public void setOccupiedBy(Player occupiedBy) {
		this.occupiedBy = occupiedBy;
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


	public Position(double _x, double _y) {
		x = _x;
		y = _y;
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

}
