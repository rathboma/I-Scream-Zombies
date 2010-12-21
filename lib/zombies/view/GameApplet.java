package zombies.view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import zombies.controller.GameController;
import zombies.model.GameBoard;
import zombies.model.GameModel;
import zombies.model.Tile;

import game.Platform;
import game.RectThing;
import game.Thing;

public class GameApplet extends Platform implements Observer {
  /**
   * 
   */
  private static final long serialVersionUID = -8728170048736426649L; 
  
  Thing minimap  = new RectThing(420, 0, 0, 0);
  TileView[][] tiles = new TileView[5][5];
  MiniTileView[][] miniTiles = new MiniTileView[0][0];
  
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
    minimap.setColor(Color.WHITE);
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
    if (!(arg1 instanceof GameBoard)) {
      return;
    }
    
    GameBoard board = (GameBoard)arg1;
    
    // Clear mini-map
    for (int x = 0; x < miniTiles.length; x++) {
      for (int y = 0; y < miniTiles[x].length; y++) {
        removeThing(miniTiles[x][y]);
      }
    }
    
    // Make new mini-map
    int width = board.getWidth();
    int height = board.getHeight();
    miniTiles = new MiniTileView[width][height];
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        MiniTileView miniTile = new MiniTileView(x, y, this);
        if (board.getTileAt(x, y) != Tile.UnknownTile) {
          miniTile.discover();
        }
        addThing(miniTile);
      }
    }
  }
}
