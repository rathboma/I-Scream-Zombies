package zombies;
import zombies.guts.*;
public class Game{
	public Player player;
	public Player other = null;
	public Tile[][] tiles;
	
	public void mergeUpdate(ActionUpdate update){
		player = update.player;
		tiles[update.revealed.tileX][update.revealed.tileY] = update.revealed;
	}
}