package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.control.Sized;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.impl.gui.render.op.ProgressBarOp;

@RegisterElement
public class ProgressBar extends Sized
{
    @StyleProperty
    public int fillerColor;

    @StyleProperty
    public int backgroundColor;

    @StyleProperty
    public int outlineColor;

    public void percentageValidation(float value, CallbackInfo callbackInfo)
    {
        if (value < 0f) { percentage = 0f; callbackInfo.cancel = true; }
        else if (value > 1f) { percentage = 1f; callbackInfo.cancel = true; }
    }
    @StyleProperty(setterCallbackPre = "percentageValidation")
    public float percentage = 0f;

    @Override
    public void onRenderUpdate(RenderOpQueue queue, boolean focused)
    {
        super.onRenderUpdate(queue, focused);
        if (rect.width == 0 || rect.height == 0) return;
        queue.enqueue(new ProgressBarOp(rect, percentage, backgroundColor, fillerColor, outlineColor));
    }
}
