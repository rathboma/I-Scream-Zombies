package game;

public class LineSegment {
	public double x1, x2, y1, y2;

	public LineSegment(double[] start, double[] end){
		this.x1 = start[0];
		this.y1 = start[1];
		this.x2 = end[0];
		this.y2 = end[1];
	}
	
	public double[] distanceTo(LineSegment other){ //Only returns parallel line distances
		if (other == null) {
			return new double[] {999,999};
		}
		double[] distance = new double[] {999,999};
		if (isHorizontal() && other.isHorizontal()) {
			if ((x1 >= other.xMin() && x1 <= other.xMax()) || (x2 >= other.xMin() && x2 <= other.xMax()) || (other.x1 >= xMin() && other.x1 <= xMax()) || (other.x2 >= xMin() && other.x2 <= xMax())) {
				distance[1] = y1 - other.y1;
			}
		} else if (isVertical() && other.isVertical()){
			if ((y1 >= other.yMin() && y1 <= other.yMax()) || (y2 >= other.yMin() && y2 <= other.yMax()) || (other.y1 >= yMin() && other.y1 <= yMax()) || (other.y2 >= yMin() && other.y2 <= yMax())) {
				distance[0] = x1 - other.x1;
			}
		}
		return distance;
	}

	public boolean isHorizontal() { return (y1 == y2); }
	public boolean isVertical() { return (x1 == x2); }
	public double xMin() { return Math.min(x1, x2); }
	public double xMax() { return Math.max(x1, x2); }
	public double yMin() { return Math.min(y1, y2); }
	public double yMax() { return Math.max(y1, y2); }
}
