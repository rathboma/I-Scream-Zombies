package zombies;
import game.*;
import org.json.*;
import java.awt.*;


public class Tile extends RectThing{
	public boolean revealed = false;
	public boolean store = false;
	public Customer[] customers;
	public int zombies = 0;
	public boolean clicked = false;
	public static int width = 20, height = 20;
	private static DiskThing p1, p2;
	public int tileX, tileY;
	public static Tile highlighted = null;
	public static void visualize(Tile[][] tiles, Coordinate player){
		double startX = 5,  startY = 5;
		
		removeAllTilesFromPlatform(tiles);
		Platform.platform.removeThing(p1);
		Platform.platform.removeThing(p2);
		p1 = null;
		p2 = null;
		for(int x = 0; x < tiles.length; x++){
			for(int y = 0; y < tiles[x].length; y++){
				Tile tile = tiles[x][y];
				tile.updateVisuals();
				Platform.platform.addThing(tile);
				tile.setX(startX + getX(x));
				tile.setY(startY + getY(y));
			}
		}
		
		
	}
	private static int getX(int x){
		return x*(Tile.width + 2);
	}
	private static int getY(int y){
		return y*(Tile.height + 2);
	}
	
	public static void showPlayers(Coordinate player1, Coordinate player2 ){
		if(p1 == null) {
			p1 = new DiskThing(0, 0, 10, 10);
			p1.setColor(Color.green);			
			Platform.platform.addThing(p1);
			}
		if(p2 == null){
			p2 = new DiskThing(0, 0, 10, 10);
			p2.setColor(Color.red);			
			Platform.platform.addThing(p2);
		}		
		p1.setX(getX(player1.x) + 10);
		p1.setY(getY(player1.y) + 10);
		p2.setX(getX(player2.x) + 15);
		p2.setY(getY(player2.y) + 15);
	}
	
	public static Tile fromJSON(JSONObject obj) throws JSONException{
		Tile result = new Tile();
		result.revealed = true;
		result.store = obj.getBoolean("store");
		result.zombies = obj.getInt("zombies");
		result.tileX = obj.getInt("x");
		result.tileY = obj.getInt("y");
		JSONArray customers = obj.getJSONArray("customers");
		result.customers = new Customer[customers.length()];
		for(int i = 0; i < customers.length(); i++){
			result.customers[i] = Customer.fromJSON(customers.getJSONObject(i));
		}
		return result;
	}
	
	
	public static void removeAllTilesFromPlatform(Tile[][] tiles){
		for(int x = 0; x < tiles.length; x++)
			for(int y = 0; y < tiles[x].length; y++)
				Platform.platform.removeThing(tiles[x][y]);
	}
	
	public Tile(){
		super(0, 0, Tile.width, Tile.height);
		this.movable = false;
		customers = new Customer[0];
	}
	public Tile(int x, int y){
		this();
		this.tileX = x;
		this.tileY = y;
	}
	
	public void merge(Tile other){
		customers = other.customers;
		zombies = other.zombies;
		store = other.store;
	}
	
	public void updateVisuals(){
		this.setColor(revealed ? (store ? Color.yellow : Color.green) : Color.darkGray );
	}
	
	public boolean mouseDown(int x, int y) {
		clicked = true;
		System.out.println("tile clicked: " + tileX + ", " + tileY + " zombies: " + zombies + " customers: " + customers.length);
		if(Tile.highlighted != null) Tile.highlighted.updateVisuals();
		Tile.highlighted = this;
		highlight();
    return super.mouseDown(x, y);
  }

	public void highlight(){
		this.setColor(Color.lightGray);
	}
  
	
	
	
	
}