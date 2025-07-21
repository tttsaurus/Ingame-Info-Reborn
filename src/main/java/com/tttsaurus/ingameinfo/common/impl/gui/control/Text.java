package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.text.FormattedText;
import com.tttsaurus.ingameinfo.common.impl.gui.render.op.TextOp;

@RegisterElement
public class Text extends Element
{
    private FormattedText formattedText = RenderUtils.bakeFormattedText("");

    @StylePropertyCallback
    public void setTextCallback()
    {
        formattedText = RenderUtils.bakeFormattedText(text);
        requestReCalc();
    }
    @StylePropertyCallback
    public void textValidation(String value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StyleProperty(setterCallbackPost = "setTextCallback", setterCallbackPre = "textValidation")
    public String text = "";

    @StylePropertyCallback
    public void scaleValidation(float value, CallbackInfo callbackInfo)
    {
        if (value < 0) callbackInfo.cancel = true;
    }
    @StyleProperty(setterCallbackPost = "requestReCalc", setterCallbackPre = "scaleValidation")
    public float scale = 0;

    @StyleProperty
    public int color;

    @StylePropertyCallback
    public void setShadowCallback()
    {
        if (!isShadowInit) isShadowInit = true;
    }
    @StyleProperty(setterCallbackPost = "setShadowCallback")
    public boolean shadow;
    private boolean isShadowInit = false;

    @Override
    public void calcWidthHeight()
    {
        rect.width = formattedText.width * scale;
        rect.height = formattedText.height * scale;
    }

    @Override
    public void applyLogicTheme(ThemeConfig themeConfig)
    {
        super.applyLogicTheme(themeConfig);

        if (scale == 0)
            setStyleProperty("scale", themeConfig.text.scale);
        if (!isShadowInit)
        {
            isShadowInit = true;
            setStyleProperty("shadow", themeConfig.text.shadow);
        }
    }

    @Override
    public void onRenderUpdate(RenderOpQueue queue, boolean focused)
    {
        super.onRenderUpdate(queue, focused);
        queue.enqueue(new TextOp(formattedText, rect.x, rect.y, scale, color, shadow));
    }
}
