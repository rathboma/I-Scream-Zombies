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
	private GameApplet gameApplet;
	private int xPos;
	private int yPos;
	
	public TileView(int xPos, int yPos, GameApplet gameApplet){
	  super((xPos*bigTot), yPos*bigTot, bigSize, bigSize);
	  makeStatic();
	  this.gameApplet = gameApplet;
	  this.xPos = xPos;
	  this.yPos = yPos;
	}
	
	public boolean mouseDown(int x, int y) {
    gameApplet.moveToTile(xPos, yPos);
    return false;
 }
	
	public void isOutOfBounds() {
	  setColor(Color.BLACK);
	}
	
	public void isHidden() {
	  setColor(Color.DARK_GRAY);
	}
	
	public void isCurrent() {
	  setColor(Color.WHITE);
	}
	
	public void discover() {
		setColor(Color.lightGray);
	}
}