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
	
	public RectThing background;
	
	int width, height;
	String[] messages = new String[]{"", "", ""}; //message, warning, error
	public SideArea(GameScene master){
		this.height = Platform.platform.getHeight();
		this.width = Platform.platform.getWidth() / 3;
		background = new RectThing(Platform.platform.getWidth() - this.width, 0, width, height);
		background.setColor(Color.white);
		background.movable = false;
		Platform.platform.addThing(background);
		this.master = master;
		move = new MenuThing(background.topLeft().x + 5, Platform.platform.getHeight() - 100, "move");
		kill = new MenuThing(background.topLeft().x + 5, Platform.platform.getHeight() - 100, "kill");
		sell = new MenuThing(background.topLeft().x + 105, Platform.platform.getHeight() - 100, "sell");
		buy = new MenuThing(background.topLeft().x + 210, Platform.platform.getHeight() - 100, "buy");

	}
	
	Tile lastTile = null;
	long lastErrorWipe = 0;
	
	public void updateSideArea(Game state){
		this.state = state;

		if(state.player.canMove) validateMove();
		else if(state.player.canAct) validateAct();
		else noButtons();
		
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
		
		g.setFont(new Font("Helvetica", Font.PLAIN, 28));
		for(int i = 0; i < messages.length; i++){
			int theX = Platform.platform.getWidth()/2 - (Platform.platform.stringWidth(messages[i], g)/2);
			g.drawString(messages[i], theX, 10 + (i*30));
		}
		g.setFont(new Font("Helvetica", Font.PLAIN, 22));		
		int nextString = 60;
		
		g.drawString("selected: " + Tile.highlighted.tileX + ", " + Tile.highlighted.tileY, background.topLeft().x + 5, nextString);
		nextString += 20;
	
		g.setFont(new Font("Helvetica", Font.PLAIN, 14));
		Tile selected = Tile.highlighted;
		g.drawString("zombies: " + selected.zombies, background.topLeft().x + 5, nextString);
		nextString += 15;
		
		if(Tile.highlighted.store){
			g.drawString("you can buy:", background.topLeft().x + 5, master.buttonStart - 20);			
		}else{
			g.drawString("you can sell: (via " + selected.customers.length + " customers)", background.topLeft().x + 5, master.buttonStart - 20);
		}
		
		
		
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