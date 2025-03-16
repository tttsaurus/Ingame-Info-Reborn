package com.tttsaurus.ingameinfo.common.impl.render.mesh;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class RectMesh extends Mesh
{
    public RectMesh()
    {
        super(new float[8 * 4], new int[6]);
    }

    // under minecraft's scaled resolution coordinate system
    public void update(float x, float y, float width, float height)
    {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        float resHeight = (float)resolution.getScaledHeight_double();
        float resWidth = (float)resolution.getScaledWidth_double();
        x = ((x - resWidth / 2f) / resWidth) * 2f;
        y = ((y - resHeight / 2f) / resHeight) * 2f;
        width = ((width - resWidth / 2f) / resWidth) * 2f;
        height = ((height - resHeight / 2f) / resHeight) * 2f;

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
    }
}
