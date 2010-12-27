package zombies.guts;
import org.json.*;

public class GameServerException extends Exception {
	
	public static GameServerException fromJson(JSONObject obj){
		
		try{
			return new GameServerException(obj.getString("error"));
		}catch(JSONException ex){
			return new GameServerException("unknown jsony error : " + obj.toString());
		}
		
	}
	
	public GameServerException(String msg){
		super(msg);
	}
	public GameServerException(Exception e){
		super(e);
	}
	

}