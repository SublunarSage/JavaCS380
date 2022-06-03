package edu.cwu;

import edu.cwu.renderer.Shader;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL30.*;

public class TestShaderClass {

    private float[] vertexArray = {
            // (pos.x, pos.y, r, g, b, a)
            0.0f, 0.8f, 1.0f, 0.0f, 0.0f, 1.0f,   // 0
            -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, // 1
            0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,   // 2
    };

    private float[] pointArray = new float[728]; // x, y.

    private int[] elementArray = {
        2, 1, 0
    };

    private int[] pElementArray = new int[pointArray.length];

    private int vaoID, vboID, eboID;

    private Shader testShader = new Shader("assets/shaders/SCLine.glsl");

    float timeV = 0;

    public void init() {
        for(int i = 0; i < pointArray.length-1; i+=2)
        {
            float x = (float) (i - pointArray.length/2)/(pointArray.length/2);
            pointArray[i] = x;
            pointArray[i+1] = (float) Math.sin(2*Math.PI*x);

            //pointArray[i] = (float) (i-pointArray.length/2)/(pointArray.length/2);
            //pointArray[i+1] = (float) -(i-pointArray.length/2)/(pointArray.length/2);
            //pointArray[i+1] = (float) Math.sin(2*Math.PI*x);
            pElementArray[i] = i;
            pElementArray[i+1] = i+1;
        }
        testShader.compile();
        //testShader.link();
        generateShaderBuffers();
    }

    public void draw(float dt) {

        // Bind
        testShader.bind();
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        //glEnableVertexAttribArray(1);

        //Draw
        glPointSize(4.0f);
        //glDrawArrays(GL_POINTS,0,pointArray.length);
        glDrawElements(GL_LINE_LOOP,pointArray.length,GL_UNSIGNED_INT,0);
        //glDrawElements(GL_LINES,elementArray.length, GL_UNSIGNED_INT, 0 );

        //timeV += dt;
        //float blueV = ((float) Math.sin(glfwGetTime())/2.0f) + .5f;
        //testShader.uploadVec4f("oColor",new Vector4f(blueV,0,0,0));

        // Unbind
        glDisableVertexAttribArray(0);
        //glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        testShader.unbind();
    }


    private void generateShaderBuffers() {

        // ============================================================
        // Generate VAO, VBA, and EBO buffer objects, and send to GPU
        // ============================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        //FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        //vertexBuffer.put(vertexArray).flip();
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(pointArray.length);
        vertexBuffer.put(pointArray).flip();

        // Create VBO and upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_DYNAMIC_DRAW);

        // Create the indices and upload

        //IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        //elementBuffer.put(elementArray).flip();

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(pElementArray.length);
        elementBuffer.put(pElementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_DYNAMIC_DRAW);

        // Add vertex attribute pointers
        int positionSize = 2;
        int colorSize = 4;
        int pointsSizeBytes = positionSize * Float.BYTES;
        int vertexSizeBytes = (positionSize + colorSize) * Float.BYTES;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, pointsSizeBytes, 0);
        glEnableVertexAttribArray(0);
        /*
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);
         */

    }

}
