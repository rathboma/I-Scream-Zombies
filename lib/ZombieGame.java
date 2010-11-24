
import zombies.*;
import game.*;
import java.awt.*;
import java.util.*;


public class ZombieGame extends Platform {

// initial version

	//this way you can access the platform from other places!


	
	Tile[][] tiles = new Tile[25][25];
	
	public void setup(){

		Config.minimap  = new RectThing(getWidth() - 210, 10, 200, 200);
		Config.minimap.makeStatic();
		Config.minimap.setColor(Color.gray);
		addThing(Config.minimap);
		
	}
	
	public void update(){
		
		
	}
	
	public void overlay(Graphics g){
		
	}
	
	
}