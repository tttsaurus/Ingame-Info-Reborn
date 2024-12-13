package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyDeserializer;
import com.tttsaurus.ingameinfo.common.impl.serialization.PivotDeserializer;

@StylePropertyDeserializer(PivotDeserializer.class)
public enum Pivot
{
    TOP_LEFT(0,0),
    TOP_MIDDLE(0, 0.5f),
    TOP_RIGHT(0, 1),

    MIDDLE_LEFT(0.5f, 0),
    CENTER(0.5f, 0.5f),
    MIDDLE_RIGHT(0.5f, 1),

    BOTTOM_LEFT(1, 0),
    BOTTOM_MIDDLE(1, 0.5f),
    BOTTOM_RIGHT(1, 1);

    public final float horizontal;
    public final float vertical;
    Pivot(float horizontal, float vertical)
    {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
}
