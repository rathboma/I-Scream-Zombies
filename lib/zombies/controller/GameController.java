package zombies.controller;

import zombies.model.GameBoard;

/**
 * "Controller" object for the I Scream Zombies Game
 * @author Dan L. Dela Rosa
 */
public class GameController {
  // Make sure the object cannot instantiated externally
  private GameController() {}
  // This class is a Singleton
  private final GameController instance = new GameController();
  // Right now, the controller managers only one game at a time
  private GameBoard gameBoard;
  
  /**
   * Returns an instance of GameController
   * @return a GameController
   */
  public GameController getInstance() {
    return instance;
  }
}
