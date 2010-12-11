package zombies;

import game.*;
import java.awt.*;

public class Tile{
	
	int bigSize = 75;
	int smallSize = 5;
	int bigGap = 10;
	int smallGap = 2;
	
	int smallTot = smallSize + smallGap;
	int bigTot = bigSize + bigGap;
	
	public int card;
	//public Zombie[] zombies = null;
	public Customer[] customers = null;
	ModifierBase modifier = null;
	
	
	public RectThing bigTile;
	public RectThing smallTile;
	
	public Tile(int xPos, int yPos){
		
		Coordinate miniStart = Config.minimap.topLeft();
		
		bigTile = new RectThing((xPos*bigTot), yPos*bigTot, bigSize, bigSize);
		bigTile.makeStatic();
		Config.platform.addThing(bigTile);
				
		smallTile = new RectThing((xPos*smallTot) + miniStart.x, miniStart.y + (yPos*smallTot), smallSize, smallSize);
		smallTile.makeStatic();
		Config.platform.addThing(smallTile);
		

		
		
		modifier = Randomizer.getModifier();
		if(modifier == null){
			customers = Randomizer.getCustomers(); //totally random
			//zombies = Randomizer.getZombies(customers); //may be based on the number of people
		}
		
			
	}
	
	public void discover(){
		smallTile.setColor(Color.gray);
		bigTile.setColor(Color.lightGray);
	}

	
}
