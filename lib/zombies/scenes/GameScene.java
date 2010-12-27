package zombies.scenes;

import java.util.*;
import java.awt.*;
import game.*;
import zombies.*;
import zombies.guts.*;
public class GameScene implements IScene{
	
	GameData game;
	GameEngine engine;
	IScene mChild = null, mParent = null;
	public GameScene(GameEngine engine){
		this.engine = engine;
	}
	
	public void setup(){
		if(!engine.started()){
			engine.startGame("test_player");
		}
		game = engine.getGameData();
	}

	public void addChild(IScene child){
		mChild = child;
	}

	//This is the simplest implementation of a finish method.
	public void finish(){
		//Platform.platform.removeThing(mBackground);
	}


		public void update(){
			//dont have to call update on the things, that's done by the platform
			//TODO: Update shape positions if needed
		}

		public void updateOverlay(Graphics g){
			//nothing
		}

		public boolean childReady(){
			//some condition
			return false;
			//return mStart.clicked();
		}

		public IScene getChild(){
			return mChild;
		}

		public void addParent(IScene p){
			mParent = p;
		}

		public IScene getParent(){
			return null;
		}

		public boolean done(){
			return false;
		}

	//private

		private void initializeBackground(){
			//mBackground = new ImageThing("menubackground.png", Platform.platform.getWidth(), Platform.platform.getHeight());
			//Platform.platform.addThing(mBackground);
		}



}