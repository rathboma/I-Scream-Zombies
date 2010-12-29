package zombies;
import java.awt.*;
import game.*;
import zombies.scenes.*;
public class IceCreamButton extends MenuThing{
	public static IceCreamButton highlighted = null;
	
	public String flavor;
	public int customer;
	public double price;
	GameScene scene;
	public int clickCount = 0;
	public IceCreamButton(GameScene owner){
		super(0, 0, 200, 20, "");
		this.mFontSize = 14;
		scene = owner;
		this.setColor(Color.white);
	}
	
	public IceCreamButton(String flavors, double price){
		super(0, 0, 200, 20, flavors + " for $" + price);
		this.mFontSize = 14;
		this.setColor(Color.white);
		this.flavor = flavors;
		this.price = price;
	}
	public void updateText(String flavors, double price, int id){
		this.flavor = flavors;
		this.price = price;
		this.customer = id;
		String text = flavors + " for $" + price;
		if(customer == -1) text += " x" + clickCount;
		this.setText(text);
	}
	
	public void updateOverlay(Graphics g){
		if(Platform.platform.containsThing(this)) super.updateOverlay(g);
	}
	public void select(){
		if(!scene.state.playerTile().contains(customer) && !scene.state.playerTile().store) return;
		this.clickCount++;
		if(IceCreamButton.highlighted != null && IceCreamButton.highlighted != this) IceCreamButton.highlighted.unSelect();
		IceCreamButton.highlighted = this;
		this.setColor(Color.lightGray);
	}
	public void unSelect(){
		this.setColor(Color.white);
		this.clickCount = 0;
		if(IceCreamButton.highlighted == this) IceCreamButton.highlighted = null;
	}
	
	public boolean mouseDown(int x, int y){
		select();
		return super.mouseDown(x, y);
	}

	
}