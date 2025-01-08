package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.TextRenderer;

@RegisterElement
public class SlidingText extends Sized
{
    private final TextRenderer textRenderer = new TextRenderer();
    private final TextRenderer secondTextRenderer = new TextRenderer();

    public SlidingText()
    {
        color = DEFAULT_COLOR_LIGHT;
        textRenderer.setColor(color);
        secondTextRenderer.setColor(color);
    }

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
        textRenderer.setText(text);
        secondTextRenderer.setText(text);
        simulatedWidth = textRenderer.simulateWidth();
        needSliding = simulatedWidth > width;
        height = textRenderer.simulateHeight();
        xShift = 0;
        freezeTimer = 0;
        onFreezeTiming = true;
        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setTextCallback", setterCallbackPre = "textValidation")
    public String text;

    @StylePropertyCallback
    public void setScaleCallback()
    {
        textRenderer.setScale(scale);
        secondTextRenderer.setScale(scale);
        simulatedWidth = textRenderer.simulateWidth();
        needSliding = simulatedWidth > width;
        height = textRenderer.simulateHeight();
        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setScaleCallback", setterCallbackPre = "nonNegativeFloatValidation")
    public float scale;

    @StylePropertyCallback
    public void setColorCallback()
    {
        textRenderer.setColor(color);
        secondTextRenderer.setColor(color);
    }
    @StyleProperty(setterCallbackPost = "setColorCallback")
    public int color;

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        textRenderer.setY(rect.y);
        secondTextRenderer.setY(rect.y);
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {
        if ((!onDemandSliding || needSliding) && !onFreezeTiming)
            xShift += (float)(deltaTime * xShiftSpeed);
        if (onFreezeTiming)
        {
            if (freezeTimer > freezeTime)
            {
                freezeTimer = 0;
                onFreezeTiming = false;
            }
            freezeTimer += (float)deltaTime;
        }
    }

    @Override
    public void onRenderUpdate(boolean focused)
    {
        if (!onDemandSliding || needSliding)
        {
            RenderUtils.startRectStencil(rect.x, rect.y, rect.width, rect.height, 1);

            if (forwardSliding)
            {
                float x = rect.x + xShift;
                if (x < rect.x + rect.width)
                {
                    textRenderer.setX(x);
                    textRenderer.render();
                }
                if (xShift > spareWidth)
                {
                    float secondX = rect.x - simulatedWidth - spareWidth + xShift;
                    if (secondX >= rect.x)
                    {
                        xShift = 0;
                        onFreezeTiming = true;
                    }
                    else
                    {
                        secondTextRenderer.setX(secondX);
                        secondTextRenderer.render();
                    }
                }
            }
            else
            {
                float x = rect.x - xShift;
                if (x > rect.x - simulatedWidth)
                {
                    textRenderer.setX(x);
                    textRenderer.render();
                }
                if (xShift > spareWidth + simulatedWidth - rect.width)
                {
                    float secondX = rect.x + spareWidth + simulatedWidth - xShift;
                    if (secondX <= rect.x)
                    {
                        xShift = 0;
                        onFreezeTiming = true;
                    }
                    else
                    {
                        secondTextRenderer.setX(secondX);
                        secondTextRenderer.render();
                    }
                }
            }

            RenderUtils.endStencil();
        }
        else
        {
            textRenderer.setX(rect.x);
            textRenderer.render();
        }
    }
}
