package com.tttsaurus.ingameinfo.common.core.animation.text;

public class CharInfo
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
}