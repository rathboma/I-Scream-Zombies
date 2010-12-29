package zombies.guts;

import java.util.*;
import java.awt.*;
import game.*;
import zombies.*;
import org.json.*;
public class GameEngine {

	Game data = null;
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
	
	public Game updateData() throws GameServerException{
		try{
			long now = (new Date()).getTime();
			if(nextRefresh <= now){
				nextRefresh = now + 1000 * 5; //five seconds
				data = (new GameData(fetcher.getBoard())).toGame();
			}
			return data;
		}catch(Exception ex){
			throw new GameServerException(ex);
		}
	}
	
	long turnCheck = 0;
	
	public boolean checkForTurn() throws GameServerException{
		long now = (new Date()).getTime();
		if(turnCheck <= now){
			turnCheck = now + 5000;
			JSONObject result = fetcher.getTurn();
			try{
				return result.getBoolean("turn");
			}catch(JSONException ex){throw new GameServerException(ex);}
		}
		else return data.player.isTurn();
	}
	
	
	public ActionUpdate moveTo(Tile p) throws GameServerException{
		try{
			JSONObject result = fetcher.postMove(p.tileX, p.tileY);
			ActionUpdate update = ActionUpdate.fromJSON(result);
			return update;
		}catch(JSONException ex){throw new GameServerException(ex);}
	}
	public ActionUpdate postKill() throws GameServerException{
		try{
			JSONObject result = fetcher.postKill();
			ActionUpdate update = ActionUpdate.fromJSON(result);
			return update;
		}catch(JSONException ex){throw new GameServerException(ex);}
		
	}
	public ActionUpdate postSell(int id, String flavor) throws GameServerException{
		try{
			JSONObject result = fetcher.postSell(id, flavor);
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
