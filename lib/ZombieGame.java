
import zombies.*;
import game.*;
import java.awt.*;


public class ZombieGame extends Platform {
  // initial version
  //this way you can access the platform from other places!
  
  /**
   * 
   */
  private static final long serialVersionUID = -8728170048736426649L;
  Tile[][] tiles = new Tile[25][25];
	
  public void setup() {
    Config.platform = this;
    Config.minimap  = new RectThing(getWidth() - 210, 10, 175, 175);
    Config.minimap.makeStatic();
    Config.minimap.setColor(Color.black);
    addThing(Config.minimap);
    for(int x = 0; x < 25; x++) {
      for(int y = 0; y < 25; y++) {
        tiles[x][y] = new Tile(x, y);
      }
    }
		
		Player player = Config.player = new Player();
		player.setPosition(13, 13);
		//Tile tile = tiles[13][13];
		tiles[13][13].discover();
		setFocus(13, 13); //sets the camera focus on the central tile.
	}
	
	private void setFocus(int x, int y){
		
		//do stuff here.
	}
	
	public void update(){
		
		
	}
	
	public void overlay(Graphics g){
		
	}
	
	
}