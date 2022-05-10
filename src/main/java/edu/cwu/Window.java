package edu.cwu;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private String glslVersion = null;
    private long windowPtr;
    private final ImGuiLayer imguiLayer;

    private double dummyVar = 0;


    public Window(ImGuiLayer layer) {
        imguiLayer = layer;

    }

    public void init() {
        initWindow();
        initImGui();
        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init(glslVersion);
    }

    public void destroy() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(windowPtr);
        glfwDestroyWindow(windowPtr);
        glfwTerminate();

    }

    private void initWindow() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() ) {
            //throw new IllegalStateException("Unable to initialize GLFW");
            System.out.println("Unable to initialize GLFW");
            System.exit(-1);
        }

        glslVersion = "#version 130";
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        windowPtr = glfwCreateWindow(1024, 768, "JOSC", NULL, NULL);

        if (windowPtr == NULL) {
            System.out.println("Unable to create window");
            System.exit(-1);
        }

        glfwMakeContextCurrent(windowPtr);
        glfwSwapInterval(1);
        glfwShowWindow(windowPtr);

        GL.createCapabilities();

    }

    private void initImGui() {
        ImGui.createContext();

        // Allows ImGui internal windows to be dragged outside of the main window.
        //ImGuiIO guiIO = ImGui.getIO();
        //guiIO.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
    }

    public void run() {
        while (!glfwWindowShouldClose(windowPtr)) {
            float dumbSine = .2f * (float) Math.sin(2 * Math.PI * dummyVar) + .3f;

            glClearColor(0.3f - dumbSine, dumbSine, 0.3f + dumbSine, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            dummyVar += .001;
            if(dummyVar > 1) dummyVar = 0;


            // ImGui render frame begins here
            imGuiGlfw.newFrame();
            ImGui.newFrame();

            imguiLayer.imgui();

            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            if(ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupWindowPtr);
            }
            // ImGui render frame ends here

            GLFW.glfwSwapBuffers(windowPtr);
            GLFW.glfwPollEvents();
        }
    }
}
