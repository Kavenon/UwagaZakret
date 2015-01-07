package uwaga.zakret.model;

public class Position {

	private double x;
	private double y;

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
	

	public boolean equals(Object other) {
		if (!(other instanceof Position)) {
			return false;
		}

		Position that = (Position) other;

		return (int) x == (int) that.x && (int) y == (int) that.y;
	}
	@Override
	public int hashCode() {
	    int hash = 7;
	    hash = 71 * hash + (int) x;
	    hash = 71 * hash + (int) y;
	    return hash;
	}

}
