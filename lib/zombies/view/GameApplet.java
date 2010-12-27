package zombies.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;

import zombies.controller.GameController;
import zombies.model.Flavors;
import zombies.model.GameBoard;
import zombies.model.GameModel;
import zombies.model.Player;
import zombies.model.Tile;

import game.PhotoThing;
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
  String[][] tileLabels2 = new String[bigMapWidth][bigMapHeight];
  MiniTileView[][] miniTiles = new MiniTileView[0][0];
  Font font = new Font("Helvetica", Font.BOLD, 14);
  
  Thing background = new PhotoThing(imageHelper("media/images/background.png"), 
      0, 0, 800, 600);
  
  Thing titleScreen = new PhotoThing(imageHelper("media/images/title.jpg"), 
      0, 0, 800, 600);
      
  Thing tutorial = new PhotoThing(
      imageHelper("media/images/instructions.png"), 0, 0, 800, 600) {
    public boolean mouseDown(int x, int y) {
      removeThing(tutorial);
      return false;
    }
  };
  
  Thing tutorialButton = new PhotoThing(
      imageHelper("media/images/buttons/button_instructions.png"), 
      320, 370, 122, 27) {
    public boolean mouseDown(int x, int y) {
      addThing(tutorial);
      return false;
    }
  };
  
  Thing joinButton = new PhotoThing(
      imageHelper("media/images/buttons/button_startgame.png"),
      320, 340, 122, 27) {
    public boolean mouseDown(int x, int y) {
      joinGame();
      removeThing(joinButton);
      removeThing(titleScreen);
      removeThing(tutorialButton);
      return false;
   }
  };
  
  Thing buyButton = new PhotoThing(
      imageHelper("media/images/buttons/button_buy.png"),
      470, 270, 61, 27) {
    public boolean mouseDown(int x, int y) {
      controller.buy(0, 0);
      return false;
   }
  };
  
  Thing sellButton = new PhotoThing(
      imageHelper("media/images/buttons/button_sell.png"),
      470, 300, 61, 27) {
    public boolean mouseDown(int x, int y) {
      controller.sell("", 0, 0);
      return false;
   }
  };
  
  Thing killButton = new PhotoThing(
      imageHelper("media/images/buttons/button_kill.png"),
      470, 330, 61, 27) {
    public boolean mouseDown(int x, int y) {
      controller.kill();
      return false;
   }
  };
  
  Thing runButton = new PhotoThing(
      imageHelper("media/images/buttons/button_run.png"),
      470, 360, 61, 27) {
    public boolean mouseDown(int x, int y) {
      controller.run();
      return false;
   }
  };
  
  Thing mapButton = new PhotoThing(
      imageHelper("media/images/buttons/button_map.png"),
      470, 390, 61, 27) {
    public boolean mouseDown(int x, int y) {
      return false;
    }
  };
  
  String playerLabel1 = "";
  String playerLabel2 = "";
  String playerLabel3 = "";
  String playerLabel4 = "";
  String playerLabel5 = "";
  
  GameModel model = GameModel.getInstance();
  GameController controller = GameController.getInstance();
  
  int refreshTimer = 0;
  
  /**
   * 
   */
  public void setup() {
    addThing(background);
    model.addObserver(this);
    for(int x = 0; x < 5; x++) {
      for(int y = 0; y < 5; y++) {
        TileView tile = new TileView(x, y, this);
        tiles[x][y] = tile;
        tileLabels[x][y] = "";
        tileLabels2[x][y] = "";
        addThing(tile);
        MiniTileView miniTile = new MiniTileView(x, y, this);
        addThing(miniTile);
      }
    }
    
    minimap.makeStatic();
    minimap.setColor(Color.WHITE);
    addThing(minimap);
    
    addThing(buyButton);
    addThing(sellButton);
    addThing(killButton);
    addThing(runButton);
    addThing(mapButton);
    
    addThing(titleScreen);
    addThing(joinButton);
    addThing(tutorialButton);
  }
  
  /**
   * 
   * @param fileName
   * @return
   */
  Image imageHelper(String fileName) {
    BufferedImage img = null;
    try {
        img = ImageIO.read(new File(fileName));
    } catch (IOException e) {
      System.out.println(e);
    }
    return img;
    /*
     * Code below doesn't work because getDocumentBase() returns null for 
     * some reason...
     */
    /* 
    try {
      BufferedImage img;
      URL url = new URL(getDocumentBase(), fileName);
      img = ImageIO.read(url);
      return img;
    } catch (IOException e) {
      System.out.println(e);
      return null;
    }
    */
  }
  
  /**
   * 
   */
  public void update(){
    String uuid = model.getUUID();
    if (uuid != "" && !model.isYourTurn()) {
      if (refreshTimer < 75) {
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
        g.drawString(tileLabels2[x][y], placeX, placeY + 20);
      }
    }
    g.drawString(playerLabel1, 420, 180);
    g.drawString(playerLabel2, 420, 200);
    g.drawString(playerLabel3, 420, 220);
    g.drawString(playerLabel4, 420, 240);
    g.drawString(playerLabel5, 420, 260);
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
          tileLabels2[x][y] = "Zs: " 
            + board.getTileAt(translateX, translateY).getZombies();
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
    playerLabelBuilder.append("Player: " + model.getUUID());
    playerLabel1 = playerLabelBuilder.toString();
    playerLabelBuilder = new StringBuilder();
    playerLabelBuilder.append("Money: " + yourPlayer.getMoney());
    playerLabelBuilder.append(" ");
    playerLabel2 = playerLabelBuilder.toString();
    playerLabelBuilder = new StringBuilder();
    playerLabelBuilder.append("Sales: " + yourPlayer.getSales());
    playerLabelBuilder.append(" ");
    playerLabel3 = playerLabelBuilder.toString();
    playerLabelBuilder = new StringBuilder();
    playerLabelBuilder.append("Zombie Kills: " + yourPlayer.getKills());
    playerLabelBuilder.append(" ");
    playerLabel4 = playerLabelBuilder.toString();
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
    playerLabel5 = playerLabelBuilder.toString();
  }
}
