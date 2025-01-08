package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;

@RegisterElement
public class ProgressBar extends Sized
{
    @StyleProperty
    public int fillerColor = DEFAULT_COLOR_LIGHT;

    @StyleProperty
    public int backgroundColor = DEFAULT_COLOR_DARK;

    @StyleProperty
    public int outlineColor = DEFAULT_COLOR_DARKER;

    public void percentageValidation(float value, CallbackInfo callbackInfo)
    {
        if (value < 0 || value > 1) callbackInfo.cancel = true;
    }
    @StyleProperty(setterCallbackPre = "percentageValidation")
    public float percentage = 0f;

    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }

    @Override
    public void onRenderUpdate(boolean focused)
    {
        if (rect.width == 0 || rect.height == 0) return;
        RenderUtils.renderRoundedRect(rect.x, rect.y, rect.width, rect.height, rect.height / 2f, backgroundColor);
        if (percentage != 0)
        {
            RenderUtils.startRoundedRectStencil(rect.x, rect.y, rect.width, rect.height, 2, rect.height / 2f);
            RenderUtils.renderRect(rect.x, rect.y, rect.width * percentage, rect.height, fillerColor);
            RenderUtils.endStencil();
        }
        RenderUtils.renderRoundedRectOutline(rect.x, rect.y, rect.width, rect.height, rect.height / 2f, 1.0f, outlineColor);
    }
}
