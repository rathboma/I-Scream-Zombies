package game;

import java.awt.Graphics;
import java.awt.Image;

public class PhotoThing extends Thing {
  private final Image img;
  private final int width;
  private final int height;
  
  public PhotoThing(Image img, double x, double y, int width, int height) {
    this.x = x;
    this.y = y;
    this.img = img;
    this.width = width;
    this.height = height;
    
    X[0] = x;
    Y[0] = y;
    X[1] = x + width;
    Y[1] = y;
    X[2] = x + width;
    Y[2] = y + height;
    X[3] = x;
    Y[3] = y + height;
    setShape(X, Y, 4);
  }
  
  public void update(Graphics g) {
    g.drawImage(img, (int)x, (int)y, width, height, null);
  }
}
