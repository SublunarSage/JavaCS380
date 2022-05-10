package edu.cwu;

import javax.sound.sampled.*;
import java.util.*;


public class Sinewave {
	
	
	protected static final int SAMPLE_RATE = 16 * 1024;
	
	public Sinewave() {
		
	}
	
	public static void play(double freq, int length) throws LineUnavailableException {
		final AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, true); //create a AudioFormat object
	    SourceDataLine line = AudioSystem.getSourceDataLine(af); //get a Data line in order to play the sine wave
	    line.open(af, SAMPLE_RATE); //open the line
	    line.start(); //start the line
	    
	    byte [] tone = createSinWaveBuffer(freq, length); //create the sine wave
	    
	    line.write(tone, 0, tone.length); //write (play) the sine wave
	    
	    line.drain(); //drain the line
	    line.close(); //close the line
	}

	public static byte[] createSinWaveBuffer(double freq, int ms) {
	    int samples = (int) ((ms * SAMPLE_RATE) / 1000);
	    byte[] output = new byte[samples];
	    //
	    double period = (double) SAMPLE_RATE / freq;
	    for (int i = 0; i < output.length; i++) {
	        double angle = 2.0 * Math.PI * i / period;
	        output[i] = (byte) (Math.sin(angle) * 127f);
	    }

	    return output;
	}
}
