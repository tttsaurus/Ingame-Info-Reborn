package com.tttsaurus.ingameinfo.common.impl.render.mesh;

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

    }
}
