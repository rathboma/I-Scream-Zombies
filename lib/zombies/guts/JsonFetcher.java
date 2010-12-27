package zombies.guts;
import org.json.*;
import java.io.*;
import java.net.*;

public class JsonFetcher{
	String root = "http://iscreamzombies.heroku.com/";
	boolean debug;
	public String uuid;
	
	public JsonFetcher(boolean debugging){
		this.debug = debugging;
	}
	
	public String join(String name){
		String args = "name=" + name;
		if(debug) args += "&debug=true";
		JSONObject result = post("join", args);
		try{
			return result.getString("uuid");
		}catch(JSONException ex){}
		
		return null;
	}
	
	public JSONObject getBoard(){
		String args = defaultArgs();
		return get("get_game_state/" + uuid, null);
	}
	
	
	
	
	
	
	private String defaultArgs(){
		return "uuid=" + uuid;
	}
	
	private JSONObject post(String path, String args){
		
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
	
	
	private JSONObject get(String path, String args){
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