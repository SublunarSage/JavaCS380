package edu.cwu;

import edu.cwu.tools.*;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class ImGuiLayer {

    private boolean showText = false;
    private int dummy = 0;

    // ImGui layers always have a beginning and ending, as it's basically functioning in a 'stack' like context
    public void imgui() {
        ImGui.begin("Cool Window", ImGuiWindowFlags.NoMove + ImGuiWindowFlags.NoResize
                + ImGuiWindowFlags.NoCollapse + ImGuiWindowFlags.NoTitleBar + ImGuiWindowFlags.NoSavedSettings);

        ImGui.setWindowSize(297,770); // Hardcode window size.
        ImGui.setWindowPos(728,0); // Hardcode window pos. This way it'll be the same on every computer.

        ImGui.beginChild("masterAudioSettings",0,80,true);
        TextTools.CenterText("MASTER AUDIO SETTINGS");
        //ImGui.text("MASTER AUDIO SETTINGS");
        // All functionality related to the Master Audio Settings (like master Volume) goes here.


        ImGui.pushTextWrapPos();
        ImGui.text("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
        ImGui.popTextWrapPos();
        // -----
        ImGui.endChild();

        ImGui.invisibleButton("hsplitter",-1,1);
        ImGui.beginChild("synthGenerator", 0, 500, true);
        TextTools.CenterText("AUDIO GENERATION");
        // All sound module GUIs should go down here. A sound module is something that generates sound and can be
        // controlled via a small GUI.


        // ----
        ImGui.endChild();

        ImGui.invisibleButton("hsplitter2",-1,1);
        ImGui.beginChild("videoSettings", 0, 0, true);
        TextTools.CenterText("VIDEO SETTINGS");
        // All functionality relating to configuring the graphical portion of the Oscilloscope goes here
        // (2D Osc, 1D Osc, etc).


        // ----
        ImGui.endChild();

        ImGui.end();
    }
}
