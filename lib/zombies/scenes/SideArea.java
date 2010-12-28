package zombies.scenes;
import java.awt.*;
import zombies.*;
import game.*;
import java.util.*;
public class SideArea{
	GameScene master;
	MenuThing move;
	MenuThing kill;
	MenuThing sell;
	MenuThing buy;
	Game state;
	
	RectThing background;
	
	int width, height;
	String[] messages = new String[]{"", "", ""}; //message, warning, error
	public SideArea(GameScene master){
		this.master = master;
		move = new MenuThing(700, 300, "move");
		kill = new MenuThing(700, 300, "kill");
		sell = new MenuThing(700, 500, "sell");
		buy = new MenuThing(700, 600, "buy");
		this.height = Platform.platform.getHeight();
		this.width = Platform.platform.getWidth() / 3;
		background = new RectThing(Platform.platform.getWidth() - this.width, 0, width, height);
		background.setColor(Color.white);
		background.movable = false;
		Platform.platform.addThing(background);
	}
	
	Tile lastTile = null;
	long lastErrorWipe = 0;
	
	public void updateSideArea(Tile t, Game state){
		this.state = state;
		if(t == null && master.activeTile == null) master.activeTile = state.player.coordinates.copy();
		else if(t == null){}
		else{
			master.activeTile = new Coordinate(t.tileX, t.tileY);
			if(lastTile != null) lastTile.unHighlight();
			t.highlight();
			lastTile = t;
		}
		if(state.player.canMove) validateMove();
		else if(state.player.canAct) validateAct();
		else noButtons();
	}
	
	public void update(){
		long now = (new Date()).getTime();
		if(lastErrorWipe < now){
			lastErrorWipe = now + 5000;
			messages = new String[]{"", "", ""};
		}
		
	}
	
	private void validateMove(){
		Platform.platform.addThing(move);
		Platform.platform.removeThing(kill);
		Platform.platform.removeThing(sell);
		Platform.platform.removeThing(buy);
	}
	
	private void validateAct(){
		Platform.platform.removeThing(move);
		Platform.platform.addThing(kill);
		Platform.platform.addThing(sell);
		Platform.platform.addThing(buy);
		
	}
	private void noButtons(){
		Platform.platform.removeThing(kill);
		Platform.platform.removeThing(sell);
		Platform.platform.removeThing(buy);
		Platform.platform.removeThing(move);
	}
	
	
	public void updateOverlay(Graphics g){
		Font oldFont = g.getFont();
		g.setFont(new Font("Helvetica", Font.PLAIN, 14));
		
		for(int i = 0; i < messages.length; i++){
			g.drawString(messages[i], background.topLeft().x + 5, 10 + (i*15));
		}
		g.setFont(new Font("Helvetica", Font.PLAIN, 22));		
		g.drawString("selected: " + master.activeTile.x + ", " + master.activeTile.y, background.topLeft().x + 5, 60);
		
		g.setFont(oldFont);
		
		if(state.player.canMove)
			move.updateOverlay(g);
		else if(state.player.canAct){
			kill.updateOverlay(g);
			sell.updateOverlay(g);
			buy.updateOverlay(g);
		}
	}
	
	public void setError(String error){
		messages[1] = error;
		lastErrorWipe = (new Date()).getTime() + 5000;
	}
	
	public boolean moveAttempted(){
		if(move.clicked()) {
			move.unClick();
			return true;
			}
		return move.clicked();
	}
	
	public boolean killAttempted(){
		if(kill.clicked()){
			kill.unClick();
			return true;
		}
		return false;
	}
	
	public boolean sellAttempted(){
		if(sell.clicked()){
			sell.unClick();
			return true;
		}
		return false;
	}
	
	public boolean buyAttempted(){
		if(buy.clicked()){
			buy.unClick();
			return true;
		}
		return false;
	}
	
	
}