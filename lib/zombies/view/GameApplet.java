package zombies.view;

import java.awt.Color;
import java.awt.Font;
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
  int bigMapWidth = 5;
  int bigMapHeight = 5;
  TileView[][] tiles = new TileView[bigMapWidth][bigMapHeight];
  int[][] translateX = new int[bigMapWidth][bigMapHeight];
  int[][] translateY = new int[bigMapWidth][bigMapHeight];
  String[][] tileLabels = new String[bigMapWidth][bigMapHeight];
  MiniTileView[][] miniTiles = new MiniTileView[0][0];
  Font font = new Font("Helvetica", Font.BOLD, 14);
  
  public void setup() {
    for(int x = 0; x < 5; x++) {
      for(int y = 0; y < 5; y++) {
        TileView tile = new TileView(x, y, this);
        tiles[x][y] = tile;
        tileLabels[x][y] = "";
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
  
  public void overlay(Graphics g) {
    g.setColor(Color.black);
    g.setFont(font);
    for (int x = 0; x < tileLabels.length; x++) {
      for (int y = 0; y < tileLabels[x].length; y++) {
        int placeX = (int)tiles[x][y].getX() + 10;
        int placeY = (int)tiles[x][y].getY() + 20;
        g.drawString(tileLabels[x][y], placeX, placeY);
      }
    }
  }
  
  public void moveToTile(int x, int y) {
    GameController controller = GameController.getInstance();
    controller.makeMove(translateX[x][y], translateY[x][y]);
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
    
    // clear big map
    for (int x = 0; x < tiles.length; x++) {
      for (int y = 0; y < tiles[x].length; y++) {
        removeThing(tiles[x][y]);
      }
    }
    
    // make new big map
    for (int x = 0; x < tiles.length; x++) {
      for (int y = 0; y < tiles[x].length; y++) {
        TileView tile = new TileView(x, y, this);
        translateX[x][y] = x + board.getYou().getX() - 2;
        translateY[x][y] = y + board.getYou().getY() - 2;
        int translateX = x + board.getYou().getX() - 2;
        int translateY = y + board.getYou().getY() - 2;
        tileLabels[x][y] = "(" + translateX + "," + translateY + ")";
        if (!board.inBounds(translateX, translateY)) {
          tile.isOutOfBounds();
        }
        else if (board.getTileAt(translateX, translateY) == Tile.UnknownTile) {
          tile.isHidden();
        }
        else if (x == 2 && y == 2) {
          tile.isCurrent();
        }
        else {
          tile.discover();
        }
        addThing(tile);
      }
    }
  }
}
