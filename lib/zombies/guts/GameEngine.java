package zombies.guts;

import java.util.*;
import java.awt.*;
import game.*;
import zombies.*;
import org.json.*;
public class GameEngine {

	GameData data = null;
	JsonFetcher fetcher;
	String uuid;
	long nextRefresh = 0;
	public GameEngine(boolean debug){
		fetcher = new JsonFetcher(debug);
	}
	public boolean started(){
		return data != null;
	}
	public void startGame(String name) throws GameServerException {
		uuid = fetcher.join(name);
		fetcher.uuid = uuid;
		updateData();
		
	}
	public GameData getGameData() {
		return this.data;
	}
	
	public Game updateData() throws GameServerException{
		try{
			long now = (new Date()).getTime();
			if(nextRefresh <= now){
				nextRefresh = now + 1000 * 5; //five seconds
				data = new GameData(fetcher.getBoard());
			}
			return data.toGame();
		}catch(Exception ex){
			throw new GameServerException(ex);
		}
	}
	
	
	public ActionUpdate moveTo(Coordinate p) throws GameServerException{
		try{
			JSONObject result = fetcher.postMove(p);
			ActionUpdate update = ActionUpdate.fromJSON(result);
			return update;
		}catch(JSONException ex){throw new GameServerException(ex);}
	}
	
	
	/*
	
		interface:
			is_turn //always has your uuid anyway
			do_action // give it an action object
			move // x, y, returns true/ false
			can_act?
			can_move?
			
	
	
	
	
	*/
	
	
	
	
}
