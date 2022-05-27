package edu.cwu;

import edu.cwu.renderer.Shader;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class SingleChannelWave {

    private float[] vertexArray = {
            // (pos.x, pos.y, r, g, b, a)
            0.0f, 0.8f, 1.0f, 0.0f, 0.0f, 1.0f,   // 0
            -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, // 1
            0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,   // 2
    };

    private int[] elementArray = {
        2, 1, 0
    };

    private int vaoID, vboID, eboID;

    private Shader testShader = new Shader("assets/shaders/basic.glsl");

    public void init() {

        testShader.compile();
        //testShader.link();
        generateShaderBuffers();
    }

    public void draw() {

        // Bind
        testShader.bind();
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        //Draw
        glPointSize(5.0f);
        //glDrawArrays(GL_TRIANGLES,0,3);
        glDrawElements(GL_TRIANGLES,elementArray.length, GL_UNSIGNED_INT, 0 );

        // Unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
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
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO and upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_DYNAMIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_DYNAMIC_DRAW);

        // Add vertex attribute pointers
        int positionSize = 2;
        int colorSize = 4;
        int vertexSizeBytes = (positionSize + colorSize) * Float.BYTES;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

    }

}
