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
	
	int maxCustomers = 3;
	ArrayList forPurchase = new ArrayList();
	
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
				updateState();
				Tile.visualize(state.tiles, state.player.coordinates);
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
		Platform.platform.removeThing(bottomPanel);
		//Platform.platform.removeThing(mBackground);
	}
	
	private void updateState(){
		try{
			if(state != null) Tile.removeAllTilesFromPlatform(state.tiles);
			state = engine.updateData();
			Tile.visualize(state.tiles, state.player.coordinates);
		}catch(GameServerException ex){sideArea.setError(ex.getMessage());}
	}

		public void update(){
			try{
				if(!state.player.isTurn()){
					if(engine.checkForTurn()) updateState();
				}
				
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
				displayPurchases();
				handleUserEvents();
				Tile.showPlayers(state.player.coordinates, state.other.coordinates);
					
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
		
		private void displayPurchases(){
			Tile t = state.tiles[activeTile.x][activeTile.y];
			int baseNum = t.customers.length > 0 ? 3 : 0;
			int total = t.customers.length + baseNum;
			
			//forPurchase > total
			for(int i = 0; i < forPurchase.size(); i++){
				if(i >= total){
					Platform.platform.removeThing((Thing)forPurchase.get(i));
					forPurchase.remove(i);					
				}
			}
			//total > forPurchase
			int diff = total - forPurchase.size();
			for(int i = 0; i < diff; i++){
				IceCreamButton b = new IceCreamButton();
				forPurchase.add(b);
				Platform.platform.addThing(b);
			}
			//now we have the right number of buttons.
			if(baseNum > 0){
				((IceCreamButton)forPurchase.get(0)).updateText("V", state.vanillaPrice, t.customers[0].id);
				((IceCreamButton)forPurchase.get(1)).updateText("C", state.chocolatePrice, t.customers[0].id);
				((IceCreamButton)forPurchase.get(2)).updateText("S", state.strawberryPrice, t.customers[0].id);
			}
			
			for(int i = 3; i < forPurchase.size(); i++){
				int ij = i - 3;
				IceCreamButton b = (IceCreamButton)forPurchase.get(i);
				b.updateText(t.customers[ij].favoriteType, t.customers[ij].favoritePrice, t.customers[ij].id);
			}
			for(int i = 0; i < forPurchase.size(); i++)
				{
					IceCreamButton b = ((IceCreamButton)forPurchase.get(i));
					b.setX(sideArea.background.topLeft().x + 5 + 100);
					b.setY(300 + 20*i);
					b.update();
					}
		}



		private void handleUserEvents() throws GameServerException{
			if(sideArea.moveAttempted()){
				System.out.println("move attempted!");
				try{
					ActionUpdate update = engine.moveTo(activeTile);
					state.mergeUpdate(update);
				}catch(GameServerException e){
					sideArea.setError(e.getMessage());
				}
				
			}else if(sideArea.killAttempted()){
				try{
					ActionUpdate update = engine.postKill();
					state.mergeUpdate(update);
				}catch(GameServerException e) {sideArea.setError(e.getMessage());}
				
			}else if(sideArea.sellAttempted()){
				System.out.println("sell attempted!");
				try{
					ActionUpdate update = engine.postSell(IceCreamButton.highlighted.customer, IceCreamButton.highlighted.flavor);
					state.mergeUpdate(update);
					
				}catch(GameServerException e){
					sideArea.setError(e.getMessage());
				}
				
			}else if(sideArea.buyAttempted()){
				
			}else{}
			
		}
		
		private void displayActiveTile(){
		}

		public void updateOverlay(Graphics g){
			sideArea.updateOverlay(g);
			renderPlayerInfo(g);
			for(int i = 0; i < forPurchase.size(); i++)
				((IceCreamButton)forPurchase.get(i)).updateOverlay(g);
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