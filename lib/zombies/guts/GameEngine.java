package zombies.guts;

import java.util.*;
import java.awt.*;
import game.*;
import zombies.*;
public class GameEngine {
	

	GameData data = null;
	JsonFetcher fetcher;
	String uuid;
	public GameEngine(boolean debug){
		fetcher = new JsonFetcher(debug);
	}
	public boolean started(){
		return data != null;
	}
	public void startGame(String name) throws GameServerException {
		uuid = fetcher.join(name);
		fetcher.uuid = uuid;
		data = new GameData(fetcher.getBoard());
	}
	public GameData getGameData(){
		return this.data;
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
