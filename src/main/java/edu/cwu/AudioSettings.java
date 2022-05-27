package edu.cwu;

public class AudioSettings {


    float amplitude = 1f; // Can be used for volume when implemented for the master audio settings.
    int sampleRate = 44100; // Most computers use 44.1 kHz. Affects how high a sound's frequencies can go.
    int bitDepth = 24; // Most sounds are 24bit. Affects the 'resolution' of the sound.
    // private float panning = 0f; // Eventual implementation of left/right panning.

    public float getAmplitude() { return amplitude; }
    public int getSampleRate() { return sampleRate; }
    public int getBitDepth() { return bitDepth; }



    public AudioSettings() {}
    public AudioSettings(float a, int s, int b) {
        amplitude = a;
        sampleRate = s;
        bitDepth = b;
    }



}
