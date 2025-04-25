package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.IMouseEnterButton;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.IMouseLeaveButton;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.IMousePressButton;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.IMouseReleaseButton;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.api.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.gui.GuiResources;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.TextRenderer;

@RegisterElement
public class Button extends AbstractButton
{
    private final TextRenderer textRenderer = new TextRenderer();

    private int currentColor;

    @StyleProperty
    public int defaultColor;

    @StyleProperty
    public int hoverColor;

    @StyleProperty
    public int holdColor;

    @StylePropertyCallback
    public void textValidation(String value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StylePropertyCallback
    public void setTextCallback()
    {
        textRenderer.setText(text);
        width = textRenderer.simulateWidth() + 4;
        height = textRenderer.simulateHeight() + 4;
        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setTextCallback", setterCallbackPre = "textValidation")
    public String text;

    public Button()
    {
        addListener(new IMouseEnterButton()
        {
            @Override
            public void enter()
            {
                currentColor = hoverColor;
            }
        }).addListener(new IMousePressButton()
        {
            @Override
            public void press()
            {
                currentColor = holdColor;
            }
        }).addListener(new IMouseLeaveButton()
        {
            @Override
            public void leave()
            {
                currentColor = defaultColor;
            }
        }).addListener(new IMouseReleaseButton()
        {
            @Override
            public void release()
            {
                currentColor = defaultColor;
            }
        });
    }

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        textRenderer.setX(rect.x + 2);
        textRenderer.setY(rect.y + 2);
    }

    @Override
    public void onRenderUpdate(boolean focused)
    {
        super.onRenderUpdate(focused);
        textRenderer.setColor(currentColor);
        textRenderer.render();
        RenderUtils.renderRoundedRectOutline(rect.x, rect.y, rect.width, rect.height, 5f, 1f, currentColor);
        //RenderUtils.renderNinePatchBorderByPixel(rect.x, rect.y, rect.width, rect.height, GuiResources.mcVanillaButton, -1);
    }

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

        currentColor = defaultColor;
    }
}
