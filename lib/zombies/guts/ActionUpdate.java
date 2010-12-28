package zombies.guts;
import zombies.*;
import org.json.*;


public class ActionUpdate{
	public Tile revealed;
	public Player player;
	
	
	public static ActionUpdate fromJSON(JSONObject json) throws JSONException{
		ActionUpdate result = new ActionUpdate();
		result.player = Player.fromJSON(json.getJSONObject("player"));
		result.revealed = Tile.fromJSON(json.getJSONObject("tile"));
		return result;
	}
	
}

