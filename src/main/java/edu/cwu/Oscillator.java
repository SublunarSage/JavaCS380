package edu.cwu;

public class Oscillator {
    protected int wavePosition = 0;

    protected AudioController audioController;

    public Oscillator(AudioController ac) {
        audioController = ac;
    }

    public double nextSample() {
        return 0;
    }


}
