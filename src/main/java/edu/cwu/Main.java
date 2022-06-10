package edu.cwu;


import imgui.app.Application;

public class Main {

    public static int waveP = 0;
    public static void main(String[] args) {


        //AudioBuffer audioBuffer = new AudioBuffer();
        //AudioThread audioThread = new AudioThread(() -> audioBuffer.getAudioBuffer()); // Crackly sound.

        // Non-crackly sound
        /*
        boolean shouldGenerate = true;
        AudioThread audioThread = new AudioThread(() -> {
           if(!shouldGenerate) return null;
           short[] s = audioBuffer.getAudioBuffer(); // Also non-crackly?
           for(int i = 0; i < s.length; ++i) {
               s[i] = (short) (Short.MAX_VALUE * Math.sin((2*Math.PI*220)/44100 * waveP++));
           } // Commenting this out makes the crackly noise again...
           return s;
        });*/
        //MasterAudioSettings masterAudioSettings = new MasterAudioSettings();

        AudioController audioController = new AudioController();

        ImGuiLayer imGuiLayer = new ImGuiLayer(audioController);

        Window window = new Window(imGuiLayer, audioController);
        window.init();
        window.run();
        window.destroy();

    }
}