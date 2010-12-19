package zombies.controller;

import java.io.*;
import java.net.*;

import org.json.*;

import zombies.model.*;

/**
 * "Controller" object for the I Scream Zombies Game
 * @author Dan L. Dela Rosa
 */
public class GameController {
  private HttpURLConnection connection;
  static final String serverAddress = "http://iscreamzombies.heroku.com/";
  private URL serverURL;
  
  // Make sure the object cannot instantiated externally
  private GameController() {
    try {
      serverURL = new URL(serverAddress);
      connection = (HttpURLConnection) serverURL.openConnection();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
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
   * 
   */
  private void setPostMode() {
    try {
      connection.setRequestMethod("POST");
    } catch (ProtocolException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 
   */
  private void setGetMode() {
    try {
      connection.setRequestMethod("GET");
    } catch (ProtocolException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * @param object
   */
  private JSONObject sendJSONObject(JSONObject object) {
    String response = "";
    try {
      OutputStream os = connection.getOutputStream();
      BufferedReader is = new BufferedReader(
          new InputStreamReader(connection.getInputStream()));
      os.write(object.toString().getBytes());
      
      String inputLine;
      while ((inputLine = is.readLine()) != null) {
        System.out.println(inputLine);
        response = response.concat(inputLine);
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
    try {
      return new JSONObject(response);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return new JSONObject();
  }
  
  public synchronized void joinGame(String name) {
    JSONObject object = new JSONObject();
    try {
      object.put("name", name);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    setPostMode();
    JSONObject response = sendJSONObject(object);
    try {
      String uuid = response.getString("uuid");
      gameModel.setUUID(uuid);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public synchronized void getTurn() {
    setGetMode();
    // TODO send request
    // TODO get response
    gameModel.updateYourTurn(false);
  }
  
  public synchronized void getGameState() {
    // TODO send request
    // TODO get response
    GameBoard gameBoard = 
        new GameBoard.GameBoardBuilder("", 0, 0).buildGameBoard();
    gameModel.setGameBoard(gameBoard);
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