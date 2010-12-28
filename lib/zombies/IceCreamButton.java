package zombies;
import java.awt.*;


public class IceCreamButton extends MenuThing{
	public static IceCreamButton highlighted = null;
	
	public String flavor;
	public int customer;
	public double price;
	
	public IceCreamButton(){
		super(0, 0, 200, 20, "");
		this.mFontSize = 14;
		this.setLineColor(Color.white);
		this.setColor(Color.white);
	}
	
	public IceCreamButton(String flavors, double price){
		super(0, 0, 200, 20, flavors + " for $" + price);
		this.mFontSize = 14;
		this.setLineColor(Color.white);
		this.setColor(Color.white);
		this.flavor = flavors;
		this.price = price;
	}
	public void updateText(String flavors, double price, int id){
		this.flavor = flavors;
		this.price = price;
		this.customer = id;
		this.setText(flavors + " for $" + price);
	}
	
	
	public void update(){
		if( IceCreamButton.highlighted == this) this.setColor(Color.lightGray);
		else this.setColor(Color.white);
		
	}
	
	
	public boolean mouseDown(int x, int y){
		IceCreamButton.highlighted = this;
		return super.mouseDown(x, y);
	}

	
}