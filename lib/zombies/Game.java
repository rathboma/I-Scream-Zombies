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
	public boolean gameOver;
	public boolean win;
	public int chocolateCost;
	public int vanillaCost;
	public int strawberryCost;
	
	public Tile playerTile(){
		return tiles[player.coordinates.x][player.coordinates.y];
	}
	
	
	public void mergeUpdate(ActionUpdate update){
		player = update.player;
		tiles[update.revealed.tileX][update.revealed.tileY].merge(update.revealed);
	}
}