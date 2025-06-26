package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.control.Sized;
import com.tttsaurus.ingameinfo.common.core.gui.property.lerp.LerpCenter;
import com.tttsaurus.ingameinfo.common.core.gui.property.lerp.LerpTarget;
import com.tttsaurus.ingameinfo.common.core.gui.property.lerp.LerpableProperty;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.gui.render.MaskEndOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.MaskStartOp;
import com.tttsaurus.ingameinfo.common.impl.gui.render.SlidingTextOp;

@RegisterElement
public class SlidingText extends Sized
{
    @LerpTarget("xShift")
    private final LerpableProperty<Float> lerpableXShift = new LerpableProperty<Float>()
    {
        @Override
        public Float lerp(float percentage)
        {
            if (currValue == 0f) prevValue = 0f;
            return LerpCenter.lerp(prevValue, currValue, percentage);
        }
    };

    private float xShift = 0;
    private boolean needSliding;
    private float simulatedWidth;
    private float freezeTimer = 0;
    private boolean onFreezeTiming = true;

    @StyleProperty(setterCallbackPre = "nonNegativeFloatValidation")
    public float spareWidth = 10f;

    @StyleProperty(setterCallbackPre = "nonNegativeFloatValidation")
    public float xShiftSpeed = 8f;

    @StyleProperty(setterCallbackPre = "nonNegativeFloatValidation")
    public float freezeTime = 2f;

    @StyleProperty
    public boolean onDemandSliding = false;

    @StylePropertyCallback
    public void setForwardSlidingCallback()
    {
        xShift = 0;
        freezeTimer = 0;
        onFreezeTiming = true;
    }
    @StyleProperty(setterCallbackPost = "setForwardSlidingCallback")
    public boolean forwardSliding = false;

    @StylePropertyCallback
    public void textValidation(String value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StylePropertyCallback
    public void setTextCallback()
    {
        simulatedWidth = RenderUtils.simulateTextWidth(text, scale);
        needSliding = simulatedWidth > width;
        height = RenderUtils.simulateTextHeight(scale);
        xShift = 0;
        freezeTimer = 0;
        onFreezeTiming = true;
        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setTextCallback", setterCallbackPre = "textValidation")
    public String text = "";

    @StylePropertyCallback
    public void setScaleCallback()
    {
        simulatedWidth = RenderUtils.simulateTextWidth(text, scale);
        needSliding = simulatedWidth > width;
        height = RenderUtils.simulateTextHeight(scale);
        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setScaleCallback", setterCallbackPre = "nonNegativeFloatValidation")
    public float scale = 0;

    @StyleProperty
    public int color;

    @StyleProperty
    public boolean shadow;

    @Override
    public void applyLogicTheme(ThemeConfig themeConfig)
    {
        super.applyLogicTheme(themeConfig);

        if (scale == 0)
            setStyleProperty("scale", themeConfig.slidingText.scale);
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {
        if ((!onDemandSliding || needSliding) && !onFreezeTiming)
            xShift += (float)(deltaTime * xShiftSpeed);
        if (onFreezeTiming)
        {
            xShift = 0;
            if (freezeTimer > freezeTime)
            {
                freezeTimer = 0;
                onFreezeTiming = false;
            }
            freezeTimer += (float)deltaTime;
        }
    }

    @Override
    public void onRenderUpdate(RenderOpQueue queue, boolean focused)
    {
        super.onRenderUpdate(queue, focused);

        if (!onDemandSliding || needSliding)
        {
            queue.enqueue(new MaskStartOp(rect));

            if (forwardSliding)
            {
                if (rect.x + xShift < rect.x + rect.width)
                    queue.enqueue(new SlidingTextOp(text, rect.x, rect.y, lerpableXShift, true, scale, color, shadow));

                if (xShift > spareWidth)
                {
                    if (rect.x - simulatedWidth - spareWidth + xShift >= rect.x)
                    {
                        onFreezeTiming = true;
                        queue.enqueue(new SlidingTextOp(text, rect.x, rect.y, scale, color, shadow));
                    }
                    else
                        queue.enqueue(new SlidingTextOp(text, rect.x - simulatedWidth - spareWidth, rect.y, lerpableXShift, true, scale, color, shadow));
                }
            }
            else
            {
                if (rect.x - xShift > rect.x - simulatedWidth)
                    queue.enqueue(new SlidingTextOp(text, rect.x, rect.y, lerpableXShift, false, scale, color, shadow));

                if (xShift > spareWidth + simulatedWidth - rect.width)
                {
                    if (rect.x + spareWidth + simulatedWidth - xShift <= rect.x)
                    {
                        onFreezeTiming = true;
                        queue.enqueue(new SlidingTextOp(text, rect.x, rect.y, scale, color, shadow));
                    }
                    else
                        queue.enqueue(new SlidingTextOp(text, rect.x + spareWidth + simulatedWidth, rect.y, lerpableXShift, false, scale, color, shadow));
                }
            }

            queue.enqueue(new MaskEndOp());
        }
        else
            queue.enqueue(new SlidingTextOp(text, rect.x, rect.y, scale, color, shadow));
    }
}
