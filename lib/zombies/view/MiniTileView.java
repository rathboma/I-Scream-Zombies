package zombies.view;

import java.awt.Color;

import game.RectThing;

/**
 * "Zoomed out" view of a tile
 * @author Dan L. Dela Rosa
 */
public class MiniTileView extends RectThing {
  static final int smallSize = 5;
  static final int smallGap = 0;
  static final int smallTot = smallSize + smallGap;

  public MiniTileView (int xPos, int yPos, GameApplet gameApplet) {
    super(gameApplet.minimap.getX() + (xPos*smallTot), 
        gameApplet.minimap.getY() + (yPos*smallTot), smallSize, smallSize);
    makeStatic();
  }
  
  public void discover(){
    setColor(Color.gray);
  }
}
