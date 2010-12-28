package zombies;
import org.json.*;

public class Customer{
	
	public int id;
	public String favoriteType;
	public double favoritePrice;
	public int favoriteNum;
	
	public static Customer fromJSON(JSONObject j) throws JSONException{
		Customer result = new Customer();
		result.id = j.getInt("id");
		result.favoriteType = j.getString("favorite_type");
		result.favoritePrice = j.getDouble("favorite_price");
		result.favoriteNum = j.getInt("favorite_number");
		
		result.favoriteNum = 1; //only one for the minute.
		return result;
	}
	
	
	
}