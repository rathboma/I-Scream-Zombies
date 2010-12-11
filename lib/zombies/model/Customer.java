package zombies.model;

/**
 * Information on a customer
 * @author Dan L. Dela Rosa
 */
public class Customer {
  private final int id;
  private final int[] favoriteType;
  private final double favoritePrice;
  private final int favoriteNumber;
  private Customer(int id, int[] favoriteType, double favoritePrice, 
      int favoriteNumber) {
    this.id = id;
    this.favoriteType = favoriteType;
    this.favoritePrice = favoritePrice;
    this.favoriteNumber = favoriteNumber;
  }
  
  /**
   * Get the customer's ID
   * @return id
   */
  public int getId() {
    return id;
  }
  
  /**
   * Get the customer's favorite type
   * @return type
   */
  public int[] getFavoriteType() {
    return favoriteType;
  }
  
  /**
   * Get the price for the customer's favorite
   * @return price of favorite
   */
  public double getFavoritePrice() {
    return favoritePrice;
  }
  
  /**
   * Get the number of times the customer's favorite can be sold
   * @return number of times favorite can be solved
   */
  public int getFavoriteNumber() {
    return favoriteNumber;
  }

  /**
   * A Builder object for the customer
   * @author Dan L. Dela Rosa
   */
  public static class CustomerBuilder {
    private final int id;
    private int[] favoriteType = new int[3];
    private double favoritePrice = 0;
    private int favoriteNumber = 0;
    
    /**
     * Get an instance of CustomerBuilder
     */
    public CustomerBuilder(int id) {
      this.id = id;
    }
    
    /**
     * Add an instance of a flavor to the customer's favorite
     * @param flavor
     * @return
     */
    public CustomerBuilder addFavoriteFlavor(int flavor) {
      favoriteType[flavor]++;
      return this;
    }
    
    /**
     * Give the customer the price of the favorite
     * @param favoritePrice
     * @return
     */
    public CustomerBuilder withCustomerPrice(double favoritePrice) {
      this.favoritePrice = favoritePrice;
      return this;
    }
    
    /**
     * Give the customer the number of times it can be sold
     * @param favoriteNumber
     * @return
     */
    public CustomerBuilder withFavoriteNumber(int favoriteNumber) {
      this.favoriteNumber = favoriteNumber;
      return this;
    }
    
    /**
     * Finally, build the Customer
     * @return a new instance of Customer
     */
    public Customer buildCustomer() {
      return new Customer(id, favoriteType, favoritePrice, favoriteNumber);
    }
  }
}
