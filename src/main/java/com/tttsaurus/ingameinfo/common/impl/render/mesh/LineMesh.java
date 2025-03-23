package com.tttsaurus.ingameinfo.common.impl.render.mesh;

import com.tttsaurus.ingameinfo.common.api.render.ScaledRes2NdcUtils;
import com.tttsaurus.ingameinfo.common.api.render.VertexIndexUtils;

public class LineMesh extends Mesh
{
    private boolean formLoop = false;
    private float lineWidth;
    private final int maxLineNum;
    private int lineNum;
    private int vertexNum;
    private final float[] vertices;

    public LineMesh(int lineNum, float lineWidth)
    {
        super(new float[(lineNum + 1) * 2 * 8], new int[lineNum * 6 + 9]);
        this.maxLineNum = lineNum;
        this.lineNum = lineNum;
        this.vertexNum = lineNum + 1;
        this.vertices = new float[vertexNum * 2];
        this.lineWidth = lineWidth;
        // 9 spare indicies to form loop
        setEboIndexOffset(9);
    }

    public LineMesh setFormLoop(boolean flag)
    {
        formLoop = flag;
        setEboIndexOffset((maxLineNum - lineNum) * 6 + (formLoop ? 0 : 9));
        return this;
    }
    public LineMesh setLineWidth(float lineWidth)
    {
        this.lineWidth = lineWidth;
        return this;
    }
    public LineMesh setLineNum(int lineNum)
    {
        if (lineNum < 0)
            throw new IllegalStateException("Line number " + lineNum + " is invalid. You need at least 0 lines in a line mesh");
        if (lineNum > maxLineNum)
            throw new IllegalStateException("Line number " + lineNum + " is invalid. You can't exceed the allocated limit " + maxLineNum);

        this.lineNum = lineNum;
        this.vertexNum = lineNum + 1;

        setEboIndexOffset((maxLineNum - lineNum) * 6 + (formLoop ? 0 : 9));
        return this;
    }
    // under minecraft's scaled resolution coordinate system
    public LineMesh setVertex(int index, float x, float y)
    {
        if (index < 0 || index > vertexNum - 1)
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for vertex number " + vertexNum);

        vertices[index * 2] = x;
        vertices[index * 2 + 1] = y;
        return this;
    }

