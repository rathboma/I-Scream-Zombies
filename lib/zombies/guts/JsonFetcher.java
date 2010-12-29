package zombies.guts;
import zombies.*;
import org.json.*;
import java.io.*;
import java.net.*;

public class JsonFetcher{
	//String root = "http://iscreamzombies.heroku.com/";
	String root = "http://localhost:3000/";
	boolean debug;
	public String uuid;
	
	public JsonFetcher(boolean debugging){
		this.debug = debugging;
	}
	
	public String join(String name) throws GameServerException {
		String args = "name=" + name;
		if(debug) args += "&debug=true";
		JSONObject result = post("join", args);
		try{
			validate(result);
			return result.getString("uuid");
		}catch(JSONException ex){
			throw new GameServerException("problems with the server, response: " + result.toString());
		}
	}
	
	public JSONObject getBoard() throws GameServerException {
		JSONObject result = get("get_game_state/" + uuid, null);
		validate(result);
		return result;
	}
	
	public JSONObject getTurn() throws GameServerException{
		JSONObject result = get("get_turn/" + uuid, null);
		validate(result);
		return result;
	}
	
	public JSONObject postMove(int x, int y) throws GameServerException{
		String args = defaultArgs();
		args += "&x=" + x;
		args += "&y=" + y;
		JSONObject result = post("move", args);
		System.out.println("posted move: " + result.toString());
		validate(result);
		return result;
	}
	public JSONObject postKill() throws GameServerException{
		String args = defaultArgs();
		JSONObject result = post("kill", args);
		validate(result);
		return result;
	}
	public JSONObject postSell(int id, String flavor) throws GameServerException{
		String args = defaultArgs();
		args += "&customer_id=" + id;
		args += "&number=" + 1;
		args += "&flavors=" + flavor;
		JSONObject result = post("sell", args);
		validate(result);
		return result;
	}
	public JSONObject postBuy(String flavor, int num) throws GameServerException{
		String args = defaultArgs();
		args += "&number=" + num;
		args += "&flavor=" + flavor;
		JSONObject result = post("buy", args);
		validate(result);
		return result;
	}
	
	
	
	
	private void validate(JSONObject obj) throws GameServerException {
		//System.out.println("uuid : " + uuid + ", validating : " + obj.toString());
		
		if(obj.has("error")) throw GameServerException.fromJson(obj);
	}
	
	
	private String defaultArgs(){
		return "uuid=" + uuid;
	}
	
	public JSONObject post(String path, String args){
		
    try {
      URL server = new URL(root + path);
      HttpURLConnection connection = (HttpURLConnection) server.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.connect();
      Writer writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
      writer.write(args);
      writer.flush();
      writer.close();
      
      BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
			String response = "";
      while ((line = is.readLine()) != null) {
        response = response + line;
      }
      return new JSONObject(response);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return new JSONObject();
	}
	
	
	public JSONObject get(String path, String args){
		JSONObject result;
    try {
			String all = root + path;
			if(args != null) all += "?" + args;
      URL server = new URL(all);
      HttpURLConnection connection = (HttpURLConnection) server.openConnection();
      connection.connect();
      BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String response = "";
			String line;
      while ((line = is.readLine()) != null) {response += line;}

      result = new JSONObject(response);
    }
    catch (Exception e) {
      e.printStackTrace();
			result = new JSONObject();
    }
		return result;
	}
	
	
}