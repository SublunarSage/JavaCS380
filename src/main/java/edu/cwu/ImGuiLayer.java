package edu.cwu;

import imgui.ImGui;
import imgui.ImGuiWindowClass;
import imgui.ImVec2;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.internal.ImGuiWindow;

public class ImGuiLayer {

    private boolean showText = false;
    private int dummy = 0;

    public void imgui() {
        ImGui.begin("Cool Window", ImGuiWindowFlags.NoMove + ImGuiWindowFlags.NoResize + ImGuiWindowFlags.NoCollapse);

        if (ImGui.button("Click me!")) showText = !showText;



        if(showText) {
            ImGui.text("You clicked me!");
            ImGui.sameLine();
            if (ImGui.button("Unclick button")) showText = !showText;
            dummy += 1;
            ImGui.text("Cool text variable: " + dummy);
            ImGui.text("FPS: " + ImGui.getIO().getFramerate());
            ImGui.text("Nanotime: " + System.nanoTime() / (double) 1000000000L);
        }

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0,8);

        ImGui.invisibleButton("hsplitter",-1,80);
        ImGui.beginChild("videoChild", 0, 200, true);
        ImGui.text("Hello world!");
        ImGui.endChild();

        ImGui.popStyleVar();

        ImGui.end();
    }
}
