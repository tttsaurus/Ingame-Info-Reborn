package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyCallback;

// for those who don't implicitly have a size
@RegisterElement(constructable = false)
public abstract class Sized extends Element
{
    @StylePropertyCallback
    public void nonNegativeFloatValidation(float value, CallbackInfo callbackInfo)
    {
        if (value < 0) callbackInfo.cancel = true;
    }

    @StyleProperty(setterCallbackPost = "requestReCalc", setterCallbackPre = "nonNegativeFloatValidation")
    public float width;

    @StyleProperty(setterCallbackPost = "requestReCalc", setterCallbackPre = "nonNegativeFloatValidation")
    public float height;

    @Override
    public void calcWidthHeight()
    {
        rect.width = width;
        rect.height = height;
    }
}
