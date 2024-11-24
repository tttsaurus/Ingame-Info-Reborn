package com.tttsaurus.ingameinfo.common.api.gui.layout;

public abstract class SizedElement extends Element
{
    protected float width;
    protected float height;

    public SizedElement(float width, float height)
    {
        this.width = width;
        this.height = height;
    }

    @Override
    protected void calcWidthHeight()
    {
        this.rect.width = width;
        this.rect.height = height;
    }
}
