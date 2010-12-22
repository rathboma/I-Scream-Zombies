package zombies.controller;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import org.json.*;

import zombies.model.Customer;
import zombies.model.Customer.CustomerBuilder;
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
  private JSONObject sendPostRequestToServer(String requestAddress, 
      JSONObject object) {
    String response = "";
    try {
      URL serverURL = new URL(requestAddress);
      HttpURLConnection connection = 
          (HttpURLConnection) serverURL.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.connect();
      
      OutputStream out = connection.getOutputStream();
      Writer writer = new OutputStreamWriter(out, "UTF-8");
      writer.write(object.toString());
      writer.flush();
      writer.close();
      
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
    TileBuilder tileBuilder = new TileBuilder();
    
    boolean isStore = tileData.getBoolean("store");
    tileBuilder.isStore(isStore);
    
    int zombies;
    if (tileData.isNull("zombies")) {
      System.out.println("Warning: zombies is null");
      zombies = 0;
    }
    else {
      zombies = tileData.getInt("zombies");
    }
    tileBuilder.withZombies(zombies);
    
    JSONArray customersData = (JSONArray)tileData.get("customers");
    int customerIndex = 0;
    while (!customersData.isNull(customerIndex)) {
      try {
        JSONObject customerData = customersData.getJSONObject(customerIndex);
        int id = customerData.getInt("id");
        CustomerBuilder cBuilder = new CustomerBuilder(id);
        
        int favoriteNumber = customerData.getInt("favorite_number");
        cBuilder.withFavoriteNumber(favoriteNumber);
        
        double favoritePrice = customerData.getDouble("favorite_price");
        cBuilder.withCustomerPrice(favoritePrice);
        
        String favoriteFlavors = customerData.getString("favorite_type");
        String[] flavors = favoriteFlavors.split("-");
        for (int i = 1; i < flavors.length; i++) {
          if (flavors[i]=="C") cBuilder.addFavoriteFlavor(Flavors.CHOCOLATE);
          if (flavors[i]=="S") cBuilder.addFavoriteFlavor(Flavors.STRAWBERRY);
          if (flavors[i]=="V") cBuilder.addFavoriteFlavor(Flavors.VANILLA);
        }
        Customer customer = cBuilder.buildCustomer();
        tileBuilder.withCustomer(customer);
      }
      catch (Exception e) {
        System.out.println(e.toString());
      }
    }
    
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
    builder.withPrevX(prevX);
    
    int prevY = playerY;
    if (playerData.isNull("prev_y")) {
      System.out.println("Warning: prev_y is null");
    }
    else {
      prevY = playerData.getInt("prev_y");
    }
    builder.withPrevY(prevY);
    
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
    builder.withKills(kills);
    
    int sales = 0;
    if (playerData.isNull("sales")) {
      System.out.println("Warning: sales is null");
    }
    else {
      sales = playerData.getInt("sales");
    }
    builder.withSales(sales);
    
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
    System.out.println("Finding a game for you...");
    String requestAddress = serverAddress + "join/";
    JSONObject request = new JSONObject();
    try {
      request.put("name", name);
      // Set AI to true for now
      request.put("ai", true);
      JSONObject response = sendPostRequestToServer(requestAddress, request);
      String UUID = response.getString("uuid");
      gameModel.setUUID(UUID);
    }
    catch (Exception e) {
      System.out.println(e);
    }
  }

  /**
   * 
   */
  public synchronized void getTurn(String UUID) {
    System.out.println("Getting turn...");
    String requestAddress = serverAddress + "get_turn/" + UUID;
    JSONObject response = sendGetRequestToServer(requestAddress);
    System.out.println(response);
    
    if (response.isNull("turn")) {
      System.out.println("Warning: turn is null");
    }
    else {
      boolean isYourTurn;
      try {
        isYourTurn = response.getBoolean("turn");
        gameModel.updateYourTurn(isYourTurn);
        if (isYourTurn) {
          System.out.println("Yes! It's your turn!");
        }
        else {
          System.out.println("It's not your turn yet...");
        }
      } catch (JSONException e) {
        System.out.println(e);
      }
    }
  }
  
  public void getGameState(String UUID) {
    System.out.println("Getting game state...");
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
        otherPlayerIndex++;
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
  
  /**
   * @param x
   * @param y
   * @param response
   * @throws JSONException
   */
  private void handleCommonResponse(int x, int y, JSONObject response)
          throws JSONException {
    if(!response.isNull("error")) {
      System.out.print(response.optString("error"));
    }
    else if (response.isNull("tile") || response.isNull("player")) {
      System.out.println("Warning: missing information, aborting operation");
    }
    else {
      JSONObject tileData = (JSONObject)response.get("tile");
      Tile tile = processTile(tileData);
      gameModel.updateTile(x, y, tile);
      
      JSONObject playerData = (JSONObject)response.get("player");
      Player player = parsePlayerData(playerData);
      gameModel.updatePlayer(player);
    }
  }
  
  /**
   * 
   * @param x
   * @param y
   */
  public synchronized void makeMove(int x, int y) {
    String requestAddress = serverAddress + "make_move/";
    JSONObject request = new JSONObject();
    try {
      request.put("uuid", gameModel.getUUID());
      request.put("x", x);
      request.put("y", y);
      JSONObject response = sendPostRequestToServer(requestAddress, request);
      System.out.println("Moving to " + x + ", " + y + "...");
      System.out.println(response);
      
      handleCommonResponse(x, y, response);
    } 
    catch (Exception e) {
      System.out.println(e);
    }
  }
  
  public synchronized void buy(int flavor, int number) {
    int x = gameModel.getPlayerX();
    int y = gameModel.getPlayerY();
    String requestAddress = serverAddress + "buy/";
    JSONObject request = new JSONObject();
    try {
      request.put("uuid", gameModel.getUUID());
      request.put("flavor", flavor);
      request.put("number", number);
      JSONObject response = sendPostRequestToServer(requestAddress, request);
      
      handleCommonResponse(x, y, response);
    } 
    catch (Exception e) {
      System.out.println(e);
    }
  }
  
  public synchronized void sell(String flavors, int number, int customerId) {
    int x = gameModel.getPlayerX();
    int y = gameModel.getPlayerY();
    String requestAddress = serverAddress + "sell/";
    JSONObject request = new JSONObject();
    try {
      request.put("uuid", gameModel.getUUID());
      request.put("flavors", flavors);
      request.put("number", number);
      request.put("customer_id", customerId);
      JSONObject response = sendPostRequestToServer(requestAddress, request);
      
      handleCommonResponse(x, y, response);
    } 
    catch (Exception e) {
      System.out.println(e);
    }
  }
  
  public synchronized void kill() {
    int x = gameModel.getPlayerX();
    int y = gameModel.getPlayerY();
    String requestAddress = serverAddress + "kill/";
    JSONObject request = new JSONObject();
    try {
      request.put("uuid", gameModel.getUUID());
      JSONObject response = sendPostRequestToServer(requestAddress, request);
      
      handleCommonResponse(x, y, response);
    } 
    catch (Exception e) {
      System.out.println(e);
    }
  }
  
  public synchronized void run() {
    int x = gameModel.getPlayerX();
    int y = gameModel.getPlayerY();
    String requestAddress = serverAddress + "run/";
    JSONObject request = new JSONObject();
    try {
      request.put("uuid", gameModel.getUUID());
      JSONObject response = sendPostRequestToServer(requestAddress, request);
      
      handleCommonResponse(x, y, response);
    } 
    catch (Exception e) {
      System.out.println(e);
    }
  }
}