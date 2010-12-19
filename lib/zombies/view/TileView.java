package zombies.view;

import java.awt.Color;

import game.*;

/**
 * "Zoomed in" view of a tile
 * TODO extends RectThing
 * @author Dan L. Dela Rosa
 */
public class TileView extends RectThing {
	static final int bigSize = 75;
	static final int bigGap = 10;
	static final int bigTot = bigSize + bigGap;
	
	public TileView(int xPos, int yPos, GameApplet gameApplet){
	  super((xPos*bigTot), yPos*bigTot, bigSize, bigSize);
	  makeStatic();
	}
	
	public void discover(){
		setColor(Color.lightGray);
	}
}