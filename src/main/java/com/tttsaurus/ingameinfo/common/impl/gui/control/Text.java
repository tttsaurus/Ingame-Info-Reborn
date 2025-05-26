package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.property.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.property.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.property.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.TextRenderer;

@RegisterElement
public class Text extends Element
{
    private final TextRenderer textRenderer = new TextRenderer();

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

    @StylePropertyCallback
    public void setShadowCallback()
    {
        textRenderer.setShadow(shadow);
    }
    @StyleProperty(setterCallbackPost = "setShadowCallback")
    public boolean shadow;

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
        super.onRenderUpdate(focused);
        textRenderer.render();
    }

    @Override
    public void loadTheme(ThemeConfig themeConfig)
    {
        super.loadTheme(themeConfig);

        if (scale == 0f)
            setStyleProperty("scale", themeConfig.text.scale);
        if (color == 0)
            setStyleProperty("color", themeConfig.text.parsedColor);
    }
}
