package com.tttsaurus.ingameinfo.common.core.gui.layout;

import com.tttsaurus.ingameinfo.common.core.serialization.DeserializerSignature;
import com.tttsaurus.ingameinfo.common.impl.serialization.PaddingDeserializer;
import java.util.Objects;

@DeserializerSignature(PaddingDeserializer.class)
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

    public void set(float top, float bottom, float left, float right)
    {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Padding padding)) return false;
        return Float.compare(top, padding.top) == 0 && Float.compare(bottom, padding.bottom) == 0 && Float.compare(left, padding.left) == 0 && Float.compare(right, padding.right) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(top, bottom, left, right);
    }
}
