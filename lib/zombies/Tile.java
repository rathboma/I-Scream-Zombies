package zombies;
import game.*;
import org.json.*;
import java.awt.*;


public class Tile extends RectThing{
	public boolean revealed = false;
	public boolean store = false;
	public Customer[] customers;
	public int zombies;
	public boolean clicked = false;
	public static int width = 45, height = 45;
	public int tileX, tileY;
	
	public static void visualize(double startX, double startY, Tile[][] tiles, Coordinate player){
		//going to display 7 by 7, with the player in the middle. If player closer than 7 to the edge, don't move.
		removeAllTilesFromPlatform(tiles);
		int sx = player.x - 3;
		if (sx < 0) sx = 0;
		if(player.x + 3 > tiles.length) sx = tiles.length - 4;
		
		int sy = player.y - 3;
		if (sy < 0) sy = 0;
		if(player.y + 3 > tiles[0].length) sy = tiles[0].length - 4; // always subtract 1 anyway.
		
		for(int x = sx; x < sx + 7; x++){
			for(int y = sy; y < sy + 7; y++){
				Tile tile = tiles[x][y];
				Platform.platform.addThing(tile);
				tile.setX(startX + (x-sx)*(Tile.width + 2));
				tile.setY(startY + (y-sy)*(Tile.height + 2));
				tile.updateVisuals();
			}
		}
	}
	
	public static Tile selected(Tile[][] tiles){
		for(int x = 0; x < tiles.length; x++)
			for(int y = 0; y < tiles[x].length; y++){
				Tile t = tiles[x][y];
				if(t.clicked){
					t.clicked = false;
					return t;
				}
			}
			return null;
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
	
	
	private static void removeAllTilesFromPlatform(Tile[][] tiles){
		for(int x = 0; x < tiles.length; x++)
			for(int y = 0; y < tiles[x].length; y++)
				Platform.platform.removeThing(tiles[x][y]);
	}
	
	public Tile(){
		super(0, 0, Tile.width, Tile.height);
		this.movable = false;
	}
	public Tile(int x, int y){
		this();
		this.tileX = x;
		this.tileY = y;
	}
	
	public void updateVisuals(){
		this.setColor(revealed ? (store ? Color.yellow : Color.green) : Color.darkGray );
	}
	
	public boolean mouseDown(int x, int y) {
		clicked = true;
    return super.mouseDown(x, y);
  }

	public void highlight(){
		this.setColor(Color.lightGray);
	}
	public void unHighlight(){
		updateVisuals();
	}
  
	
	
	
	
}