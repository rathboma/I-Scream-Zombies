package zombies;


public class Coordinate{
	public int x, y;
	public Coordinate(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Coordinate copy(){
		return new Coordinate(this.x, this.y);
	}
	
	public boolean nextTo(Coordinate other){
		int diffA = Math.abs(other.x - this.x);
		int diffB = Math.abs(other.y - this.y);
		return diffA + diffB == 1; // 1 tile away
	}
	
}