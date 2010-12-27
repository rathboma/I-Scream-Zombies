package zombies.scenes;

import java.util.*;
import java.awt.*;
import game.*;
import zombies.*;
import zombies.guts.*;
public class GameScene implements IScene{
	

	GameEngine engine;
	String error;
	IScene mChild = null, mParent = null;
	long nextTurnCheck = 0;
	public GameScene(GameEngine engine){
		this.engine = engine;
	}
	
	public void setup(){
		try{
			if(!engine.started()){
				engine.startGame("test_player");
			}
		}catch(GameServerException ex){
			System.out.println(ex);
			this.error = ex.getMessage();
		}
	}

	public void addChild(IScene child){
		mChild = child;
	}

	//This is the simplest implementation of a finish method.
	public void finish(){
		//Platform.platform.removeThing(mBackground);
	}


		public void update(){
			try{
				if(engine.myTurn()){	
					System.out.println("its my turn!");
				}				
				
			}catch(GameServerException ex){
				System.err.println(ex);
			}
			
			
			
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