package zombies;
import zombies.guts.*;
import game.*;
public class Game{
	public Player player;
	public Player other = null;
	public Tile[][] tiles;
	public double chocolatePrice;
	public double strawberryPrice;
	public double vanillaPrice;
	
	public int chocolateCost;
	public int vanillaCost;
	public int strawberryCost;
	
	public void mergeUpdate(ActionUpdate update){
		player = update.player;
		Platform.platform.removeThing(tiles[update.revealed.tileX][update.revealed.tileY]);
		tiles[update.revealed.tileX][update.revealed.tileY] = update.revealed;
		Tile.visualize(tiles, player.coordinates);
	}
}