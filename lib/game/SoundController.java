package game;
import java.awt.*;
import java.util.*;

public class SoundController {
	
	public static SoundController active = new SoundController();
	
	MidiSynth synth = new MidiSynth();
	int scale[] = {2, 4, 6, 7, 9, 11, 13, 14, 16, 18, 19, 21, 23, 25, 26};
	double noteTime[] = new double[scale.length];
	public void playNote(int note){
		double currentTime = System.currentTimeMillis() / 1000.0;
		synth.noteOn(0, 64 + scale[note], 63);
    	noteTime[note] = currentTime + 0.5;
	}

	public void housekeeping(){
		double currentTime = System.currentTimeMillis() / 1000.0;
		for(int i = 0; i < noteTime.length; i++){
			if(noteTime[i] < currentTime){
				synth.noteOff(0, 64 + scale[i], 63);
			}
		}
	}

	
}


