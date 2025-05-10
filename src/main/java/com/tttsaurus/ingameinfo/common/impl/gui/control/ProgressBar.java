package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.RenderMask;

@RegisterElement
public class ProgressBar extends Sized
{
    private final RenderMask mask = new RenderMask(RenderMask.MaskShape.ROUNDED_RECT);

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
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        mask.setRoundedRectMask(rect.x, rect.y, rect.width, rect.height, rect.height / 2f);
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }

    @Override
    public void onRenderUpdate(boolean focused)
    {
        super.onRenderUpdate(focused);
        if (rect.width == 0 || rect.height == 0) return;
        RenderUtils.renderRoundedRect(rect.x, rect.y, rect.width, rect.height, rect.height / 2f, backgroundColor);
        if (percentage != 0)
        {
            mask.startMasking();
            RenderUtils.renderRect(rect.x, rect.y, rect.width * percentage, rect.height, fillerColor);
            mask.endMasking();
        }
        RenderUtils.renderRoundedRectOutline(rect.x, rect.y, rect.width, rect.height, rect.height / 2f, 1.0f, outlineColor);
    }

    @Override
    public void loadTheme(ThemeConfig themeConfig)
    {
        super.loadTheme(themeConfig);

        if (fillerColor == 0)
            setStyleProperty("fillerColor", themeConfig.progressBar.parsedFillerColor);
        if (backgroundColor == 0)
            setStyleProperty("backgroundColor", themeConfig.progressBar.parsedBackgroundColor);
        if (outlineColor == 0)
            setStyleProperty("outlineColor", themeConfig.progressBar.parsedOutlineColor);
    }
}
