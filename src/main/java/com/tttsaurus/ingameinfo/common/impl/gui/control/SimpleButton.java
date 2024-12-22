package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.IMouseEnterButton;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.IMouseLeaveButton;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.IMousePressButton;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.IMouseReleaseButton;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.gui.style.wrapped.IntProperty;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.TextRenderer;

@RegisterElement
public class SimpleButton extends Button
{
    private final TextRenderer textRenderer = new TextRenderer();

    @StyleProperty
    public IntProperty currentColor = new IntProperty();

    @StyleProperty
    public IntProperty defaultColor = new IntProperty();

    @StyleProperty
    public IntProperty hoverColor = new IntProperty();

    @StyleProperty
    public IntProperty holdColor = new IntProperty();

    @StylePropertyCallback
    public void setTextCallback()
    {
        textRenderer.setText(text);
        width = textRenderer.simulateWidth() + 4;
        height = textRenderer.simulateHeight() + 4;
        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setTextCallback")
    public String text;

    public SimpleButton()
    {
        defaultColor.set(DEFAULT_COLOR_DARK);
        hoverColor.set(DEFAULT_COLOR_LIGHT);
        holdColor.set(DEFAULT_COLOR_DARKER);
        currentColor.set(defaultColor.get());

        addListener(new IMouseEnterButton()
        {
            @Override
            public void enter() { currentColor.set(hoverColor.get()); }
        }).addListener(new IMousePressButton()
        {
            @Override
            public void press()
            {
                currentColor.set(holdColor.get());
            }
        }).addListener(new IMouseLeaveButton()
        {
            @Override
            public void leave()
            {
                currentColor.set(defaultColor.get());
            }
        }).addListener(new IMouseReleaseButton()
        {
            @Override
            public void release()
            {
                currentColor.set(defaultColor.get());
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
        textRenderer.setColor(currentColor.get());
        textRenderer.render();
        RenderUtils.renderRoundedRectOutline(rect.x, rect.y, rect.width, rect.height, 5f, 1f, currentColor.get());
    }
}
