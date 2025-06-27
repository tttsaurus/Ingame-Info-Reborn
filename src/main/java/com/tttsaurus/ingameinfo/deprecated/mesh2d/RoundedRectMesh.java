package com.tttsaurus.ingameinfo.deprecated.mesh2d;

import com.tttsaurus.ingameinfo.deprecated.Mesh;
import com.tttsaurus.ingameinfo.deprecated.ScaledRes2NdcUtils;
import com.tttsaurus.ingameinfo.deprecated.VertexIndexUtils;

public class RoundedRectMesh extends Mesh
{
    private final int maxCornerSegment;
    private int cornerSegment;
    private float cornerRadius;
    private float x;
    private float y;
    private float width;
    private float height;
    private int vertexIndex = 0;

    public RoundedRectMesh(int maxCornerSegment)
    {
        super(new float[((maxCornerSegment + 1) * 4 + 1) * 8], new int[(maxCornerSegment * 4 + 4) * 3]);
        this.maxCornerSegment = maxCornerSegment;
        cornerSegment = maxCornerSegment;
    }

    public RoundedRectMesh setCornerRadius(float radius)
    {
        cornerRadius = radius;
        cornerSegment = Math.min(maxCornerSegment, Math.max(3, (int)(cornerRadius / 2f)));
        setEboIndexOffset((maxCornerSegment - cornerSegment) * 4 * 3);
        return this;
    }
    public RoundedRectMesh setRect(float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    private void addArcVertices(float[] vertices, float cx, float cy, float startAngle, float endAngle, int segments)
    {
        startAngle -= 90;
        endAngle -= 90;
        for (int i = 0; i <= segments; i++)
        {
            float angle = (float)Math.toRadians(startAngle + (endAngle - startAngle) * (float)i / (float)segments);
            float x = (float)(cx + Math.cos(angle) * cornerRadius);
            float y = (float)(cy + Math.sin(angle) * cornerRadius);

            // pos
            vertices[vertexIndex * 8] = ScaledRes2NdcUtils.toNdcX(x);
            vertices[vertexIndex * 8 + 1] = ScaledRes2NdcUtils.toNdcY(y);
            vertices[vertexIndex * 8 + 2] = 0f;
            // texcoord
            vertices[vertexIndex * 8 + 3] = 0f;
            vertices[vertexIndex * 8 + 4] = 0f;
            // normal
            vertices[vertexIndex * 8 + 5] = 0f;
            vertices[vertexIndex * 8 + 6] = 0f;
            vertices[vertexIndex * 8 + 7] = 1f;

            vertexIndex++;
        }
    }

    public void update()
    {
        float[] newVertices = new float[getVerticesLength()];

        float centerX = x + width / 2f;
        float centerY = y + height / 2f;

        // pos
        newVertices[0] = ScaledRes2NdcUtils.toNdcX(centerX);
        newVertices[1] = ScaledRes2NdcUtils.toNdcY(centerY);
        newVertices[2] = 0f;
        // texcoord
        newVertices[3] = 0f;
        newVertices[4] = 0f;
        // normal
        newVertices[5] = 0f;
        newVertices[6] = 0f;
        newVertices[7] = 1f;

        vertexIndex = 1;

        addArcVertices(newVertices, x + width - cornerRadius, y + cornerRadius, 0, 90, cornerSegment);
        addArcVertices(newVertices, x + width - cornerRadius, y + height - cornerRadius, 90, 180, cornerSegment);
        addArcVertices(newVertices, x + cornerRadius, y + height - cornerRadius, 180, 270, cornerSegment);
        addArcVertices(newVertices, x + cornerRadius, y + cornerRadius, 270, 360, cornerSegment);

        int[] newIndices = new int[getIndicesLength()];
        for (int i = 1; i < vertexIndex; i++)
        {
            int offset = (maxCornerSegment - cornerSegment) * 4;

            int i1 = i, i2 = i + 1;
            float x1 = newVertices[i * 8], y1 = newVertices[i * 8 + 1];
            float x2, y2;
            if (i == vertexIndex - 1)
            {
                x2 = newVertices[8];
                y2 = newVertices[9];
                i2 = 1;
            }
            else
            {
                x2 = newVertices[(i + 1) * 8];
                y2 = newVertices[(i + 1) * 8 + 1];
            }

            if (VertexIndexUtils.isCcw(newVertices[0], newVertices[1], x1, y2, x2, y2))
            {
                newIndices[(i - 1 + offset) * 3] = 0;
                newIndices[(i - 1 + offset) * 3 + 1] = i1;
                newIndices[(i - 1 + offset) * 3 + 2] = i2;
            }
            else
            {
                newIndices[(i - 1 + offset) * 3] = 0;
                newIndices[(i - 1 + offset) * 3 + 1] = i2;
                newIndices[(i - 1 + offset) * 3 + 2] = i1;
            }
        }

        updateVerticesByBufferSubData(newVertices);
        updateIndicesByBufferSubData(newIndices);
    }
}
