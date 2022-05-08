package edu.cwu;
import imgui.ImGui;
import imgui.app.Application; // High level abstraction layer
import imgui.app.Configuration;


public class HelloImGui extends Application {
    @Override
    protected void configure(Configuration config) {
        config.setTitle("Dear ImGui is Awesome!");
    }

    @Override
    public void process() {
        ImGui.text("Hello, World!");
    }

}