package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
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
    private boolean onFreezeTiming = false;

    @StyleProperty
    public float spareWidth = 10f;

    @StyleProperty
    public float xShiftSpeed = 8f;

    @StyleProperty
    public boolean onDemandSliding;

    @StyleProperty
    public float freezeTime = 2f;

    @StylePropertyCallback
    public void setTextCallback()
    {
        textRenderer.setText(text);
        secondTextRenderer.setText(text);
        simulatedWidth = textRenderer.simulateWidth();
        needSliding = simulatedWidth > width;
        height = textRenderer.simulateHeight();
        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setTextCallback")
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
    @StyleProperty(setterCallbackPost = "setScaleCallback")
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
            RenderUtils.startStencil(rect.x, rect.y, rect.width, rect.height, 1);

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

            RenderUtils.endStencil();
        }
        else
        {
            textRenderer.setX(rect.x);
            textRenderer.render();
        }
    }
}
