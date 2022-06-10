package edu.cwu;

import org.joml.Vector4f;

public class AudioController {
    private boolean shouldGenerate;
    private final int MAX_SAFE_VOLUME = 150;
    private OscillatorSimpleSine testOsc = new OscillatorSimpleSine(this);
    private final MasterAudioSettings masterAudioSettings = new MasterAudioSettings();
    private final AudioBuffer masterAudioBuffer = new AudioBuffer();
    private final AudioThread masterAudioThread = new AudioThread(() -> {
        if(!shouldGenerate) return null;

        short[] bufferSource = new short[AudioThread.BUFFER_SIZE];
        masterAudioBuffer.setAudioBuffer(bufferSource);
        for(int i = 0; i < AudioThread.BUFFER_SIZE; ++i) {
            //bufferSource[i] = (short) (Short.MAX_VALUE * Math.sin((2*Math.PI*110)/44100 * wavePos++)); // Works!
            bufferSource[i] = (short) (Short.MAX_VALUE * testOsc.nextSample() * masterAudioSettings.amplitude/MAX_SAFE_VOLUME);
        }
        return bufferSource;
    });

    public Oscillator getOscillators() { return testOsc; }
    public AudioSettings getAudioSettings() { return masterAudioSettings; }
    public boolean shouldGenerate() { return shouldGenerate; }
    public AudioBuffer getAudioBuffer() { return masterAudioBuffer; }

    public void closeThread() { masterAudioThread.close(); }
    public void play() {
        if(!masterAudioThread.isRunning()) {
            shouldGenerate = true;
            masterAudioThread.triggerPlayback();
        }
    }
    public void stop() {
        if(masterAudioThread.isRunning()) {
            shouldGenerate = false;
        }

    }

    private Vector4f colorA = new Vector4f(0.1f,0.2f,0.3f, 1);
    private Vector4f colorB = new Vector4f(0.1f, 0.2f, 0.3f, 1);
    private Vector4f currentColor = colorA;

    public Vector4f getCurrentColor() {
        return currentColor;
    }

    public void setToColorA() {
        currentColor = colorA;
    }
    public void  setToColorB(){
        currentColor = colorB;
    }
}
