package com.tttsaurus.ingameinfo.common.core.animation.text;

import com.tttsaurus.ingameinfo.common.core.gui.property.lerp.CopyableLerpTarget;

public class CharInfo implements CopyableLerpTarget
{
    public float x;
    public float y;
    public float scale;
    public int color;
    public boolean shadow;

    public CharInfo(float x, float y, float scale, int color, boolean shadow)
    {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.color = color;
        this.shadow = shadow;
    }

    @Override
    public Object copy()
    {
        return new CharInfo(x, y, scale, color, shadow);
    }
}