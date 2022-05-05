package edu.cwu;

import javax.sound.sampled.*;

public class Main {
	
	protected static final int SAMPLE_RATE = 16 * 1024;
	
	public static void play(double freq, int length) throws LineUnavailableException {
		final AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
	    SourceDataLine line = AudioSystem.getSourceDataLine(af);
	    line.open(af, SAMPLE_RATE);
	    line.start();
	    
	    byte [] tone = createSinWaveBuffer(freq, length);
	    
	    line.write(tone, 0, tone.length);
	    
	    line.drain();
	    line.close();
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

	
	
	
    public static void main(String[] args) throws LineUnavailableException {

        System.out.println("Hello World!");
        
        play(1200, 2000);
    }
}

