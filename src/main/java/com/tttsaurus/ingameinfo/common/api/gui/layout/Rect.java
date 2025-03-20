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
    public boolean contains(Rect rect)
    {
        return rect.x >= x && (rect.x + rect.width) <= (x + width) && rect.y >= y && (rect.y + rect.height) <= (y + height);
    }

    @Nullable
    public Rect intersect(Rect rect2)
    {
        if (rect2 == null) return null;

        float x1 = Math.max(this.x, rect2.x);
        float y1 = Math.max(this.y, rect2.y);
        float x2 = Math.min(this.x + this.width, rect2.x + rect2.width);
        float y2 = Math.min(this.y + this.height, rect2.y + rect2.height);

        float width = x2 - x1;
        float height = y2 - y1;

        if (width > 0 && height > 0)
            return new Rect(x1, y1, width, height);
        else
            return null;
    }
}
