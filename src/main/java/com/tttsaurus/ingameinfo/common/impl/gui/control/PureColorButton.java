package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.IMouseEnterButton;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.IMouseLeaveButton;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.IMousePressButton;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.IMouseReleaseButton;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.TextRenderer;
import java.awt.*;

@RegisterElement
public class PureColorButton extends Button
{
    private final TextRenderer textRenderer = new TextRenderer();

    protected int currentColor;
    protected int defaultColor;
    protected int hoverColor;
    protected int holdColor;
    protected String text;

    public PureColorButton(String text)
    {
        super(0, 0);

        textRenderer.setText(text);
        width = textRenderer.simulateWidth() + 4;
        height = textRenderer.simulateHeight() + 4;

        defaultColor = Color.GRAY.getRGB();
        hoverColor = Color.LIGHT_GRAY.getRGB();
        holdColor = Color.DARK_GRAY.getRGB();
        currentColor = defaultColor;

        this.text = text;

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
    }
}
