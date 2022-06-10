package edu.cwu;

import edu.cwu.tools.*;
import imgui.ImGui;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;

public class ImGuiLayer {

    private boolean showText = false;
    private int dummy = 0;
    MasterAudioSettings masterAudioSettings;
    AudioController masterAudioController;
    OscillatorSimpleSine testSine;

    public ImGuiLayer(AudioController mac) {
        masterAudioController = mac;
        masterAudioSettings = (MasterAudioSettings) masterAudioController.getAudioSettings();
        testSine = (OscillatorSimpleSine) masterAudioController.getOscillators();
        //testSine = new OscillatorSimpleSine(masterAudioController);
    }

    // ImGui layers always have a beginning and ending, as it's basically functioning in a 'stack' like context
    public void imgui() {
        ImGui.begin("Cool Window", ImGuiWindowFlags.NoMove + ImGuiWindowFlags.NoResize
                + ImGuiWindowFlags.NoCollapse + ImGuiWindowFlags.NoTitleBar + ImGuiWindowFlags.NoSavedSettings);

        ImGui.setWindowSize(297,770); // Hardcode window size.
        ImGui.setWindowPos(728,0); // Hardcode window pos. This way it'll be the same on every computer.

        ImGui.beginChild("masterAudioSettings",0,100,true);
        TextTools.CenterText("MASTER AUDIO SETTINGS");
        // All functionality related to the Master Audio Settings (like master Volume) goes here.
        masterAudioSettings.MasterAudioGUI();
        // -----
        ImGui.endChild();

        ImGui.invisibleButton("hsplitter",-1,1);
        ImGui.beginChild("audioGenerator", 0, 500, true);
        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing,0,16);
        TextTools.CenterText("AUDIO GENERATION");
        // All sound module GUIs should go down here. A sound module is something that generates sound and can be
        // controlled via a small GUI.

        testSine.invokeGUI(true);
        testSine.GUI();
        //testSine.update(); // This function causes the crackling. I think.


        // ----
        // Implement functionality relating to adding sounds to the audioGenerator Window

        ImGui.popStyleVar();
        ImGui.endChild();

        ImGui.invisibleButton("hsplitter2",-1,1);
        ImGui.beginChild("videoSettings", 0, 0, true);
        TextTools.CenterText("VIDEO SETTINGS");
        // All functionality relating to configuring the graphical portion of the Oscilloscope goes here
        // (2 channel Osc, 1 channel Osc, etc).
        if(ImGui.button("Change to A", -1,20)) {
            masterAudioController.setToColorA();

        }
        if(ImGui.button("Change to B", -1,20)) {
            masterAudioController.setToColorB();

        }

        // ----
        ImGui.endChild();

        ImGui.end();
    }

}
