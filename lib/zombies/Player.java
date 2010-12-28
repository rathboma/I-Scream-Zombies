package zombies;
import org.json.*;
import zombies.guts.*;


public class Player{
	public double money;
	public Coordinate coordinates;
	public int vanilla;
	public int strawberry;
	public int chocolate;
	public int kills;
	public int sales;
	public int turnsRemaining;
	
	public boolean canMove;
	public boolean canAct;
	
	public boolean isTurn(){
		return canMove || canAct;
	}
	
	
	public static Player fromJSON(JSONObject json) throws JSONException{
		Player p = Player.basicFromJSON(json);
		p.turnsRemaining = json.getInt("turns_remaining");
		p.canMove = json.getBoolean("can_move");
		p.canAct = json.getBoolean("can_act");
		p.vanilla = json.getInt("vanilla");
		p.chocolate = json.getInt("chocolate");
		p.strawberry = json.getInt("strawberry");
		return p;
	}
	
	public static Player basicFromJSON(JSONObject json) throws JSONException{
		Player p = new Player();
		p.coordinates = new Coordinate(json.getInt("x"), json.getInt("y"));
		p.kills = json.getInt("kills");
		p.sales = json.getInt("sales");
		return p;
	}
	
}