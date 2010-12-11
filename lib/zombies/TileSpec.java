package zombies;

// GENERIC THING

import java.awt.*;
import java.util.*;

public class TileSpec
{
	public static class Coordinate{
		public int x;
		public int y;
		public Coordinate(int a, int b){x = a; y = b;}
	}
	public static class Door extends TileSpec.Coordinate{
		public Door(int a, int b, int[] keys){
			super(a, b);
			keySequence = keys;
		}
		public int[] keySequence;
	}
	public static class Powerup extends TileSpec.Coordinate{
		public Powerup(int a, int b, int c){super(a, b); note=c;}
		public int note;
		
	}
	public int x;
	public int y;
	public static int width = 28;
	public static int height = 19;
	public static int tileWidth = 24;
	public static Color floorColor = Color.lightGray;
	public static Color doorColor = Color.red;
	public static Color powerupColor = Color.blue;
	public static Color wallColor = Color.gray;
	public static Color[] toneColors = {Color.black, new Color(0, 255, 255), new Color(0, 153, 255), new Color(0, 51, 255), new Color(0, 51, 102)};
	public static int border = 20;
	public static int bottom_border = border + height*tileWidth + 40;
	
	public static boolean[][] loadFloorTiles(){
		boolean[][] results = new boolean[TileSpec.width][TileSpec.height];
		

		String floor = "1.1,1.2,1.3,2.3,3.3,4.3,5.3,6.3,6.4,6.5,6.6,6.7,6.8,7.8,7.9,7.10,7.11,7.12,7.13,7.14,7.15,7.16,6.10,5.10,5.11,5.12,4.12,3.12,3.13,3.14,3.15,3.16,4.16,5.16";
		floor += ",8.16,9.16,9.15,9.14,9.13,9.12,9.11,9.10,9.9,9.8,9.7,9.6,9.5,9.4,9.3,10.3,11.3,12.3,13.3,14.3,15.3,16.3,17.3,18.3,19.3,20.3,21.3,22.3,23.3,24.3,25.3,26.3";
		floor += ",12.4,12.5,12.6,13.6,14.6,15.6,15.7,16.7,17.7,18.7,15.8,15.9,14.9,13.9,12.9,11.9,10.9,9.9,10.11,11.11,12.11,13.11,14.11,15.11,16.11,17.11,18.11,19.11,20.11,21.11,22.11,23.11,24.11,25.11,26.11";
		floor += ",26.4,26.5,26.6,26.7,26.8,26.9,26.10,25.9,24.9,23.9,22.9,21.9,20.9,20.8,20.7,20.6,20.5,21.5,22.5,23.5,24.5";
		floor += ",13.16,14.16,15.16,16.16,17.16,18.16,19.16,20.16,21.16,22.16,23.16,24.16,25.16,26.16,13.15,13.14,14.14,15.14,16.14,19.12,19.13,19.14,19.15,20.14,21.14,22.14,23.14,24.14,25.14,26.14,26.17,26.18,26.19,27.19,28.19";
		String[] split = floor.split(",");
		
		for(int i = 0; i < split.length; i++){
			String item = split[i];
			String[] points = item.split("\\.");
			int pointX = Integer.parseInt(points[0]) - 1;
			int pointY = Integer.parseInt(points[1]) - 1;
			results[pointX][pointY] = true;
		}
		return results;
	}
	public static ArrayList getDoors(){
		ArrayList results = new ArrayList();
				
		int[] keys2 = {1, 1, 1}; //blocks tone 2
		results.add(new TileSpec.Door(5, 6, keys2));
		
		int[] keysA = {1, 1, 1}; //blocks tone 2
		results.add(new TileSpec.Door(2, 11, keysA));
		
		
		int[] keys3 = {1, 1, 2, 2}; //blocks area 2
		results.add(new TileSpec.Door(7, 15, keys3));
		
		int[] keys4 = {1, 2, 1, 2, 1, 1}; //blocks tone 3
		results.add(new TileSpec.Door(24, 8, keys4));
		
		int[] keysC = {1,2,3,1,2,3};
		results.add(new TileSpec.Door(18, 11, keysC));
		
		int[] keys5 = {3, 2, 1, 2, 3, 1, 1};
		results.add(new TileSpec.Door(19, 13, keys5));
		
		int[] keys6 = {3, 3, 3, 1, 1, 1, 2};
		results.add(new TileSpec.Door(12, 14, keys6));
		
		int[] keys7 = {4, 3, 4, 3, 1, 2, 3, 4, 1};
		results.add(new TileSpec.Door(24, 15, keys7));
		
		
		return results;
	}
	
	public static ArrayList getPowerups(){
		ArrayList results = new ArrayList();
		results.add(new TileSpec.Powerup(2, 2, 1));
		results.add(new TileSpec.Powerup(4, 15, 2));
		results.add(new TileSpec.Powerup(23, 4, 3));
		results.add(new TileSpec.Powerup(25, 13, 4));
		return results;
	}
	
}