package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;

@RegisterElement(constructable = false)
public abstract class Sized extends Element
{
    @StyleProperty(setterCallbackPost = "requestReCalc")
    public float width;

    @StyleProperty(setterCallbackPost = "requestReCalc")
    public float height;

    @Override
    public void calcWidthHeight()
    {
        rect.width = width;
        rect.height = height;
    }
}
