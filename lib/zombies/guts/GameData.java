package zombies.guts;
import game.*;
import org.json.*;

public class GameData {
	
	JSONObject mData;
	
	public GameData(JSONObject gameBoard){
		mData = gameBoard;
		System.out.println("gameboard: " + gameBoard.toString());
	}
	
	
	
	public boolean myTurn() throws GameServerException{
		try{
			return mData.getJSONObject("players").getJSONObject("you").getBoolean("turn");
		}catch(JSONException ex){
			throw new GameServerException(ex);
		}
	}
	
	
	private void extract(JSONObject data){
		
	}
	
}