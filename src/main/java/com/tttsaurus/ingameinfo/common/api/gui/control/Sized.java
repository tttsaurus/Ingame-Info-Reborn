package com.tttsaurus.ingameinfo.common.api.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.Element;

public abstract class Sized extends Element
{
    protected float width;
    protected float height;

    public Sized(float width, float height)
    {
        this.width = width;
        this.height = height;
    }

    @Override
    public void calcWidthHeight()
    {
        this.rect.width = width;
        this.rect.height = height;
    }
}
