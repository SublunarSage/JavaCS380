package edu.cwu;


import imgui.app.Application;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Hello world!");
        //new HelloWorld().run();
        //Application.launch(new HelloImGui());

        Window window = new Window(new ImGuiLayer());
        window.init();
        window.run();
        window.destroy();

    }
}