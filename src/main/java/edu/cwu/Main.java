package edu.cwu;


import imgui.app.Application;

public class Main {
    public static void main(String[] args) {
        MasterAudioSettings masterAudioSettings = new MasterAudioSettings();
        ImGuiLayer imGuiLayer = new ImGuiLayer();
        imGuiLayer.setMasterAudioSettings(masterAudioSettings);

        Window window = new Window(imGuiLayer);
        window.init();
        window.run();
        window.destroy();

    }
}