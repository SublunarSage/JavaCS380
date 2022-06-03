package edu.cwu.renderer;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE;
    private List<BatchRenderer2DPoint> batches;

    public Renderer() {
        this(1024);
    }

    public Renderer(int size) {
        MAX_BATCH_SIZE = size;
        this.batches = new ArrayList<>();
    }

    public void set(float[] points) {
        for (BatchRenderer2DPoint batch: batches) {
            batch.reset();
        }
        add(points);
    }


    public void add(float[] points) { // assumed to be x1, y1, x2, y2...
        if(points.length % 2 == 0) {
            for(int i = 0; i < points.length - 1; i+=2)
            {
                Vector2f point = new Vector2f(points[i],points[i+1]);
                add(point);
            }
        }
    }

    public void add(Vector2f point) {
        boolean added = false;
        for(BatchRenderer2DPoint batch : batches) {
            if(batch.hasRoom()) {
                batch.addPoint(point);
                added = true;
                break;
            }
        }

        if(!added) {
            BatchRenderer2DPoint newBatch = new BatchRenderer2DPoint(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addPoint(point);
        }
    }

    public void render() {
        for (BatchRenderer2DPoint batch : batches) {
            batch.render();
        }
    }

}
