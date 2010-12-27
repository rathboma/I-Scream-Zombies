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
	long nextTurnCheck = 0;
	public GameEngine(boolean debug){
		fetcher = new JsonFetcher(debug);
	}
	public boolean started(){
		return data != null;
	}
	public void startGame(String name) throws GameServerException {
		uuid = fetcher.join(name);
		fetcher.uuid = uuid;
		
	}
	public GameData getGameData() {
		return this.data;
	}
	
	public void updateData() throws GameServerException{
		data = new GameData(fetcher.getBoard());
	}
	
	
	public boolean myTurn() throws GameServerException{		
		long now = (new Date()).getTime();
		if(nextTurnCheck <= now){
			System.out.println("" + nextTurnCheck + " vs " + now );
			nextTurnCheck = now + 1000 * 5; //five seconds
			updateData();
		}
		return data.myTurn();
	}
	
	public boolean isTurn() throws GameServerException {
		JSONObject o = fetcher.getTurn();
		try{
			return o.getBoolean("turn");
		}catch(JSONException ex){
			throw new GameServerException("can't work out if its my turn: " + o.toString());
		}
		
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
