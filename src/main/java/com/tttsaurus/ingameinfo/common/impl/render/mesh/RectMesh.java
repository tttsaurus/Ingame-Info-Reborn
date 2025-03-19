package com.tttsaurus.ingameinfo.common.impl.render.mesh;

import com.tttsaurus.ingameinfo.common.api.render.ScaledRes2NdcUtils;

public class RectMesh extends Mesh
{
    public RectMesh()
    {
        super(new float[8 * 4], new int[6]);
    }

    // under minecraft's scaled resolution coordinate system
    public void update(float x, float y, float width, float height)
    {
        x = ScaledRes2NdcUtils.toNdcX(x);
        y = ScaledRes2NdcUtils.toNdcY(y);
        width = ScaledRes2NdcUtils.toNdcWidth(width);
        height = ScaledRes2NdcUtils.toNdcHeight(height);

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
            2, 1, 0,
            0, 3, 2
        };

        updateVerticesByBufferSubData(vertices);
        updateIndicesByBufferSubData(indices);
    }
}
