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

	Game state;
	SideArea sideArea;
	RectThing bottomPanel;
	public Coordinate activeTile = null;

	
	public GameScene(GameEngine engine){
		this.engine = engine;
	}
	
	public void setup(){
		this.sideArea = new SideArea(this);
		bottomPanel = new RectThing(0, Platform.platform.getHeight() - 40, Platform.platform.getWidth(), 40);
		bottomPanel.setColor(Color.white);
		Platform.platform.addThing(bottomPanel);
		try{
			if(!engine.started()){
				engine.startGame("test_player");
				state = engine.updateData();
				Tile.visualize(0, 0, state.tiles, state.player.coordinates);
			}
		}catch(GameServerException ex){
			System.out.println(ex);
			this.error = ex.getMessage();
		}
	}
	private void refreshTiles(){

	}

	public void addChild(IScene child){
		mChild = child;
	}

	//This is the simplest implementation of a finish method.
	public void finish(){
		Platform.platform.removeThing(bottomPanel);
		//Platform.platform.removeThing(mBackground);
	}


		public void update(){
			try{
				/*
					for each tile do
						if tile.clicked
							tile.unclick
							display_tile_information_in_sidebar
				
				*/
				//state = engine.updateData();
				Tile t = Tile.selected(state.tiles);
				sideArea.updateSideArea(t, state);
				sideArea.update();
				handleUserEvents();

					
					/*
						if canMove()
							ensure current view == move_view
							if (playerMoved) // decided by a keypress event
								engine.doMove()
					
						else if canAct()
							ensure current_view = action_view (has buttons for stuff)
							for each action_button do
								if kill -> engine.kill()
								if sell -> validate selection (loop through each ice-cream selection button and see which are clicked)
								if buy -> validate purchase, engine.buy!
						
						end
					
					*/
					

				
			}catch(GameServerException ex){
				System.err.println(ex);
			}
			
			
			
			//dont have to call update on the things, that's done by the platform
			//TODO: Update shape positions if needed
		}

		private void handleUserEvents() throws GameServerException{
			if(sideArea.moveAttempted()){
				System.out.println("move attempted!");
				try{
					ActionUpdate update = engine.moveTo(activeTile);
					state.mergeUpdate(update);
					Tile.visualize(0, 0, state.tiles, state.player.coordinates);
				}catch(GameServerException e){
					sideArea.setError(e.getMessage());
				}
				
			}else if(sideArea.killAttempted()){
				
			}else if(sideArea.sellAttempted()){
				
			}else if(sideArea.buyAttempted()){
				
			}else{}
			
		}
		
		private void displayActiveTile(){
		}

		public void updateOverlay(Graphics g){
			sideArea.updateOverlay(g);
			renderPlayerInfo(g);
		}
		
		private void renderPlayerInfo(Graphics g){
			Font oldFont = g.getFont();
			g.setFont(new Font("Helvetica", Font.PLAIN, 14));
			
			String[] playerData = new String[11];
			playerData[0] = "YOU:";
			playerData[1] = "Money: " + state.player.money;
			playerData[2] = "Kills: " + state.player.kills;
			playerData[3] = "Sales: " + state.player.sales;				
			playerData[4] = "V: " + state.player.vanilla + " C: " + state.player.chocolate + " S: " + state.player.strawberry;
			playerData[5] = "turns remaining: " + state.player.turnsRemaining;
			playerData[6] = "";
			playerData[7] = "OPPONENT:";
			playerData[8] = "Money: " + state.other.money;
			playerData[9] = "Kills: " + state.other.kills;
			playerData[10] = "Sales: " + state.other.sales;
			String drawMe = "";
			for(int i = 0; i < playerData.length; i++){
				drawMe += playerData[i] + " ";
			}
			g.drawString(drawMe, 10, Platform.platform.getHeight() - 20);
			g.setFont(oldFont);
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