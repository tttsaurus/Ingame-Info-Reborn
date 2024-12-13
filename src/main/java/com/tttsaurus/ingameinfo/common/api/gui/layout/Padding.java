package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyDeserializer;
import com.tttsaurus.ingameinfo.common.impl.serialization.PaddingDeserializer;

@StylePropertyDeserializer(PaddingDeserializer.class)
public class Padding
{
    public float top;
    public float bottom;
    public float left;
    public float right;

    public Padding(float top, float bottom, float left, float right)
    {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }
}
