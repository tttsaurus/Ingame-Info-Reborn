package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.delegate.button.IMouseEnterButton;
import com.tttsaurus.ingameinfo.common.core.gui.delegate.button.IMouseLeaveButton;
import com.tttsaurus.ingameinfo.common.core.gui.delegate.button.IMousePressButton;
import com.tttsaurus.ingameinfo.common.core.gui.delegate.button.IMouseReleaseButton;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.gui.GuiResources;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.TextRenderer;

@RegisterElement
public class Button extends AbstractButton
{
    private final TextRenderer textRenderer = new TextRenderer();

    @StyleProperty
    public String style = "";

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
        textRenderer.setText(text);
        width = textRenderer.simulateWidth() + 10;
        height = textRenderer.simulateHeight() + 10;
        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setTextCallback", setterCallbackPre = "textValidation")
    public String text;

    @StylePropertyCallback
    public void setShadowCallback()
    {
        textRenderer.setShadow(shadow);
    }
    @StyleProperty(setterCallbackPost = "setShadowCallback")
    public boolean shadow = true;

    public Button()
    {
        textRenderer.setShadow(true);
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
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        textRenderer.setX(rect.x + (rect.width - textRenderer.simulateWidth()) / 2f);
        textRenderer.setY(rect.y + (rect.height - textRenderer.simulateHeight()) / 2f);
    }

    @Override
    public void onRenderUpdate(boolean focused)
    {
        super.onRenderUpdate(focused);
        switch (style)
        {
            case "simple" ->
            {
                RenderUtils.renderRoundedRectOutline(rect.x, rect.y, rect.width, rect.height, 5f, 1f, currentColor);
                textRenderer.setColor(currentTextColor);
                textRenderer.render();
            }
            case "mc-vanilla" ->
            {
                RenderUtils.renderNinePatchBorderByPixel(rect.x, rect.y, rect.width, rect.height, GuiResources.mcVanillaButton, currentColor);
                textRenderer.setColor(currentTextColor);
                textRenderer.render();
            }
        }
    }

    @Override
    public void loadTheme(ThemeConfig themeConfig)
    {
        super.loadTheme(themeConfig);

        if (style.isEmpty())
            setStyleProperty("style", themeConfig.button.style);

        if (defaultColor == 0 && style.equals("simple"))
            setStyleProperty("defaultColor", themeConfig.button.simple.parsedDefaultColor);
        if (hoverColor == 0 && style.equals("simple"))
            setStyleProperty("hoverColor", themeConfig.button.simple.parsedHoverColor);
        if (holdColor == 0 && style.equals("simple"))
            setStyleProperty("holdColor", themeConfig.button.simple.parsedHoldColor);
        if (defaultTextColor == 0 && style.equals("simple"))
            setStyleProperty("defaultTextColor", themeConfig.button.simple.parsedDefaultTextColor);
        if (hoverTextColor == 0 && style.equals("simple"))
            setStyleProperty("hoverTextColor", themeConfig.button.simple.parsedHoverTextColor);
        if (holdTextColor == 0 && style.equals("simple"))
            setStyleProperty("holdTextColor", themeConfig.button.simple.parsedHoldTextColor);

        if (defaultColor == 0 && style.equals("mc-vanilla"))
            setStyleProperty("defaultColor", themeConfig.button.mcVanilla.parsedDefaultColor);
        if (hoverColor == 0 && style.equals("mc-vanilla"))
            setStyleProperty("hoverColor", themeConfig.button.mcVanilla.parsedHoverColor);
        if (holdColor == 0 && style.equals("mc-vanilla"))
            setStyleProperty("holdColor", themeConfig.button.mcVanilla.parsedHoldColor);
        if (defaultTextColor == 0 && style.equals("mc-vanilla"))
            setStyleProperty("defaultTextColor", themeConfig.button.mcVanilla.parsedDefaultTextColor);
        if (hoverTextColor == 0 && style.equals("mc-vanilla"))
            setStyleProperty("hoverTextColor", themeConfig.button.mcVanilla.parsedHoverTextColor);
        if (holdTextColor == 0 && style.equals("mc-vanilla"))
            setStyleProperty("holdTextColor", themeConfig.button.mcVanilla.parsedHoldTextColor);

        currentColor = defaultColor;
        currentTextColor = defaultTextColor;
    }
}
