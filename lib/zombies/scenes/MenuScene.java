package zombies.scenes;

import java.util.*;
import java.awt.*;
import game.*;
import zombies.*;
public class MenuScene implements IScene{
	IScene mChild = null;
	IScene mParent = null;
	ArrayList mButtons;
	ImageThing mBackground;
	MenuThing start;
	MenuThing ai;
	static boolean useAi = false;
	String title = "I SCREAM ZOMBIES";
	Font mFont;

	public MenuScene(){
		mButtons = new ArrayList();
		mParent = null;
	}

	public void setup(){
		mFont = new Font("Helvetica", Font.PLAIN, 90);
		initializeBackground();
		initializeButtons();
	}

	public void addChild(IScene child){
		mChild = child;
	}

	//This is the simplest implementation of a finish method.
	public void finish(){
		for(int i = 0; i < mButtons.size(); i++){
			Thing t = (Thing)mButtons.get(i);
			Platform.platform.removeThing(t);
		}
		Platform.platform.removeThing(mBackground);
	}

	public void update(){
		//dont have to call update on the things, that's done by the platform
		//TODO: Update shape positions if needed
	}

	public void updateOverlay(Graphics g){
		g.setColor(Color.white);
		Font old = g.getFont();
		g.setFont(new Font("Helvetica", Font.PLAIN, 40));
		g.drawString(title, 10, 50);
		g.setFont(old);
		for(int i = 0; i < mButtons.size(); i++){
			MenuThing m = (MenuThing)mButtons.get(i);
			m.updateOverlay(g);
		}
		//nothing
	}

	public boolean childReady(){
		//some condition
		return start.clicked() || ai.clicked();
	}

	public IScene getChild(){
		if(ai.clicked()) MenuScene.useAi = true;
		start.unClick();
		ai.unClick();
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
		mBackground = new ImageThing("main_background.jpg", Platform.platform.getWidth(), Platform.platform.getHeight());
		Platform.platform.addThing(mBackground);
	}

	private void initializeButtons(){
		System.out.println("adding buttons");
		Image image = Platform.platform.getImage(Platform.platform.getBase(), "Button_newgame.png");
		start = new MenuThing(Platform.platform.getWidth() / 2.0, 300, 150, 100, image);
		image = Platform.platform.getImage(Platform.platform.getBase(), "ai_game.png");
		ai = new MenuThing(Platform.platform.getWidth() / 2.0, 425, 61, 27, image);
		
		Platform.platform.addThing(start);
		Platform.platform.addThing(ai);
		mButtons.add(start);
		mButtons.add(ai);
	}
}
