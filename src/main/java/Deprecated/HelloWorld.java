package Deprecated;


import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class HelloWorld {

    private long window;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    private void init() {
        // Setup an error callback. Default implementation will print error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        if( !glfwInit() ) throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // Optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // The window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // The window will be resizable

        // Create the window
        window = glfwCreateWindow(480,480, "Hello World!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window!");

        // Setup a key callback. It will be called every time a key is pressed, repeated, or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        } // Stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the openGL bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.5f, 0.5f, 0.5f, 0.0f);



        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while( !glfwWindowShouldClose(window) ) {


            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer
            int x = 0;
            int y = 0;
            Random rand = new Random();
            float radius = rand.nextFloat();
            int NUM_PIZZA_SLICES = 100;
            int SUBDIVISIONS = 100;
            glPushMatrix();
            glTranslatef(x, y, 0);
            glScalef(radius, radius, 1);

            glBegin(GL11.GL_TRIANGLE_FAN);
            glVertex2f(0, 0);
            for(int i = 0; i <= NUM_PIZZA_SLICES; i++){ //NUM_PIZZA_SLICES decides how round the circle looks.
                double angle = Math.PI * 2 * i / SUBDIVISIONS;
                glVertex2f((float)Math.cos(angle), (float)Math.sin(angle));
            }
            glEnd();

            glPopMatrix();

            glfwSwapBuffers(window); // Swap the color buffers

            // Poll for the window events. The key callback above will only be invoked during this call.
            glfwPollEvents();
        }
    }
}
