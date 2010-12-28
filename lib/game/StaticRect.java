package game;
import zombies.*;

// A RECTANGLE SHAPED THING

public class StaticRect extends ImageThing {
      protected double _Width;
      protected double _Height;
			
      public StaticRect(String path, double x, double y, double width, double height) {
            super(path, (int) width, (int) height, (int) x, (int) y);
            this.x = x + width / 2;
            this.y = y + height / 2;
            X[0] = x;
            Y[0] = y;
            X[1] = x + width;
            Y[1] = y;
            X[2] = x + width;
            Y[2] = y + height;
            X[3] = x;
            Y[3] = y + height;
            setShape(X, Y, 4);
            _Width = width;
            _Height = height;
      }
}
