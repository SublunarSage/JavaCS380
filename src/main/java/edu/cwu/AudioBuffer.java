package edu.cwu;

public class AudioBuffer {

    private short[] audioBuffer;
    private static int wavePos = 0;

    public AudioBuffer() {
        audioBuffer = new short[AudioThread.BUFFER_SIZE];
    }

    public short[] getAudioBuffer() { return audioBuffer; }
    public void setAudioBuffer(short[] newBuffer) {
        if(newBuffer.length != AudioThread.BUFFER_SIZE)
        {
            System.out.println("Buffer sizes aren't the same!");
            return;
        }
        audioBuffer = newBuffer;
    }

    public void setAudioBufferSample(double sample, int index) {

    }


}
