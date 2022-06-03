package edu.cwu;

import imgui.ImGui;
import imgui.flag.*;

public class MasterAudioSettings extends AudioSettings {

    private float[] masterVolume = {amplitude}; // Slider floats need a float array as an input.
    private int[] masterSampleRate = {sampleRate}; // Same goes with slider ints
    private int[] masterBitDepth = {bitDepth};
    private float step = 1f; // Keep volume locked to one-steps.

    public void MasterAudioGUI() {
        if (ImGui.beginTable("table1",2, ImGuiTableFlags.SizingFixedFit)) {
            ImGui.tableSetupColumn("col0", 0,0,0);
            ImGui.tableSetupColumn("col1", ImGuiTableColumnFlags.WidthStretch, -1, 1);

            ImGui.tableNextRow();
            ImGui.tableSetColumnIndex(0);
            ImGui.text("Volume:");
            ImGui.tableSetColumnIndex(1);

            ImGui.pushItemWidth(-1);
            ImGui.pushStyleVar(ImGuiStyleVar.FramePadding,0,0);
            if(ImGui.sliderFloat("##masterVolume",masterVolume,0,100, "%.2f")) {
                masterVolume[0] = Math.round(masterVolume[0]/step) * step;
            }
            ImGui.popStyleVar();
            //-----
            ImGui.tableNextRow();
            ImGui.tableSetColumnIndex(0);
            ImGui.text("Sample Rate:");
            ImGui.tableSetColumnIndex(1);

            ImGui.pushStyleVar(ImGuiStyleVar.FramePadding,0,0);
            if(ImGui.sliderInt("##masterSampleRate",masterSampleRate,2000,44100)) {

            }
            ImGui.popStyleVar();

            //-----
            ImGui.tableNextRow();
            ImGui.tableSetColumnIndex(0);
            ImGui.text("Bit-Depth:");
            ImGui.tableSetColumnIndex(1);

            ImGui.pushStyleVar(ImGuiStyleVar.FramePadding,0,0);
            if(ImGui.sliderInt("##masterBitDepth",masterBitDepth,1,16)) {

            }
            ImGui.popStyleVar();

            //----
            ImGui.endTable();
        }


    }

}
