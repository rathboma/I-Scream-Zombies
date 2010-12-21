package zombies.model;

import java.util.Observable;

/**
 * "Model" object for the I Scream Zombies Game
 * @author Dan L. Dela Rosa
 */
public class GameModel extends Observable {
  private GameBoard board = 
    new GameBoard.GameBoardBuilder("", 0 ,0).buildGameBoard();
  private String UUID;
  
  //Make sure the object cannot instantiated externally
  private GameModel() {}
  // This class is a Singleton
  private static final GameModel instance = new GameModel();
  
  /**
   * Returns an instance of GameController
   * @return a GameController
   */
  public static GameModel getInstance() {
    return instance;
  }
  
  public void setGameBoard(GameBoard board) {
    this.board = board;
    setChanged();
    notifyObservers(board);
    clearChanged();
  }
  
  public GameBoard getGameBoard() {
    return board;
  }

  public void setUUID(String uUID) {
    UUID = uUID;
    setChanged();
    notifyObservers(board);
    clearChanged();
  }

  public String getUUID() {
    return UUID;
  }

  public void updateYourTurn(boolean yourTurn) {
    board.updateYourTurn(yourTurn);
    setChanged();
    notifyObservers(board);
    clearChanged();
  }

  public void updateTile(int x, int y, Tile tile) {
    board.updateTile(x, y, tile);
    setChanged();
    notifyObservers(board);
    clearChanged();
  }

  public void updatePlayer(Player player) {
    board.updatePlayer(player);
    setChanged();
    notifyObservers(board);
    clearChanged();
  }

  public int getPlayerX() {
    Player you = board.getYou();
    return you.getX();
  }

  public int getPlayerY() {
    Player you = board.getYou();
    return you.getY();
  }
}
