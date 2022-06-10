package edu.cwu;

import edu.cwu.renderer.Renderer;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import org.joml.Vector4f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    private final int WIDTH = 1024;
    private final int HEIGHT = 768;
    private final int VIEWPORT_WIDTH = 728;

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private String glslVersion = null;
    private long windowPtr; // Java does not have native pointers, so the long datatype is used.
    private final ImGuiLayer imguiLayer;

    private double dummyVar = 0;

    private TestShaderClass TSClass = new TestShaderClass();
    private Renderer renderer = new Renderer(VIEWPORT_WIDTH/2);
    private boolean shouldGenerate = true;
    private int wavePos = 0;
    private AudioBuffer audioBuffer;
    private AudioController audioController;



    public Window(ImGuiLayer layer, AudioController audio) {
        imguiLayer = layer;
        audioController = audio;
        audioBuffer = audioController.getAudioBuffer();
    }

    public void init() {
        initWindow();
        initImGui();
        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init(glslVersion);

    }

    public void destroy() {
        audioController.closeThread();
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

        // Configure GLFW
        glslVersion = "#version 330";
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);


        // Create the Window
        windowPtr = glfwCreateWindow(WIDTH, HEIGHT, "JOSC", NULL, NULL);

        if (windowPtr == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowPtr);
        // Enable V-Sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(windowPtr);

        // This line is critical for LWJGL's interoperation with GLFW's OpenGL context,
        // or any context that is managed externally. LWJGL detects the context that is current in the current
        // thread, creates the GLCapabilities instance and makes the OpenGL bindings available for use.
        GL.createCapabilities();

        glViewport(0,0,728,HEIGHT); // Where openGL will draw pixels. Conversion from normalized to pixels.


    }

    private void initImGui() {
        ImGui.createContext();

        // Allows ImGui internal windows to be dragged outside of the main window.
        //ImGuiIO guiIO = ImGui.getIO();
        //guiIO.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
    }

    public void run() {
        //TSClass.init();


        float beginTime = (float)glfwGetTime(); // Use LWJGL's built in timer.
        float endTime;
        float dt = -1;


        while (!glfwWindowShouldClose(windowPtr)) {
            float dumbSine = .2f * (float) Math.sin(2 * Math.PI * dummyVar) + .3f;
            float dumbCos = .2f * (float) Math.cos(2 * Math.PI * dummyVar) + .3f;
            float dumberSine = .2f * (float) Math.sin(3 * Math.PI * dummyVar) + .3f;

            glClearColor(audioController.getCurrentColor().x, audioController.getCurrentColor().y, audioController.getCurrentColor().z, audioController.getCurrentColor().w);
            glClear(GL_COLOR_BUFFER_BIT);



            //if(!audioThread.isRunning()) audioThread.triggerPlayback();

            // Put custom OpenGL Code below -----
            if(dt >= 0) {
                //dummyVar += dt/6;
                if(dummyVar > 6) dummyVar = 0;
                renderer.render();
                //TSClass.draw(dt);


                float[] normalizedBuffer = new float[audioBuffer.getAudioBuffer().length];

                for(int i = 0; i < normalizedBuffer.length; i++) {
                    normalizedBuffer[i] = (float) audioBuffer.getAudioBuffer()[i] / Short.MAX_VALUE;
                }

                float[] testFloat = new float[audioBuffer.getAudioBuffer().length];

                for(int i = 0; i < testFloat.length-1; i+=2) {
                    float x = (float) (i * 2 - audioBuffer.getAudioBuffer().length)/ audioBuffer.getAudioBuffer().length;
                    testFloat[i] = x;
                    testFloat[i+1] = (float) normalizedBuffer[i];
                }

                renderer.set(testFloat);

            }



            // Put custom OpenGl Code above -----

            // ImGui render frame begins here
            imGuiGlfw.newFrame();
            ImGui.newFrame();


            // Insert GUI windows below ------
            imguiLayer.imgui();


            // Insert GUI windows above ------

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

            // Time management
            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
