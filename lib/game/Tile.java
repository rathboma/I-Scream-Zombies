package game;


import java.awt.*;
import java.util.*;


public class Tile extends RectThing {
	public boolean isFloor;
	public boolean isDoor;
	public boolean isPowerup;
	public boolean isExit;
	public int[] keySequence = {};
	public int note;
	int toPlay = 0;
	double playAt = 0;
	double donePlaying = 0.0;
	
	public Tile(boolean isFloorTile, int x, int y, int tileSize){
		super(x, y, tileSize, tileSize);
		this.isFloor = isFloorTile;
		this.setColor(Color.black);
	
	}
	public void setDoor(int[] key){
		this.isDoor = true;
		this.keySequence = key;

	}
	public void setPowerup(int note){
		this.isPowerup = true;
		this.note = note;

	}
	public void dim(){
		double currentTime = System.currentTimeMillis() / 1000.0;
		if(donePlaying < currentTime) this.setColor(Color.darkGray);
		if(isExit) this.setColor(Color.green);

		}
	
	public void lightUp(){
		if(isDoor && this.color != Color.darkGray) return;
		this.setColor(TileSpec.wallColor);
		if(isFloor) this.setColor(TileSpec.floorColor);
		if(isDoor) this.setColor(TileSpec.doorColor);
		if(isPowerup) this.setColor(TileSpec.toneColors[this.note]);
		if(isExit) this.setColor(Color.green);
	}
	
	public boolean sequenceMatch(ArrayList playerSequence){
		int mark = 0;
		//System.out.print("player sequence : ");
		for(int i = 0; i < playerSequence.size(); i++){
			//System.out.print("" +((Integer)playerSequence.get(i)).intValue() + " ");
			int note = ((Integer)playerSequence.get(i)).intValue();
			if(keySequence[mark] == note){
				mark++;
			}else{mark = 0;}
			if(mark == keySequence.length) return true;
		}
		//System.out.println("");
		return false;
	}
	public void unlock(){
		isDoor = false;
	}
	public void playSequence(SoundController sounds){
		double currentTime = System.currentTimeMillis() / 1000.0;
		if(!isDoor) return;
		//play each note 0.2 seconds apart
		//if the player starts playing, stop playing
		//if you've just finished playing the whole sequence, wait 2 seconds before playing again.
		if(toPlay == keySequence.length){
			playAt = currentTime + 2;
			toPlay = 0;
			return;
		}
		if(playAt <= currentTime){
			sounds.addPlayerSound(keySequence[toPlay]);
			this.donePlaying = currentTime + 0.5;
			this.setColor(TileSpec.toneColors[keySequence[toPlay]]);
			toPlay++;
			playAt = currentTime + 0.5;
		}
		
	}
	public void resetSequence(){
		toPlay = 0;
	}
	
	
	
	
}
