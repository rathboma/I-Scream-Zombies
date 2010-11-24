package zombies;

import game.*;
import java.awt.*;
import java.util.*;


public class Tile{
	
	int bigSize = 40;
	int smallSize = 4;
	int bigGap = 8;
	int smallGap = 1;
	
	public int card;
	public Zombie[] zombies;
	public Customer[] customers;
	ModifierBase modifier = null;
	
	
	public RectThing bigTile;
	public RectThing smallTile;
	
	public Tile(int xPos, int yPos){
		
		Coordinate miniStart = Config.minimap.topLeft();
		
		modifier = Randomizer.getModifier();
		if(modifier == null){
			customers = Randomizer.getCustomers(); //totally random
			zombies = Randomizer.getZombies(customers); //may be based on the number of people
		}
		
	}

	
}
