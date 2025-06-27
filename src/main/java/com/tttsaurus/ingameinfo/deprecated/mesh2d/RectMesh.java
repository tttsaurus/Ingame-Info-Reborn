package com.tttsaurus.ingameinfo.deprecated.mesh2d;

import com.tttsaurus.ingameinfo.deprecated.ScaledRes2NdcUtils;
import com.tttsaurus.ingameinfo.deprecated.Mesh;

public class RectMesh extends Mesh
{
    public RectMesh()
    {
        super(new float[8 * 4], new int[6]);
    }

    // under minecraft's scaled resolution coordinate system
    public void update(float x, float y, float width, float height)
    {
        float[] vertices = new float[]
        {
            // positions                                                                          // texcoords  // normals
            ScaledRes2NdcUtils.toNdcX(x),          ScaledRes2NdcUtils.toNdcY(y),           0.0f,  0.0f, 0.0f,   0.0f, 0.0f, 1.0f,  // bottom-left
            ScaledRes2NdcUtils.toNdcX(x + width),  ScaledRes2NdcUtils.toNdcY(y),           0.0f,  1.0f, 0.0f,   0.0f, 0.0f, 1.0f,  // bottom-right
            ScaledRes2NdcUtils.toNdcX(x + width),  ScaledRes2NdcUtils.toNdcY(y + height),  0.0f,  1.0f, 1.0f,   0.0f, 0.0f, 1.0f,  // top-right
            ScaledRes2NdcUtils.toNdcX(x),          ScaledRes2NdcUtils.toNdcY(y + height),  0.0f,  0.0f, 1.0f,   0.0f, 0.0f, 1.0f   // top-left
        };
        int[] indices = new int[]
        {
            0, 2, 1,
            0, 3, 2
        };

        updateVerticesByBufferSubData(vertices);
        updateIndicesByBufferSubData(indices);
    }
}
