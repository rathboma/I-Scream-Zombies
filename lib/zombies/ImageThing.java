package zombies;

import game.*;
import java.awt.*;
import java.awt.image.*;

public class ImageThing extends Thing implements ImageObserver{
	int mWidth, mHeight, mX, mY;
	Image mImage;

	public ImageThing(String path, int width, int height){
		this(path, width, height, 0, 0);
	}
	
	public ImageThing(String path, int width, int height, int x, int y){
		this.mPolygon = false;
		this.movable = false;
		if (path != null){
			mImage = Platform.platform.getImage(Platform.platform.getBase(), path);
		} else {
			mImage = null;
		}
		mWidth = width;
		mHeight = height;		
		mX = x;
		mY = y;
	}

	public void update(Graphics g){
		if (mImage != null){
			g.drawImage(mImage, mX, mY, mWidth, mHeight, this);
		}
		super.update(g);
	}

	public boolean imageUpdate(Image img, int a, int b, int c, int d, int e){
		return true;
	}
}