    public void update()
    {
        float[] newVertices = new float[getVerticesLength()];

        // line segment normal
        float[] lineNormalX = new float[vertexNum];
        float[] lineNormalY = new float[vertexNum];
        for (int i = 0; i < vertexNum - 1; i++)
        {
            float x1 = vertices[i * 2];
            float y1 = vertices[i * 2 + 1];
            float x2 = vertices[(i + 1) * 2];
            float y2 = vertices[(i + 1) * 2 + 1];
            float dxN = -(y2 - y1);
            float dyN = x2 - x1;
            float len = (float) Math.sqrt(dxN * dxN + dyN * dyN);
            lineNormalX[i] = dxN / len;
            lineNormalY[i] = dyN / len;
        }
        // duplicate the last normal
        lineNormalX[vertexNum - 1] = lineNormalX[vertexNum - 2];
        lineNormalY[vertexNum - 1] = lineNormalY[vertexNum - 2];

        // vertex normal
        float[] vertexNormalX = new float[vertexNum * 2];
        float[] vertexNormalY = new float[vertexNum * 2];
        for (int i = 0; i < vertexNum; i++)
        {
            float normalX1;
            float normalY1;
            float normalX2;
            float normalY2;

            if (i == 0 || i == vertexNum - 1)
            {
                normalX1 = lineNormalX[i] * lineWidth / 2f;
                normalY1 = lineNormalY[i] * lineWidth / 2f;
                normalX2 = -normalX1;
                normalY2 = -normalY1;
            }
            else
            {
                float dx1 = lineNormalX[i - 1];
                float dx2 = lineNormalX[i];
                float dy1 = lineNormalY[i - 1];
                float dy2 = lineNormalY[i];
                float dx = (dx1 + dx2) / 2f;
                float dy = (dy1 + dy2) / 2f;

                float prevNormalX = vertexNormalX[(i - 1) * 2];
                float prevNormalY = vertexNormalY[(i - 1) * 2];

                float x1 = vertices[(i - 1) * 2];
                float y1 = vertices[(i - 1) * 2 + 1];
                float x2 = vertices[i * 2];
                float y2 = vertices[i * 2 + 1];

                float k = (y2 - y1) / (x2 - x1 == 0 ? 1E-6f : x2 - x1);
                float c = y1 + prevNormalY - (x1 + prevNormalX) * k;

                float normalK = dy / (dx == 0 ? 1E-6f : dx);
                float normalC = y2 - x2 * normalK;

                float intersectX = (c - normalC) / (normalK - k);
                float intersectY = normalK * intersectX + normalC;

                normalX1 = intersectX - x2;
                normalY1 = intersectY - y2;

                prevNormalX = vertexNormalX[(i - 1) * 2 + 1];
                prevNormalY = vertexNormalY[(i - 1) * 2 + 1];

                c = y1 + prevNormalY - (x1 + prevNormalX) * k;

                intersectX = (c - normalC) / (normalK - k);
                intersectY = normalK * intersectX + normalC;

                normalX2 = intersectX - x2;
                normalY2 = intersectY - y2;
            }
            vertexNormalX[i * 2] = normalX1;
            vertexNormalY[i * 2] = normalY1;
            vertexNormalX[i * 2 + 1] = normalX2;
            vertexNormalY[i * 2 + 1] = normalY2;

            float x = vertices[i * 2];
            float y = vertices[i * 2 + 1];

            // pos
            newVertices[i * 2 * 8] = ScaledRes2NdcUtils.toNdcX(x + normalX1);
            newVertices[i * 2 * 8 + 1] = ScaledRes2NdcUtils.toNdcY(y + normalY1);
            newVertices[i * 2 * 8 + 2] = 0f;
            // texcoord
            newVertices[i * 2 * 8 + 3] = 0f;
            newVertices[i * 2 * 8 + 4] = 0f;
            // normal
            newVertices[i * 2 * 8 + 5] = 0f;
            newVertices[i * 2 * 8 + 6] = 0f;
            newVertices[i * 2 * 8 + 7] = 1f;

            // pos
            newVertices[(i * 2 + 1) * 8] = ScaledRes2NdcUtils.toNdcX(x + normalX2);
            newVertices[(i * 2 + 1) * 8 + 1] = ScaledRes2NdcUtils.toNdcY(y + normalY2);
            newVertices[(i * 2 + 1) * 8 + 2] = 0f;
            // texcoord
            newVertices[(i * 2 + 1) * 8 + 3] = 0f;
            newVertices[(i * 2 + 1) * 8 + 4] = 0f;
            // normal
            newVertices[(i * 2 + 1) * 8 + 5] = 0f;
            newVertices[(i * 2 + 1) * 8 + 6] = 0f;
            newVertices[(i * 2 + 1) * 8 + 7] = 1f;
        }

        int[] newIndices = new int[getIndicesLength()];
        for (int i = 0; i < lineNum; i++)
        {
            int offset = maxLineNum - lineNum;
            newIndices[9 + (i + offset) * 6] = i * 2;
            newIndices[9 + (i + offset) * 6 + 1] = i * 2 + 2;
            newIndices[9 + (i + offset) * 6 + 2] = i * 2 + 1;
            newIndices[9 + (i + offset) * 6 + 3] = i * 2 + 2;
            newIndices[9 + (i + offset) * 6 + 4] = i * 2 + 3;
            newIndices[9 + (i + offset) * 6 + 5] = i * 2 + 1;
        }

        if (formLoop)
        {
            int i1 = 0;
            int i2 = 1;
            int i3 = vertexNum * 2 - 2;
            int i4 = vertexNum * 2 - 1;
            float x1 = newVertices[i1 * 8], y1 = newVertices[i1 * 8 + 1];
            float x2 = newVertices[i2 * 8], y2 = newVertices[i2 * 8 + 1];
            float x3 = newVertices[i3 * 8], y3 = newVertices[i3 * 8 + 1];
            float x4 = newVertices[i4 * 8], y4 = newVertices[i4 * 8 + 1];

            if (VertexIndexUtils.isCcw(x1, y1, x2, y2, x3, y3))
            {
                newIndices[0] = i1;
                newIndices[1] = i2;
                newIndices[2] = i3;
            }
            else
            {
                newIndices[0] = i1;
                newIndices[1] = i3;
                newIndices[2] = i2;
            }
            if (VertexIndexUtils.isCcw(x3, y3, x2, y2, x4, y4))
            {
                newIndices[3] = i3;
                newIndices[4] = i2;
                newIndices[5] = i4;
            }
            else
            {
                newIndices[3] = i3;
                newIndices[4] = i4;
                newIndices[5] = i2;
            }
            if (VertexIndexUtils.isCcw(x1, y1, x4, y4, x3, y3))
            {
                newIndices[6] = i1;
                newIndices[7] = i4;
                newIndices[8] = i3;
            }
            else
            {
                newIndices[6] = i1;
                newIndices[7] = i3;
                newIndices[8] = i4;
            }
        }

        updateVerticesByBufferSubData(newVertices);
        updateIndicesByBufferSubData(newIndices);
    }
}
