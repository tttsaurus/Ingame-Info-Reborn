package com.tttsaurus.ingameinfo.common.core.gui.layout;

import com.tttsaurus.ingameinfo.common.core.serialization.DeserializerSignature;
import com.tttsaurus.ingameinfo.common.impl.serialization.AlignmentDeserializer;

@DeserializerSignature(AlignmentDeserializer.class)
public enum Alignment
{
    NULL(0, 0),

    TOP_LEFT(0,0),
    TOP_CENTER(0, 0.5f),
    TOP_RIGHT(0, 1),

    CENTER_LEFT(0.5f, 0),
    CENTER(0.5f, 0.5f),
    CENTER_RIGHT(0.5f, 1),

    BOTTOM_LEFT(1, 0),
    BOTTOM_CENTER(1, 0.5f),
    BOTTOM_RIGHT(1, 1);

    public final float horizontal;
    public final float vertical;
    Alignment(float horizontal, float vertical)
    {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
}
