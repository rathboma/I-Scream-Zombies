package zombies.view;

import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import zombies.model.GameBoard;
import zombies.model.GameModel;

import game.Platform;

public class GameApplet extends Platform implements Observer {
  /**
   * 
   */
  private static final long serialVersionUID = -8728170048736426649L; 
  
  //initial version
  //this way you can access the platform from other places!
  TileView[][] tiles = new TileView[5][5];
  
  public void setup() {
    GameModel model = GameModel.getInstance();
    model.addObserver(this);
    
    // TODO rework this code
    //Config.platform = this;
    //Config.minimap  = new RectThing(getWidth() - 210, 10, 175, 175);
    //Config.minimap.makeStatic();
    //Config.minimap.setColor(Color.black);
    //addThing(Config.minimap);
    for(int x = 0; x < 5; x++) {
      for(int y = 0; y < 5; y++) {
        tiles[x][y] = new TileView(x, y);
      }
    }
    
    tiles[2][2].discover();
    setFocus(2, 2);
  }
  
  private void setFocus(int x, int y){

  }
  
  public void update(){
    
    
  }
  
  public void overlay(Graphics g){
    
  }

  public void update(Observable arg0, Object arg1) {
    if (!(arg1 instanceof GameBoard)) {
      return;
    }
    // TODO grab changes
  }
}
