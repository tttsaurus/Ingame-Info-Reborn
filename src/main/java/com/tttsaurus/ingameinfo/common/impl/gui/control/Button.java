package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.control.Interactable;
import com.tttsaurus.ingameinfo.common.core.gui.event.IUIEventListener;
import com.tttsaurus.ingameinfo.common.core.gui.event.UIEvent;
import com.tttsaurus.ingameinfo.common.core.gui.event.UIEventListenerType;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.gui.render.ButtonOp;

// todo: another ButtonPro to handle complicated logic
@RegisterElement
public class Button extends Interactable
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
        addEventListener(UIEvent.MouseEnter.class, new IUIEventListener<>()
        {
            @Override
            public void handle(UIEvent.MouseEnter event)
            {
                currentColor = hoverColor;
                currentTextColor = hoverTextColor;
            }

            @Override
            public UIEventListenerType type()
            {
                return UIEventListenerType.LOCAL;
            }
        });
        addEventListener(UIEvent.MousePress.class, new IUIEventListener<>()
        {
            @Override
            public void handle(UIEvent.MousePress event)
            {
                currentColor = holdColor;
                currentTextColor = holdTextColor;
            }

            @Override
            public UIEventListenerType type()
            {
                return UIEventListenerType.LOCAL;
            }
        });
        addEventListener(UIEvent.MouseLeave.class, new IUIEventListener<>()
        {
            @Override
            public void handle(UIEvent.MouseLeave event)
            {
                currentColor = defaultColor;
                currentTextColor = defaultTextColor;
            }

            @Override
            public UIEventListenerType type()
            {
                return UIEventListenerType.LOCAL;
            }
        });
        addEventListener(UIEvent.MouseRelease.class, new IUIEventListener<>()
        {
            @Override
            public void handle(UIEvent.MouseRelease event)
            {
                currentColor = defaultColor;
                currentTextColor = defaultTextColor;
            }

            @Override
            public UIEventListenerType type()
            {
                return UIEventListenerType.LOCAL;
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
                shadow,
                hover,
                hold));
    }

    @Override
    public void applyLogicTheme(ThemeConfig themeConfig)
    {
        super.applyLogicTheme(themeConfig);

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
