package zombies.model;

/**
 * A player's info
 * @author Dan L. Dela Rosa
 *
 */
public class Player {
  private final int x;
  private final int y;
  private final double score;
  private final double money;
  private final int[] inventory;
  private final int kills;
  private final int sales;
  private final int turnsRemaining;
  private final boolean canAct;
  private final boolean canMove;
  
  /**
   * Get the player's x-position
   * @return
   */
  public int getX() {
    return x;
  }

  /**
   * Get the player's y-position
   * @return
   */
  public int getY() {
    return y;
  }

  /**
   * Get the player's score
   * @return
   */
  public double getScore() {
    return score;
  }

  /**
   * Get the player's money
   * @return
   */
  public double getMoney() {
    return money;
  }
  
  /**
   * Get the number of instances of the specified item in the player's 
   * inventory
   * @param flavor
   * @return
   */
  public int getInstancesOf(int flavor) {
    if (inventory == null || inventory.length < flavor) {
      return 0;
    }
    else {
      return inventory[flavor];
    }
  }

  /**
   * Get the number of kills for the player
   * @return
   */
  public int getKills() {
    return kills;
  }

  /**
   * Get the number of sales for the player
   * @return
   */
  public int getSales() {
    return sales;
  }

  /**
   * Get the number of turns remaining for the player
   * @return
   */
  public int getTurnsRemaining() {
    return turnsRemaining;
  }

  /**
   * Get whether the player can act
   * @return
   */
  public boolean canAct() {
    return canAct;
  }

  /**
   * Get whether the player can move
   * @return
   */
  public boolean canMove() {
    return canMove;
  }

  private Player(int x, int y, double score, double money, int[] inventory, 
      int kills, int sales, int turns_remaining, boolean can_act, 
      boolean can_move) {
    this.x = x;
    this.y = y;
    this.score = score;
    this.money = money;
    this.inventory = inventory;
    this.kills = kills;
    this.sales = sales;
    this.turnsRemaining = turns_remaining;
    this.canAct = can_act;
    this.canMove = can_move;
  }
  
  /**
   * A builder object for the Player
   * @author Dan L. Dela Rosa
   *
   */
  public static class PlayerBuilder {
    private final int x;
    private final int y;
    private double score = 0;
    private double money = 0;
    private int[] inventory = new int[3];
    private int kills = 0;
    private int sales = 0;
    private int turnsRemaining = 0;
    private boolean canAct = false;
    private boolean canMove = false;
    
    /**
     * Creates an instance of PlayerBuilder
     * @param x
     * @param y
     */
    public PlayerBuilder(int x, int y) {
      this.x = x;
      this.y = y;
    }
    
    /**
     * Set the player's score
     * @param score
     * @return
     */
    public PlayerBuilder withScore(double score) {
      this.score = score;
      return this;
    }
    
    /**
     * Set the player's money
     * @param money
     * @return
     */
    public PlayerBuilder withMoney(double money) {
      this.money = money;
      return this;
    }
    
    /**
     * Set of the number of an item the player has in the inventory
     * @param item
     * @param quantity
     * @return
     */
    public PlayerBuilder withItem(int item, int quantity) {
      this.inventory[item] = quantity;
      return this;
    }
    
    /**
     * Set the number of turns remaining
     * @param turns_remaining
     * @return
     */
    public PlayerBuilder turnsRemaining(int turnsRemaining) {
      this.turnsRemaining = turnsRemaining;
      return this;
    }
    
    /**
     * Set whether the player can act
     * @param can_act
     * @return
     */
    public PlayerBuilder canAct(boolean canAct) {
      this.canAct = canAct;
      return this;
    }
    
    /**
     * Set whether the player can move
     * @param can_move
     * @return
     */
    public PlayerBuilder canMove(boolean canMove) {
      this.canMove = canMove;
      return this;
    }
    
    /**
     * Finally, create the Player object
     * @return A new instance of Player
     */
    public Player buildPlayer() {
      return new Player(x, y, score, money, inventory, kills, sales, 
          turnsRemaining, canAct, canMove);
    }
  }
}
