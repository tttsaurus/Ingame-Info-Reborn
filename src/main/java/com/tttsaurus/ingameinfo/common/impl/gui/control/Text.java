package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.TextRenderer;

@RegisterElement
public class Text extends Element
{
    private final TextRenderer textRenderer = new TextRenderer();

    public Text()
    {
        color = DEFAULT_COLOR_LIGHT;
        textRenderer.setColor(color);
    }

    @StylePropertyCallback
    public void textValidation(String value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StylePropertyCallback
    public void setTextCallback()
    {
        textRenderer.setText(text);
        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setTextCallback", setterCallbackPre = "textValidation")
    public String text;

    @StylePropertyCallback
    public void scaleValidation(float value, CallbackInfo callbackInfo)
    {
        if (value < 0) callbackInfo.cancel = true;
    }
    @StylePropertyCallback
    public void setScaleCallback()
    {
        textRenderer.setScale(scale);
        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setScaleCallback", setterCallbackPre = "scaleValidation")
    public float scale;

    @StylePropertyCallback
    public void setColorCallback()
    {
        textRenderer.setColor(color);
    }
    @StyleProperty(setterCallbackPost = "setColorCallback")
    public int color;

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        textRenderer.setX(rect.x);
        textRenderer.setY(rect.y);
    }

    @Override
    public void calcWidthHeight()
    {
        rect.width = textRenderer.simulateWidth();
        rect.height = textRenderer.simulateHeight();
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }

    @Override
    public void onRenderUpdate(boolean focused)
    {
        textRenderer.render();
    }
}
