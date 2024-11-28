package com.tttsaurus.ingameinfo.common.api.gui.layout;

public enum Pivot
{
    CENTER(0.5f, 0.5f),

    TOP_LEFT(0, 0),
    TOP_RIGHT(0, 1),

    BOTTOM_LEFT(1, 0),
    BOTTOM_RIGHT(1, 1);

    public final float horizontal;
    public final float vertical;
    Pivot(float horizontal, float vertical)
    {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
}
