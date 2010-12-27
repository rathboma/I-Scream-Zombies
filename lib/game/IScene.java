package game;
import java.awt.*;


public interface IScene {
	
	public void addChild(IScene child);
	public void addParent(IScene parent);
	public void setup();
	public void finish();
	public void update();
	public void updateOverlay(Graphics g);
	public boolean childReady();
	public IScene getChild();
	public IScene getParent();
	public boolean done();
	
	
}