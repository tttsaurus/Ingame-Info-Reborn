package com.tttsaurus.ingameinfo.common.api.gui.layout;

import javax.annotation.Nullable;

public class Rect
{
    public float x;
    public float y;
    public float width;
    public float height;

    public Rect(float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void set(float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(float x, float y)
    {
        return x >= this.x && x <= (this.x + width) && y >= this.y && y <= (this.y + height);
    }

    @Nullable
    public static Rect intersect(Rect rect1, Rect rect2)
    {
        if (rect1 == null || rect2 == null) return null;

        float x1 = Math.max(rect1.x, rect2.x);
        float y1 = Math.max(rect1.y, rect2.y);
        float x2 = Math.min(rect1.x + rect1.width, rect2.x + rect2.width);
        float y2 = Math.min(rect1.y + rect1.height, rect2.y + rect2.height);

        float width = x2 - x1;
        float height = y2 - y1;

        if (width > 0 && height > 0)
            return new Rect(x1, y1, width, height);
        else
            return null;
    }
}
