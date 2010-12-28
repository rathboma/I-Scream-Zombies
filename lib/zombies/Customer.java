package zombies;
import org.json.*;

public class Customer{
	
	public int id;
	public String[] willBuy = new String[]{"V", "C", "S", ""};
	public double[] purchasePrices = new double[]{1.5, 4, 4.5, 0};
	public int[] number = new int[]{1, 1, 1, 1};
	
	public static Customer fromJSON(JSONObject j) throws JSONException{
		Customer result = new Customer();
		result.willBuy[3] = j.getString("favorite_type");
		result.purchasePrices[3] = j.getDouble("favorite_price");
		result.number[3] = j.getInt("favorite_number");
		return result;
	}
	
	
	
}