package com.tttsaurus.ingameinfo.common.impl.render.mesh;

public class RectMesh extends Mesh
{
    public RectMesh()
    {
        super(new float[8 * 4], new int[6]);
    }

    public RectMesh update(float x, float y, float width, float height)
    {
        float[] vertices = new float[]
        {
            // positions                    // texcoords  // normals
            x,          y,           0.0f,  0.0f, 0.0f,   0.0f, 0.0f, 1.0f,  // bottom-left
            x + width,  y,           0.0f,  1.0f, 0.0f,   0.0f, 0.0f, 1.0f,  // bottom-right
            x + width,  y + height,  0.0f,  1.0f, 1.0f,   0.0f, 0.0f, 1.0f,  // top-right
            x,          y + height,  0.0f,  0.0f, 1.0f,   0.0f, 0.0f, 1.0f   // top-left
        };
        int[] indices = new int[]
        {
            0, 1, 2,
            2, 3, 0
        };
        updateVerticesByBufferSubData(vertices);
        updateIndicesByBufferSubData(indices);
        return this;
    }
}
