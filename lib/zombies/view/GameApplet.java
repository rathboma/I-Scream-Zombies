package zombies.view;

import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import zombies.controller.GameController;
import zombies.model.GameBoard;
import zombies.model.GameModel;

import game.Platform;
import game.RectThing;
import game.Thing;

public class GameApplet extends Platform implements Observer {
  /**
   * 
   */
  private static final long serialVersionUID = -8728170048736426649L; 
  
  Thing minimap  = new RectThing(420, 10, 175, 175);
  TileView[][] tiles = new TileView[5][5];
  
  public void setup() {
    for(int x = 0; x < 5; x++) {
      for(int y = 0; y < 5; y++) {
        TileView tile = new TileView(x, y, this);
        tiles[x][y] = tile;
        addThing(tile);
        MiniTileView miniTile = new MiniTileView(x, y, this);
        addThing(miniTile);
      }
    }
    minimap.makeStatic();
    addThing(minimap);
    
    GameModel model = GameModel.getInstance();
    model.addObserver(this);
    
    GameController controller = GameController.getInstance();
    controller.getGameState("NCQNWTPPJVBTLJSS");
    //controller.joinGame("Player 1");
  }
  
  public void update(){
  }
  
  public void overlay(Graphics g){
  }

  public void update(Observable arg0, Object arg1) {
    System.out.println("updated");
    if (!(arg1 instanceof GameBoard)) {
      return;
    }
  }
}
