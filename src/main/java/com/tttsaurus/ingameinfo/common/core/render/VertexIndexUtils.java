package com.tttsaurus.ingameinfo.common.core.render;

public final class VertexIndexUtils
{
    // is this triangle counterclockwise
    public static boolean isCcw(float x1, float y1, float x2, float y2, float x3, float y3)
    {
        float cross = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
        return cross > 0;
    }
}
