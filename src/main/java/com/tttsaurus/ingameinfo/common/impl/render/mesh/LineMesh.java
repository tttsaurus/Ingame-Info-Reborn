package com.tttsaurus.ingameinfo.common.impl.render.mesh;

import com.tttsaurus.ingameinfo.common.api.render.ScaledRes2NdcUtils;

public class LineMesh extends Mesh
{
    private float lineWidth;
    private final int maxLineNum;
    private int lineNum;
    private int vertexNum;
    private final float[] vertices;

    public LineMesh(int lineNum, float lineWidth)
    {
        super(new float[(lineNum + 1) * 2 * 8], new int[lineNum * 6]);
        this.maxLineNum = lineNum;
        this.lineNum = lineNum;
        this.vertexNum = lineNum + 1;
        this.vertices = new float[vertexNum * 2];
        this.lineWidth = lineWidth;
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

        setEboIndexOffset((maxLineNum - lineNum) * 6);
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

        float[] normDxNs = new float[vertexNum];
        float[] normDyNs = new float[vertexNum];
        for (int i = 0; i < vertexNum - 1; i++)
        {
            float x1 = ScaledRes2NdcUtils.toNdcX(vertices[i * 2]);
            float y1 = ScaledRes2NdcUtils.toNdcY(vertices[i * 2 + 1]);
            float x2 = ScaledRes2NdcUtils.toNdcX(vertices[(i + 1) * 2]);
            float y2 = ScaledRes2NdcUtils.toNdcY(vertices[(i + 1) * 2 + 1]);
            float dxN = -(y2 - y1);
            float dyN = x2 - x1;
            float len = (float) Math.sqrt(dxN * dxN + dyN * dyN);
            normDxNs[i] = dxN / len;
            normDyNs[i] = dyN / len;
        }
        normDxNs[vertexNum - 1] = normDxNs[vertexNum - 2];
        normDyNs[vertexNum - 1] = normDyNs[vertexNum - 2];

        for (int i = 0; i < vertexNum; i++)
        {
            float x = ScaledRes2NdcUtils.toNdcX(vertices[i * 2]);
            float y = ScaledRes2NdcUtils.toNdcY(vertices[i * 2 + 1]);
            float normDxN;
            float normDyN;

            if (i == 0 || i == vertexNum - 1)
            {
                normDxN = normDxNs[i];
                normDyN = normDyNs[i];
            }
            else
            {
                float dx1 = normDxNs[i - 1];
                float dx2 = normDxNs[i];
                float dy1 = normDyNs[i - 1];
                float dy2 = normDyNs[i];
                float dx = (dx1 + dx2) / 2f;
                float dy = (dy1 + dy2) / 2f;
                float len = (float) Math.sqrt(dx * dx + dy * dy);
                normDxN = dx / len;
                normDyN = dy / len;
            }

            // pos
            newVertices[i * 2 * 8] = x + ScaledRes2NdcUtils.toNdcWidth(normDxN * lineWidth / 2f);
            newVertices[i * 2 * 8 + 1] = y - ScaledRes2NdcUtils.toNdcHeight(normDyN * lineWidth / 2f);
            newVertices[i * 2 * 8 + 2] = 0f;
            // texcoord
            newVertices[i * 2 * 8 + 3] = 0f;
            newVertices[i * 2 * 8 + 4] = 0f;
            // normal
            newVertices[i * 2 * 8 + 5] = 0f;
            newVertices[i * 2 * 8 + 6] = 0f;
            newVertices[i * 2 * 8 + 7] = 1f;

            // pos
            newVertices[(i * 2 + 1) * 8] = x - ScaledRes2NdcUtils.toNdcWidth(normDxN * lineWidth / 2f);
            newVertices[(i * 2 + 1) * 8 + 1] = y + ScaledRes2NdcUtils.toNdcHeight(normDyN * lineWidth / 2f);
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
            newIndices[(i + offset) * 6] = i * 2;
            newIndices[(i + offset) * 6 + 1] = i * 2 + 1;
            newIndices[(i + offset) * 6 + 2] = i * 2 + 2;
            newIndices[(i + offset) * 6 + 3] = i * 2 + 2;
            newIndices[(i + offset) * 6 + 4] = i * 2 + 1;
            newIndices[(i + offset) * 6 + 5] = i * 2 + 3;
        }

        updateVerticesByBufferSubData(newVertices);
        updateIndicesByBufferSubData(newIndices);
    }
}
