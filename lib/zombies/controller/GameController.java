package zombies.controller;

import java.io.*;
import java.net.*;

import org.json.*;

import zombies.model.*;
import zombies.model.GameBoard.GameBoardBuilder;
import zombies.model.Tile.TileBuilder;

/**
 * "Controller" object for the I Scream Zombies Game
 * @author Dan L. Dela Rosa
 */
public class GameController {
  static final String serverAddress = "http://iscreamzombies.heroku.com/";
  
  // Make sure the object cannot instantiated externally
  private GameController() {
  }
  // This class is a Singleton
  private static final GameController instance = new GameController();
  // Right now, the controller managers only one game at a time
  private GameModel gameModel = GameModel.getInstance();
  
  /**
   * Returns an instance of GameController
   * @return a GameController
   */
  public static GameController getInstance() {
    return instance;
  }
  
  /**
   * @param requestAddress
   */
  private JSONObject sendGetRequestToServer(String requestAddress) {
    String response = "";
    try {
      URL serverURL = new URL(requestAddress);
      HttpURLConnection connection = 
          (HttpURLConnection) serverURL.openConnection();
      connection.connect();
      BufferedReader is = new BufferedReader(
          new InputStreamReader(connection.getInputStream()));
      String inputLine;
      while ((inputLine = is.readLine()) != null) {
        response = response + inputLine;
      }
      return new JSONObject(response);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return new JSONObject();
  }
    
  public synchronized void joinGame(String name) {
  }

  public synchronized void getTurn() {
    // TODO send request
    // TODO get response
    gameModel.updateYourTurn(false);
  }
  
  public void getGameState(String UUID) {
    String requestAddress = serverAddress + "get_game_state/" + UUID;
    JSONObject response = sendGetRequestToServer(requestAddress);
    System.out.println(response);
    
    try {
      JSONObject gameBoardData = (JSONObject)response.get("game_board");
      
      JSONArray sizeData = (JSONArray)gameBoardData.get("size");
      int width = sizeData.getInt(0);
      int height = sizeData.getInt(1);
      GameBoardBuilder gameBoardBuilder = 
        new GameBoardBuilder(UUID, width, height);
      
      JSONArray knownTileData = (JSONArray)gameBoardData.get("known");
      int knownTileIndex = 0;
      while(!knownTileData.isNull(knownTileIndex)) {
        JSONObject tileData = (JSONObject)knownTileData.get(knownTileIndex);
        System.out.println(tileData);
        
        int tileX = tileData.getInt("x");
        int tileY = tileData.getInt("y");
        boolean isStore = false;
        int zombies = 0;
        try {
          isStore = tileData.getBoolean("store");
          zombies = tileData.getInt("zombies");
        } 
        catch (JSONException e) {
          e.printStackTrace();
        }
        finally {
          TileBuilder tileBuilder = new TileBuilder();
          tileBuilder.isStore(isStore);
          tileBuilder.withZombies(zombies);
          Tile tile = tileBuilder.buildTile();
          gameBoardBuilder.withKnownTile(tileX, tileY, tile);
        }
        knownTileIndex++;
      }
      
      JSONObject playerData = (JSONObject)response.get("players");
      
      JSONObject yourPlayerData = (JSONObject)playerData.get("you");
      System.out.println(yourPlayerData);
      
      JSONArray otherPlayerData = (JSONArray)playerData.get("others");
      System.out.println(otherPlayerData);
      
      JSONObject costData = (JSONObject)response.get("costs");
      System.out.println(costData);
      
      JSONObject basePriceData = (JSONObject)response.get("prices");
      System.out.println(basePriceData);
      
      boolean win = response.getBoolean("win");
      gameBoardBuilder.isWin(win);
      
      boolean isGameOver = response.getBoolean("game_over");
      gameBoardBuilder.isGameOver(isGameOver);
      
      GameBoard gameBoard = gameBoardBuilder.buildGameBoard();
      gameModel.setGameBoard(gameBoard);
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
  }
  
  public synchronized void makeMove(int x, int y) {
    // TODO send request
    // TODO get response
    Tile tile = new Tile.TileBuilder().buildTile();
    gameModel.updateTile(x, y, tile);
    Player player = new Player.PlayerBuilder(x, y).buildPlayer();
    gameModel.updatePlayer(player);
  }
  
  public synchronized void buy(int flavor, int number) {
    // TODO send request
    // TODO get response
    int x = gameModel.getPlayerX();
    int y = gameModel.getPlayerY();
    Tile tile = new Tile.TileBuilder().buildTile();
    gameModel.updateTile(x, y, tile);
    Player player = new Player.PlayerBuilder(x, y).buildPlayer();
    gameModel.updatePlayer(player);
  }
  
  public synchronized void sell(int flavor, int number, int customerId) {
    // TODO send request
    // TODO get response
    int x = gameModel.getPlayerX();
    int y = gameModel.getPlayerY();
    Tile tile = new Tile.TileBuilder().buildTile();
    gameModel.updateTile(x, y, tile);
    Player player = new Player.PlayerBuilder(x, y).buildPlayer();
    gameModel.updatePlayer(player);
  }
  
  public synchronized void kill() {
    // TODO send request
    // TODO get response
    int x = gameModel.getPlayerX();
    int y = gameModel.getPlayerY();
    Tile tile = new Tile.TileBuilder().buildTile();
    gameModel.updateTile(x, y, tile);
    Player player = new Player.PlayerBuilder(x, y).buildPlayer();
    gameModel.updatePlayer(player);
  }
  
  public synchronized void run() {
    // TODO send request
    // TODO get response
    int x = gameModel.getPlayerX();
    int y = gameModel.getPlayerY();
    Tile tile = new Tile.TileBuilder().buildTile();
    gameModel.updateTile(x, y, tile);
    Player player = new Player.PlayerBuilder(x, y).buildPlayer();
    gameModel.updatePlayer(player);
  }
}