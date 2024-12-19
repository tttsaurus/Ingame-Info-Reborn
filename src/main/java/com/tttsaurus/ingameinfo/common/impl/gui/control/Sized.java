package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;

public abstract class Sized extends Element
{
    @StyleProperty(setterCallback = "requestReCalc")
    public float width;

    @StyleProperty(setterCallback = "requestReCalc")
    public float height;

    @Override
    public void calcWidthHeight()
    {
        rect.width = width;
        rect.height = height;
    }
}
