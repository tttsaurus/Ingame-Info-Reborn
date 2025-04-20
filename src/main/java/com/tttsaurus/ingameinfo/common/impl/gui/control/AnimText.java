package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.animation.text.ITextAnimDef;
import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.api.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.impl.gui.style.wrapped.DoubleProperty;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.AnimTextRenderer;

@RegisterElement
public class AnimText extends Element
{
    private final AnimTextRenderer animTextRenderer = new AnimTextRenderer();
    private final DoubleProperty timer = new DoubleProperty();

    @StylePropertyCallback
    public void textAnimDefValidation(ITextAnimDef value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StyleProperty(setterCallbackPre = "textAnimDefValidation")
    public ITextAnimDef animDef;

    @StylePropertyCallback
    public void textValidation(String value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StylePropertyCallback
    public void setTextCallback()
    {
        animTextRenderer.setText(text);
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
        animTextRenderer.setScale(scale);
        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setScaleCallback", setterCallbackPre = "scaleValidation")
    public float scale;

    @StylePropertyCallback
    public void setColorCallback()
    {
        animTextRenderer.setColor(color);
    }
    @StyleProperty(setterCallbackPost = "setColorCallback")
    public int color;

    @StylePropertyCallback
    public void setShadowCallback()
    {
        animTextRenderer.setShadow(shadow);
    }
    @StyleProperty(setterCallbackPost = "setShadowCallback")
    public boolean shadow;

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        animTextRenderer.setX(rect.x);
        animTextRenderer.setY(rect.y);
    }

    @Override
    public void calcWidthHeight()
    {
        rect.width = animTextRenderer.simulateWidth();
        rect.height = animTextRenderer.simulateHeight();
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {
        if (timer.get() == null || timer.get() == Double.POSITIVE_INFINITY)
            timer.set(0d);
        animDef.calcAnim(animTextRenderer.getCharacterInfos(), timer, deltaTime);
        timer.set(timer.get() + deltaTime);
    }

    @Override
    public void onRenderUpdate(boolean focused)
    {
        animTextRenderer.render();
    }

    @Override
    public void loadTheme(ThemeConfig themeConfig)
    {
        super.loadTheme(themeConfig);

        if (scale == 0f)
            setStyleProperty("scale", themeConfig.animText.scale);
        if (color == 0)
            setStyleProperty("color", themeConfig.animText.parsedColor);
    }
}
