import game.*;
import java.awt.*;
import zombies.*;
import zombies.scenes.*;
import zombies.guts.*;
import java.util.*;
public class Zombies extends Platform {

	SceneManager mManager;

	public void setup(){
		//_controller = new LevelController();
		
		try{
			Platform.platform = this;
		
			MenuScene menu = new MenuScene();		
			GameScene game = new GameScene(new GameEngine(true));
			menu.addChild(game);
			mManager = new SceneManager(menu);
		}
		catch(Exception ex){
			System.out.println(ex);
		}
	}
	
	
	public void update(){
		
		mManager.getCurrentScene().update();
	}
	
	public void overlay(Graphics g) {
		mManager.getCurrentScene().updateOverlay(g);
	}
	
}




