package zombies.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import zombies.controller.GameController;
import zombies.model.Flavors;
import zombies.model.GameBoard;
import zombies.model.GameModel;
import zombies.model.Player;
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
  
  Thing joinButton = new RectThing(0, 420, 40, 20) {
    public boolean mouseDown(int x, int y) {
      joinGame();
      return false;
   }
  };
  
  Thing buyButton = new RectThing(50, 420, 40, 20) {
    public boolean mouseDown(int x, int y) {
      controller.buy(0, 0);
      return false;
   }
  };
  
  Thing sellButton = new RectThing(100, 420, 40, 20) {
    public boolean mouseDown(int x, int y) {
      controller.sell("", 0, 0);
      return false;
   }
  };
  
  Thing killButton = new RectThing(150, 420, 40, 20) {
    public boolean mouseDown(int x, int y) {
      controller.kill();
      return false;
   }
  };
  
  Thing runButton = new RectThing(200, 420, 40, 20) {
    public boolean mouseDown(int x, int y) {
      controller.run();
      return false;
   }
  };
  
  String playerLabel1 = "";
  String playerLabel2 = "";
  
  GameModel model = GameModel.getInstance();
  GameController controller = GameController.getInstance();
  
  int refreshTimer = 0;
  
  
  
  /**
   * 
   */
  public void setup() {
    model.addObserver(this);
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
    
    joinButton.setColor(Color.WHITE);
    addThing(joinButton);
    
    buyButton.setColor(Color.WHITE);
    addThing(buyButton);
    
    sellButton.setColor(Color.WHITE);
    addThing(sellButton);
    
    killButton.setColor(Color.WHITE);
    addThing(killButton);
    
    runButton.setColor(Color.WHITE);
    addThing(runButton);
  }
  
  /**
   * 
   */
  public void update(){
    String uuid = model.getUUID();
    if (uuid != "" && !model.isYourTurn()) {
      if (refreshTimer < 300) {
        refreshTimer++;
      }
      else {
        controller.getTurn(uuid);
        refreshTimer = 0;
      }
    }
  }
  
  /**
   * 
   */
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
    g.drawString("Join", (int)joinButton.getX() + 5, 
        (int)joinButton.getY() + 15);
    g.drawString("Buy", (int)buyButton.getX() + 5, 
            (int)buyButton.getY() + 15);
    g.drawString("Sell", (int)sellButton.getX() + 5, 
            (int)sellButton.getY() + 15);
    g.drawString("Kill", (int)killButton.getX() + 5, 
            (int)killButton.getY() + 15);
    g.drawString("Run", (int)runButton.getX() + 5, 
            (int)runButton.getY() + 15);
    g.drawString(playerLabel1, 10, 460);
    g.drawString(playerLabel2, 10, 480);
  }
  
  /**
   * 
   */
  public void joinGame() {
    if (model.getUUID() != "") {
      System.out.println("Already in a game");
      return;
    }
    controller.joinGame("Player 1");
    String uuid = model.getUUID();
    System.out.println("Joined game: " + uuid);
    controller.getGameState(uuid);
  }
  
  /**
   * 
   * @param x
   * @param y
   */
  public void moveToTile(int x, int y) {
    GameController controller = GameController.getInstance();
    controller.makeMove(translateX[x][y], translateY[x][y]);
  }
  
  /**
   * 
   */
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
    Player yourPlayer = board.getYou();
    StringBuilder playerLabelBuilder = new StringBuilder();
    playerLabelBuilder.append("Money: " + yourPlayer.getMoney());
    playerLabelBuilder.append(" ");
    playerLabelBuilder.append("Sales: " + yourPlayer.getSales());
    playerLabelBuilder.append(" ");
    playerLabelBuilder.append("Zombie Kills: " + yourPlayer.getKills());
    playerLabelBuilder.append(" ");
    playerLabel1 = playerLabelBuilder.toString();
    playerLabelBuilder = new StringBuilder();
    playerLabelBuilder.append("C: " 
            + yourPlayer.getInstancesOf(Flavors.CHOCOLATE));
    playerLabelBuilder.append(" ");
    playerLabelBuilder.append("S: " 
            + yourPlayer.getInstancesOf(Flavors.STRAWBERRY));
    playerLabelBuilder.append(" ");
    playerLabelBuilder.append("V: " 
            + yourPlayer.getInstancesOf(Flavors.VANILLA));
    playerLabelBuilder.append(" ");
    playerLabel2 = playerLabelBuilder.toString();
    
    // Show whether it is your turn
    if (!board.isYourTurn()) {
      buyButton.setColor(Color.LIGHT_GRAY);
      sellButton.setColor(Color.LIGHT_GRAY);
      killButton.setColor(Color.LIGHT_GRAY);
      runButton.setColor(Color.LIGHT_GRAY);
    }
    else {
      buyButton.setColor(Color.WHITE);
      sellButton.setColor(Color.WHITE);
      killButton.setColor(Color.WHITE);
      runButton.setColor(Color.WHITE);
    }
  }
}
