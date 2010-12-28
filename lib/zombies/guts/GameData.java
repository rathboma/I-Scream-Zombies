package zombies.guts;
import game.*;
import org.json.*;
import zombies.*;

public class GameData {
	
	JSONObject mData;
	JSONObject mPlayer;
	JSONArray mOthers;
	
	public int x, y;
	
	public GameData(JSONObject gameBoard) throws GameServerException{
		try{
			mData = gameBoard;
			mPlayer = mData.getJSONObject("players").getJSONObject("you");
			mOthers = mData.getJSONObject("players").getJSONArray("others");
			this.x = mData.getJSONObject("game_board").getJSONArray("size").getInt(0);
			this.y = mData.getJSONObject("game_board").getJSONArray("size").getInt(1);
			System.out.println("game data initialized, x: " + this.x + " y: " + this.y);
		}catch(JSONException ex){
			throw new GameServerException(ex);
		}
		System.out.println("gameboard: " + gameBoard.toString());
	}
	
	public Game toGame() throws JSONException, GameServerException{
		Game g = new Game();
		g.player = Player.fromJSON(mPlayer);
		g.chocolatePrice = mData.getJSONObject("prices").getDouble("C");
		g.strawberryPrice = mData.getJSONObject("prices").getDouble("S");
		g.vanillaPrice = mData.getJSONObject("prices").getDouble("V");
		
		g.vanillaCost = mData.getJSONObject("costs").getInt("V");
		g.chocolateCost = mData.getJSONObject("costs").getInt("C");
		g.strawberryCost = mData.getJSONObject("costs").getInt("S");		
		g.tiles = getTiles();
		if( mOthers.length() > 0) g.other = Player.basicFromJSON(mOthers.getJSONObject(0));
		return g;
	}
	
	
	private Tile[][] getTiles() throws GameServerException{
			Tile[] visibleTiles = visibleTiles();
			Tile[][] results = new Tile[x][y];
			for(int i = 0; i < results.length; i++)
				for(int j = 0; j < results[i].length; j++){
					results[i][j] = new Tile(i, j);
				}
			for(int i = 0; i < visibleTiles.length; i++){
				Tile t = visibleTiles[i];
				results[t.tileX][t.tileY] = t;
			}
			return results;
	}
	
	
	
	public Tile[] visibleTiles() throws GameServerException{
		try{
			JSONArray tiles = mData.getJSONObject("game_board").getJSONArray("known");
			Tile[] results = new Tile[tiles.length()];
			for(int i = 0; i < results.length; i++){
				Tile t = Tile.fromJSON(tiles.getJSONObject(i));
				results[i] = t;
			}
			return results;
		}catch(JSONException ex){
			throw new GameServerException(ex);
		}
	}
	
	
	public int getPlayerInt(String field) throws GameServerException{
		try{
			return mPlayer.getInt(field);
		}catch(JSONException ex){
			throw new GameServerException(ex);
		}
	}
}