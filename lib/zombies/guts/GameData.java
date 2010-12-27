package zombies.guts;
import game.*;
import org.json.*;

public class GameData {
	
	JSONObject mData;
	
	public GameData(JSONObject gameBoard){
		mData = gameBoard;
		System.out.println("gameboard: " + gameBoard.toString());
	}
	
	
	
	
}