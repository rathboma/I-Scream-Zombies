package zombies.model;

/**
 * Game board for the I Scream Zombies Game
 * @author Dan L. Dela Rosa
 */
public class GameBoard {
  private final String UUID;
  private Player you;
  private Player[] others;
  private Tile[][] tiles;
  private double[] cost;
  private double[] basePrice;
  private boolean yourTurn;
  private boolean gameOver;
  private boolean win;
  
  private GameBoard(GameBoardBuilder builder) {
    this.UUID = builder.UUID;
    this.you = builder.you;
    this.others = builder.others;
    this.tiles = builder.tiles;
    this.cost = builder.cost;
    this.basePrice = builder.basePrice;
    this.yourTurn = builder.yourTurn;
    this.gameOver = builder.gameOver;
    this.win = builder.win;
  }
  
  public boolean inBounds(int x, int y) {
    if (x < 0) return false;
    if (y < 0) return false;
    if (x > tiles.length - 1) return false;
    if (y > tiles[x].length - 1) return false;
    return true;
  }
  
  /**
   * Builder object for the Game Board
   * @author Dan L. Dela Rosa
   */
  public static class GameBoardBuilder {
    private final String UUID;
    private Player you = new Player.PlayerBuilder(-1,-1).buildPlayer();
    private Player[] others;
    private Tile[][] tiles;
    private double[] cost;
    private double[] basePrice;
    private boolean yourTurn;
    private boolean gameOver;
    private boolean win;
    
    /**
     * Gives the Game Board a UUID, width, and height
     * @param UUID
     * @param width
     * @param height
     */
    public GameBoardBuilder(String UUID, int width, int height) {
      this.UUID = UUID;
      tiles = new Tile[width][height];
    }
    
    /**
     * Give the board your player
     * @param you
     * @return
     */
    public GameBoardBuilder withYou(Player you) {
      this.you = you;
      return this;
    }
    
    /**
     * Give the board the other players
     * @param others
     * @return
     */
    public GameBoardBuilder withOthers(Player[] others) {
      this.others = others;
      return this;
    }
    
    /**
     * Specify data for an already known tile
     * @param x
     * @param y
     * @param tile
     * @return
     */
    public GameBoardBuilder withKnownTile(int x, int y, Tile tile) {
      tiles[x][y] = tile;
      return this;
    }
    
    /**
     * Add the costs to buy each item 
     * @param costs
     * @return
     */
    public GameBoardBuilder withCosts(double[] costs) {
      this.cost = costs;
      return this;
    }
    
    /**
     * Add the base prices to sell to each customer
     * @param basePrices
     * @return
     */
    public GameBoardBuilder withBasePrices(double[] basePrices) {
      this.basePrice = basePrices;
      return this;
    }
    
    /**
     * Specify whether it is your turn
     * @param yourTurn
     * @return
     */
    public GameBoardBuilder isYourTurn(boolean yourTurn) {
      this.yourTurn = yourTurn;
      return this;
    }
    
    /**
     * Specify whether the game is over
     * @param gameOver
     * @return
     */
    public GameBoardBuilder isGameOver(boolean gameOver) {
      this.gameOver = gameOver;
      return this;
    }
    
    /**
     * Specify whether your won
     * @param win
     * @return
     */
    public GameBoardBuilder isWin(boolean win) {
      this.win = win;
      return this;
    }
    
    /**
     * Finally, build the game board
     * @return
     */
    public GameBoard buildGameBoard() {
      for (int x = 0; x < tiles.length; x++) {
        for (int y = 0; y < tiles[x].length; y++) {
          if (tiles[x][y] == null) {
            tiles[x][y] = Tile.UnknownTile;
          }
        }
      }
      return new GameBoard(this);
    }
  }
  
  /**
   * Get the boards' UUID
   * @return
   */
  public String getUUID() {
    return UUID;
  }

  /**
   * Get your player
   * @return
   */
  public Player getYou() {
    return you;
  }

  /**
   * Get the other players
   * @return
   */
  public Player[] getOthers() {
    return others;
  }
  
  /**
   * Get the tile at the specified location
   * @param x
   * @param y
   * @return
   */
  public Tile getTileAt(int x, int y) {
    return tiles[x][y];
  }

  /**
   * See if it is your turn
   * @return
   */
  public boolean isYourTurn() {
    return yourTurn;
  }

  /**
   * See if the game is over
   * @return
   */
  public boolean isGameOver() {
    return gameOver;
  }

  /**
   * See if you won
   * @return
   */
  public boolean isWin() {
    return win;
  }
  
  /**
   * Get the cost of an item
   * @param flavor
   * @return
   */
  public double getCostOf(int flavor) {
    if (cost == null || cost.length < flavor) {
      return 0;
    }
    else {
      return cost[flavor];
    }
  }
  
  /**
   * Get the base selling price of an item
   * @param flavor
   * @return
   */
  public double getBasePriceOf(int flavor) {
    if (basePrice == null || basePrice.length < flavor) {
      return 0;
    }
    else {
      return basePrice[flavor];
    }
  }
  
  public int getWidth() {
    return tiles.length;
  }
  
  public int getHeight() {
    if (tiles.length == 0) return 0;
    return tiles[0].length;
  }

  /**
   * Overwrite the client's player data
   * @param player The player data
   */
  void updatePlayer(Player player) {
    you = player;
  }
  
  /**
   * Overwrite the tile data at a given position
   * @param x
   * @param y
   * @param tile
   */
  void updateTile(int x, int y, Tile tile) {
    tiles[x][y] = tile;
  }
  
  /**
   * Update whether it is your turn
   * @param yourTurn
   */
  void updateYourTurn(boolean yourTurn) {
    this.yourTurn = yourTurn;
  }
  
  /**
   * Update whether the game is over
   * @param gameOver
   */
  void updateGameOver(boolean gameOver) {
    this.gameOver = gameOver;
  }
  
  /**
   * Update whether you win
   * @param win
   */
  void updateWin(boolean win) {
    this.win = win;
  }
}
