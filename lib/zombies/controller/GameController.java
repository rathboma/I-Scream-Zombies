package zombies.controller;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import org.json.*;

import zombies.model.Flavors;
import zombies.model.GameBoard;
import zombies.model.GameBoard.GameBoardBuilder;
import zombies.model.GameModel;
import zombies.model.Player;
import zombies.model.Player.PlayerBuilder;
import zombies.model.Tile;
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
  
  /**
   * @param requestAddress
   */
  private JSONObject sendPostRequestToServer(String requestAddress) {
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
  
  /**
   * 
   * @param tileData
   * @return
   * @throws JSONException
   */
  private Tile processTile(JSONObject tileData) throws JSONException {
    boolean isStore = tileData.getBoolean("store");
    
    int zombies;
    if (tileData.isNull("zombies")) {
      System.out.println("Warning: zombies is null");
      zombies = 0;
    }
    else {
      zombies = tileData.getInt("zombies");
    }
    
    JSONArray customersData = (JSONArray)tileData.get("customers");
    int customerIndex = 0;
    while (!customersData.isNull(customerIndex)) {
      // TODO get customer
    }
    
    TileBuilder tileBuilder = new TileBuilder();
    tileBuilder.isStore(isStore);
    tileBuilder.withZombies(zombies);
    Tile tile = tileBuilder.buildTile();
    return tile;
  }
  
  /**
   * 
   * @param playerData
   * @return
   * @throws JSONException
   */
  private Player parsePlayerData(JSONObject playerData) throws JSONException {
    int playerX = playerData.getInt("x");
    int playerY = playerData.getInt("y");
    PlayerBuilder builder = new PlayerBuilder(playerX, playerY);
    
    boolean canMove = false;
    if (playerData.isNull("can_move")) {
      System.out.println("Warning: can_move is null");
    }
    else {
      canMove = playerData.getBoolean("can_move");
    }
    builder.canMove(canMove);
    
    boolean canAct = false;
    if (playerData.isNull("can_act")) {
      System.out.println("Warning: can_act is null");
    }
    else {
      canAct = playerData.getBoolean("can_act");
    }
    builder.canAct(canAct);
    
    int prevX = playerX;
    if (playerData.isNull("prev_x")) {
      System.out.println("Warning: prev_x is null");
    }
    else {
      prevX = playerData.getInt("prev_x");
    }
    
    int prevY = playerY;
    if (playerData.isNull("prev_y")) {
      System.out.println("Warning: prev_y is null");
    }
    else {
      prevY = playerData.getInt("prev_y");
    }
    
    int turnsRemaining = 0;
    if (playerData.isNull("turns_remaining")) {
      System.out.println("Warning: turns_remaining is null");
    }
    else {
      turnsRemaining = playerData.getInt("turns_remaining");
    }
    builder.turnsRemaining(turnsRemaining);
    
    double money = 0;
    if (playerData.isNull("money")) {
      System.out.println("Warning: money is null");
    }
    else {
      money = playerData.getDouble("money");
    }
    builder.withMoney(money);
    
    int kills = 0;
    if (playerData.isNull("kills")) {
      System.out.println("Warning: kills is null");
    }
    else {
      kills = playerData.getInt("kills");
    }
    
    int sales = 0;
    if (playerData.isNull("sales")) {
      System.out.println("Warning: sales is null");
    }
    else {
      sales = playerData.getInt("sales");
    }
    
    int chocolate = 0;
    if (playerData.isNull("chocolate")) {
      System.out.println("Warning: chocolate is null");
    }
    else {
      chocolate = playerData.getInt("chocolate");
    }
    builder.withItem(Flavors.CHOCOLATE, chocolate);
    
    int vanilla = 0;
    if (playerData.isNull("vanilla")) {
      System.out.println("Warning: vanilla is null");
    }
    else {
      vanilla = playerData.getInt("vanilla");
    }
    builder.withItem(Flavors.VANILLA, vanilla);
    
    int strawberry = 0;
    if (playerData.isNull("strawberry")) {
      System.out.println("Warning: strawberry is null");
    }
    else {
      strawberry = playerData.getInt("strawberry");
    }
    builder.withItem(Flavors.STRAWBERRY, strawberry);
    
    return builder.buildPlayer();
  }
    
  /**
   * 
   * @param name
   */
  public synchronized void joinGame(String name) {
  }

  /**
   * 
   */
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
        int tileX = tileData.getInt("x");
        int tileY = tileData.getInt("y");
        Tile tile = processTile(tileData);
        gameBoardBuilder.withKnownTile(tileX, tileY, tile);
        knownTileIndex++;
      }
      
      JSONObject playerData = (JSONObject)response.get("players");
      
      JSONObject yourPlayerData = (JSONObject)playerData.get("you");
      Player yourPlayer = parsePlayerData(yourPlayerData);
      gameBoardBuilder.withYou(yourPlayer);
      boolean turn = yourPlayerData.getBoolean("turn");
      gameBoardBuilder.isYourTurn(turn);
      
      JSONArray otherPlayerData = (JSONArray)playerData.get("others");
      ArrayList otherPlayersList = new ArrayList();
      int otherPlayerIndex = 0;
      while (!otherPlayerData.isNull(otherPlayerIndex)) {
        JSONObject anotherPlayerData = 
            (JSONObject)otherPlayerData.get(otherPlayerIndex);
        Player player = parsePlayerData(anotherPlayerData);
        otherPlayersList.add(player);
      }
      Player[] otherPlayersArray = new Player[otherPlayersList.size()];
      for (int i = 0; i < otherPlayersList.size(); i++) {
        otherPlayersArray[i] = (Player)otherPlayersList.get(i);
      }
      gameBoardBuilder.withOthers(otherPlayersArray);
      
      JSONObject costData = (JSONObject)response.get("costs");
      double[] itemCosts = new double[3];
      itemCosts[Flavors.CHOCOLATE] = costData.getDouble("C");
      itemCosts[Flavors.STRAWBERRY] = costData.getDouble("S");
      itemCosts[Flavors.VANILLA] = costData.getDouble("V");
      gameBoardBuilder.withCosts(itemCosts);
      
      JSONObject basePriceData = (JSONObject)response.get("prices");
      double[] basePrices = new double[3];
      itemCosts[Flavors.CHOCOLATE] = basePriceData.getDouble("C");
      itemCosts[Flavors.STRAWBERRY] = basePriceData.getDouble("S");
      itemCosts[Flavors.VANILLA] = basePriceData.getDouble("V");
      gameBoardBuilder.withBasePrices(basePrices);
      
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