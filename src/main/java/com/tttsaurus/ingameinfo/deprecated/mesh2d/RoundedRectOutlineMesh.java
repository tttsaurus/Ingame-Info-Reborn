package com.tttsaurus.ingameinfo.deprecated.mesh2d;

public class RoundedRectOutlineMesh extends LineMesh
{
    private final int maxCornerSegment;
    private int cornerSegment;
    private float cornerRadius;
    private float x;
    private float y;
    private float width;
    private float height;
    private int vertexIndex = 0;

    public RoundedRectOutlineMesh(int maxCornerSegment, float lineWidth)
    {
        super(maxCornerSegment * 4 + 3, lineWidth);
        this.maxCornerSegment = maxCornerSegment;
        cornerSegment = maxCornerSegment;
        setFormLoop(true);
    }

    public RoundedRectOutlineMesh setCornerRadius(float radius)
    {
        cornerRadius = radius;
        cornerSegment = Math.min(maxCornerSegment, Math.max(3, (int)(cornerRadius / 2f)));
        setLineNum(cornerSegment * 4 + 3);
        return this;
    }
    public RoundedRectOutlineMesh setRect(float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    private void addArcVertices(float cx, float cy, float startAngle, float endAngle, int segments)
    {
        startAngle -= 90;
        endAngle -= 90;
        for (int i = 0; i <= segments; i++)
        {
            float angle = (float)Math.toRadians(startAngle + (endAngle - startAngle) * (float)i / (float)segments);
            float x = (float)(cx + Math.cos(angle) * cornerRadius);
            float y = (float)(cy + Math.sin(angle) * cornerRadius);
            setVertex(vertexIndex++, x, y);
        }
    }

    @Override
    public void update()
    {
        vertexIndex = 0;

        addArcVertices(x + width - cornerRadius, y + cornerRadius, 0, 90, cornerSegment);
        addArcVertices(x + width - cornerRadius, y + height - cornerRadius, 90, 180, cornerSegment);
        addArcVertices(x + cornerRadius, y + height - cornerRadius, 180, 270, cornerSegment);
        addArcVertices(x + cornerRadius, y + cornerRadius, 270, 360, cornerSegment);

        super.update();
    }
}
