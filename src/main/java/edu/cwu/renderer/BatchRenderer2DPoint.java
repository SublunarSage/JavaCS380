package edu.cwu.renderer;

import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL30.*;

public class BatchRenderer2DPoint {
    // Vertex format: Pos.x, pos.y, R, G, B, A. All values are float.
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = (POS_OFFSET + COLOR_SIZE) * Float.BYTES;
    private boolean perPointColorEnabled = false;
    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private Vector2f[] points;
    private float[] positions = new float[2]; // x, y
    private float[] colors = new float[4]; // red, green, blue. alpha (or opacity)
    private float[] vertices = new float[POS_SIZE+COLOR_SIZE]; // x, y, r, g, b, a.

    private boolean hasRoom;



    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;
    private int numPoints = 0;

    public BatchRenderer2DPoint(int maxBatchSize) {
        shader = new Shader("assets/shaders/SCLine.glsl");
        shader.compile();
        this.points = new Vector2f[maxBatchSize];
        this.maxBatchSize = maxBatchSize; // 1 batch = 1 set of x, y, r, g, b, and a.

        vertices = new float[maxBatchSize * VERTEX_SIZE]; // x1, y1, r1, g1, b1, a1, | x2, y2, r2...

        this.numPoints = 0;
        this.hasRoom = true;
    }

    public void start() {
        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate spaces for vertices in buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void reset() {
        this.points = new Vector2f[maxBatchSize];
        vertices = new float[maxBatchSize * VERTEX_SIZE];
        numPoints = 0;
        hasRoom = true;
    }

    public void setPoints(float[] points) // Assumed given as x1, y1, x2, y2...
    {
        for(int i = 0; i < maxBatchSize; i++) {
            addPoint(new Vector2f(points[0],points[1]));
        }
    }

    public void addPoint(float[] point) { // Assumed to be given as x, y
        if(point.length != POS_SIZE) {
            System.out.println("Invalid point size given in batch renderer addPoint!");
            return;
        }
        Vector2f vPoint = new Vector2f(point[0],point[1]);
        addPoint(vPoint);
    }

    public void addPoint(Vector2f point) {
        int index = this.numPoints;
        this.points[index] = point;
        this.numPoints++;

        loadVertexProperties(index);

        if(numPoints >= this.maxBatchSize) {
            this.hasRoom = false;
        }
    }

    private void loadVertexProperties(int index) {
        Vector2f point = this.points[index];

        // Find offset within array (1 vertex per point)
        int offset = index * 1 * VERTEX_SIZE;

        Vector4f color = new Vector4f(1,1,1,0);


        // load position
        vertices[offset] = point.x;
        vertices[offset+1] = point.y;

        // load color
        if(perPointColorEnabled) {
            vertices[offset+2] = color.x;
            vertices[offset+3] = color.y;
            vertices[offset+4] = color.z;
            vertices[offset+5] = color.w;
        }

    }



    public void render() {
        // Rebuffer every render call.
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER,0,vertices);

        shader.bind();

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glLineWidth(5.0f);
        glDrawElements(GL_LINE_STRIP, this.numPoints, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shader.unbind();

    }

    private int[] generateIndices() {
        int[] elements = new int[maxBatchSize]; // Working with points. 1 index per point.
        for(int i = 0; i < maxBatchSize; i++)
        {
            elements[i] = i;
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = VERTEX_SIZE * index;


    }

    public boolean hasRoom() {
        return this.hasRoom;
    }



}
