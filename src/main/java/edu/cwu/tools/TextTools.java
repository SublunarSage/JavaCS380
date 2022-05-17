package edu.cwu.tools;

import imgui.ImGui;

// Custom tool for text manipulation in ImGui.
public class TextTools {

    public static void CenterText(String text) // Centers given text in the middle of an ImGui window/subwindow
    {
        ImGui imTool = new ImGui(); // Have to assign ImGui class manually because of calcTextSize not being a static method

        float windowWidth = ImGui.getWindowSize().x;
        float textWidth = imTool.calcTextSize(text).x;

        ImGui.setCursorPosX((windowWidth - textWidth) * .5f);
        ImGui.text(text);

    }
}
