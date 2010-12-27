package zombies.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Information about a game tile
 * @author Dan L. Dela Rosa
 */
public class Tile implements Iterable {
  private final boolean isStore;
  private final int zombies;
  private final List customers;
  private Tile(boolean isStore, int zombies, List customers){
    this.isStore = isStore;
    this.zombies = zombies;
    this.customers = customers;
  }
  
  /**
   * Get whether the tile is a store
   * @return
   */
  public boolean isStore() {
    return isStore;
  }
  
  /**
   * Get the number of zombies on this tile
   * @return
   */
  public int getZombies() {
    return zombies;
  }
  
  /**
   * Get an iterator of customers
   * @return
   */
  public Iterator iterator() {
    return customers.listIterator();
  }
  
  /**
   * Builder object for a tile
   */
  public static class TileBuilder {
    private boolean isStore = false;
    private int zombies = 0;
    private List customers = new LinkedList();
    
    /**
     * Set whether the tile is a store
     * @param isStore
     * @return
     */
    public TileBuilder isStore(boolean isStore) {
      this.isStore = isStore;
      return this;
    }
    
    /**
     * Set the number of zombies at this tile
     * @param zombies
     * @return
     */
    public TileBuilder withZombies(int zombies) {
      this.zombies = zombies;
      return this;
    }
    
    /**
     * Add a customer to this tile
     * @param customer
     * @return
     */
    public TileBuilder withCustomer(Customer customer) {
      customers.add(customer);
      return this;
    }
    
    /**
     * Finally, build the tile
     * @return
     */
    public Tile buildTile() {
      return new Tile(isStore, zombies, customers);
    }
  }
  
  /**
   * "Unknown" Tile Object - follows the "Null Pattern" 
   */
  public final static Tile UnknownTile = new TileBuilder().buildTile();
}
