package edu.cwu;

import edu.cwu.tools.TextTools;
import imgui.ImGui;
import imgui.flag.ImGuiStyleVar;

public class OscillatorSimpleSine extends Oscillator {

    private int frequency = 110;
    private int[] frequencyGUI = {frequency};
    private int numHarmonics = 1;
    private int maxHarmonics = 10;
    private int minHarmonics = 1;
    private int[] numHarmonicsGUI = {numHarmonics};
    private boolean GUIActive = false;

    public boolean isGUIActive() { return GUIActive; }
    public void invokeGUI(boolean status) { GUIActive = status; }
    private int activeHarmonics = numHarmonics;
    public int getActiveHarmonics() { return activeHarmonics; }

    public OscillatorSimpleSine(AudioController ac) {
        super(ac);
    }

    @Override
    public double nextSample() {
        wavePosition++;
        double sample = Math.sin(2*Math.PI*frequency * (wavePosition - 1) / audioController.getAudioSettings().sampleRate);
        for(int h = minHarmonics+1; h <= numHarmonics && h <= maxHarmonics; h++) {
            sample += Math.sin(2*Math.PI*frequency*(wavePosition - 1)*h / audioController.getAudioSettings().sampleRate);
        }
        sample = sample/numHarmonics; // To keep overflow/underflow from happening
        return sample;
    }

    public void GUI() {
        if(GUIActive) {
            ImGui.beginChild("SoundModuleGUI", 0,100,true);
            TextTools.CenterText("Simple Sine");

            ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing,0,5);
            ImGui.text("Frequency: ");
            ImGui.sameLine();
            if(ImGui.sliderInt("##frequency",frequencyGUI,110,880)) {
                frequency = frequencyGUI[0];
            }
            ImGui.text("Harmonics: ");
            ImGui.sameLine();
            if(ImGui.sliderInt("##harmonics",numHarmonicsGUI,minHarmonics,maxHarmonics)) {
                numHarmonics = numHarmonicsGUI[0];
            }
            if(ImGui.button("Play")) {
                if(!audioController.shouldGenerate())
                {
                    audioController.play();
                }
                else audioController.stop();
                System.out.println(audioController.shouldGenerate());
            }
            ImGui.text("WavePos: " + wavePosition);
            ImGui.popStyleVar();

            ImGui.endChild();
        }
    }

}
