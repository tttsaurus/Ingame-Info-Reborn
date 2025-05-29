package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.animation.text.CharInfo;
import com.tttsaurus.ingameinfo.common.core.animation.text.ITextAnimDef;
import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.property.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.property.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.property.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.gui.render.AnimTextOp;
import com.tttsaurus.ingameinfo.common.core.gui.property.wrapped.DoubleProperty;
import java.util.Arrays;

@RegisterElement
public class AnimText extends Element
{
    private CharInfo[] charInfos = new CharInfo[0];
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
        int oldLength = charInfos.length;
        int length = text.length();

        float width = 0;
        for (int i = 0; i < oldLength; i++)
        {
            char c = text.charAt(i);
            charInfos[i].x = width;
            width += RenderUtils.fontRenderer.getCharWidth(c) * charInfos[i].scale;
        }

        charInfos = Arrays.copyOf(charInfos, length);

        for (int i = oldLength; i < length; i++)
        {
            char c = text.charAt(i);
            charInfos[i] = new CharInfo(width, 0f, scale, color, shadow);
            width += RenderUtils.fontRenderer.getCharWidth(c) * scale;
        }

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
        float width = 0;
        for (int i = 0; i < charInfos.length; i++)
        {
            char c = text.charAt(i);
            charInfos[i].x = width;
            charInfos[i].scale = scale;
            width += RenderUtils.fontRenderer.getCharWidth(c) * charInfos[i].scale;
        }

        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setScaleCallback", setterCallbackPre = "scaleValidation")
    public float scale = 1;

    @StylePropertyCallback
    public void setColorCallback()
    {
        for (CharInfo info : charInfos)
            info.color = color;
    }
    @StyleProperty(setterCallbackPost = "setColorCallback")
    public int color;

    @StylePropertyCallback
    public void setShadowCallback()
    {
        for (CharInfo info : charInfos)
            info.shadow = shadow;
    }
    @StyleProperty(setterCallbackPost = "setShadowCallback")
    public boolean shadow;

    @Override
    public void calcWidthHeight()
    {
        rect.width = RenderUtils.simulateTextWidth(text, scale);
        rect.height = RenderUtils.simulateTextHeight(scale);
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {
        if (timer.get() == null || timer.get() == Double.POSITIVE_INFINITY)
            timer.set(0d);
        animDef.calcAnim(charInfos, timer, deltaTime);
        timer.set(timer.get() + deltaTime);
    }

    @Override
    public void onRenderUpdate(RenderOpQueue queue, boolean focused)
    {
        super.onRenderUpdate(queue, focused);
        queue.enqueue(new AnimTextOp(text, rect.x, rect.y, scale, color, charInfos));
    }
}
