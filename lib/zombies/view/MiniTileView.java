package zombies.view;

import java.awt.Color;

import game.RectThing;

/**
 * "Zoomed out" view of a tile
 * TODO extends RectThing
 * @author koto-nakamura
 *
 */
public class MiniTileView extends RectThing {
  static final int smallSize = 5;
  static final int smallGap = 2;
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
