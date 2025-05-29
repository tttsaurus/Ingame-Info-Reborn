package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.delegate.button.IMouseEnterButton;
import com.tttsaurus.ingameinfo.common.core.gui.delegate.button.IMouseLeaveButton;
import com.tttsaurus.ingameinfo.common.core.gui.delegate.button.IMousePressButton;
import com.tttsaurus.ingameinfo.common.core.gui.delegate.button.IMouseReleaseButton;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.property.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.property.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.property.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.gui.render.ButtonOp;

// todo: another ButtonPro to handle complicated logic
@RegisterElement
public class Button extends AbstractButton
{
    private int currentColor;

    @StyleProperty
    public int defaultColor;

    @StyleProperty
    public int hoverColor;

    @StyleProperty
    public int holdColor;

    private int currentTextColor;

    @StyleProperty
    public int defaultTextColor;

    @StyleProperty
    public int hoverTextColor;

    @StyleProperty
    public int holdTextColor;

    @StylePropertyCallback
    public void textValidation(String value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StylePropertyCallback
    public void setTextCallback()
    {
        width = RenderUtils.simulateTextWidth(text, 1f) + 10;
        height = RenderUtils.simulateTextHeight(1f) + 10;
        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setTextCallback", setterCallbackPre = "textValidation")
    public String text;

    @StyleProperty
    public boolean shadow = true;

    public Button()
    {
        addListener(new IMouseEnterButton()
        {
            @Override
            public void enter()
            {
                currentColor = hoverColor;
                currentTextColor = hoverTextColor;
            }
        }).addListener(new IMousePressButton()
        {
            @Override
            public void press()
            {
                currentColor = holdColor;
                currentTextColor = holdTextColor;
            }
        }).addListener(new IMouseLeaveButton()
        {
            @Override
            public void leave()
            {
                currentColor = defaultColor;
                currentTextColor = defaultTextColor;
            }
        }).addListener(new IMouseReleaseButton()
        {
            @Override
            public void release()
            {
                currentColor = defaultColor;
                currentTextColor = defaultTextColor;
            }
        });
    }

    @Override
    public void onRenderUpdate(RenderOpQueue queue, boolean focused)
    {
        super.onRenderUpdate(queue, focused);
        queue.enqueue(new ButtonOp(
                rect,
                currentColor,
                text,
                rect.x + (rect.width - RenderUtils.simulateTextWidth(text, 1f)) / 2f,
                rect.y + (rect.height - RenderUtils.simulateTextHeight(1f)) / 2f,
                1f,
                currentTextColor,
                shadow));
    }

    // todo: a stage to handle logic theme + applyThemeEarly
    @Override
    public void loadTheme(ThemeConfig themeConfig)
    {
        super.loadTheme(themeConfig);

        if (defaultColor == 0)
            setStyleProperty("defaultColor", themeConfig.button.parsedDefaultColor);
        if (hoverColor == 0)
            setStyleProperty("hoverColor", themeConfig.button.parsedHoverColor);
        if (holdColor == 0)
            setStyleProperty("holdColor", themeConfig.button.parsedHoldColor);
        if (defaultTextColor == 0)
            setStyleProperty("defaultTextColor", themeConfig.button.parsedDefaultTextColor);
        if (hoverTextColor == 0)
            setStyleProperty("hoverTextColor", themeConfig.button.parsedHoverTextColor);
        if (holdTextColor == 0)
            setStyleProperty("holdTextColor", themeConfig.button.parsedHoldTextColor);

        currentColor = defaultColor;
        currentTextColor = defaultTextColor;
    }
}
