package zombies;

import game.*;
import java.awt.*;
import java.awt.image.*;

public class MenuThing extends StaticRect{
	Color mTextColor = Color.blue; // change me!!
	protected int mFontSize = 40;
	boolean mClicked = false;
	String mText;
	Image mImage = null;
	Image mPrimary;
	public Image alternateImage;
	double mWidth, mHeight, mTopX, mTopY;
	public boolean allowClicks = true;
	public int number;

	public void usePrimaryImage(){
		mImage = mPrimary;
	}
	public void useAlternateImage(){
		mImage = alternateImage;
	}

	private MenuThing(double centerX, double centerY, double width, double height){
		super(null, centerX - width/2.0, centerY - height/2.0, width, height);
		mTopX = centerX - width/2.0;
		mTopY = centerY - height/2.0;
		mWidth = width;
		mHeight = height;
		this.setColor(Color.gray);
		this.setLineColor(Color.gray);		
	}

	public MenuThing(double cX, double cY, double w, double h, String t){
		this(cX, cY, w, h);
		this.mPolygon = true;
		mText = t;
	}

	public MenuThing(double centerX, double centerY, String text){
		this(centerX, centerY, 100, 55, text);
		mText = text;
	}

	
	public MenuThing(double centerX, double centerY, Image image){
		this(centerX, centerY, 50, 50);
		this.mImage = this.mPrimary = image;
		this.mPolygon = false;
	}
	public MenuThing(double centerX, double centerY, double w, double h, Image image){
		this(centerX, centerY, w, h);
		this.mImage = this.mPrimary = image;
		this.mPolygon = false;
	}

	public void setText(String t){
		mText = t;
	}
	
	public void update(Graphics g){
		if(!this.mPolygon) g.drawImage(mImage, (int)mTopX, (int)mTopY, (int)mWidth, (int)mHeight, this);
		super.update(g);
	}

	public void updateOverlay(Graphics g){
		if(mText == null) return;
		//System.out.println("overlay");
		Font font = new Font("Helvetica", Font.PLAIN, mFontSize);
		g.setFont(font);
		g.setColor(Color.black);
		g.drawString(mText, textX(g), textY(g));
	}

	public boolean mouseDown(int x, int y){
		if(allowClicks) mClicked = true;
		return super.mouseDown(x, y);
	}

	public boolean clicked(){
		return mClicked;
	}

	public void unClick(){
		mClicked = false;
	}

	private int textX(Graphics g){
		return (int)(this.getX() - (Platform.getStringWidth(mText, g) / 2.0));
	}

	private int textY(Graphics g){
		return (int)(this.getY()+ mFontSize/2.0 - 5);
	}
}
