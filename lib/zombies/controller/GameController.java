package zombies.controller;

import zombies.model.GameBoard;
import zombies.model.GameModel;
import zombies.model.Player;
import zombies.model.Tile;

/**
 * "Controller" object for the I Scream Zombies Game
 * @author Dan L. Dela Rosa
 */
public class GameController {
  // Make sure the object cannot instantiated externally
  private GameController() {}
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
  
  public synchronized void joinGame(String name) {
    // TODO send request
    // TODO get response
  }
  
  public synchronized void getTurn() {
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