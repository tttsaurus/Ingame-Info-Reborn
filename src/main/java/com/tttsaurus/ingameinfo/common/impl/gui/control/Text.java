package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.TextRenderer;

public class Text extends Element
{
    private final TextRenderer textRenderer = new TextRenderer();

    public Text(String text, float scale, int color)
    {
        textRenderer.setText(text);
        textRenderer.setScale(scale);
        textRenderer.setColor(color);
    }

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        textRenderer.setX(rect.x);
        textRenderer.setY(rect.y);
    }

    @Override
    public void calcWidthHeight()
    {
        rect.width = textRenderer.simulateWidth();
        rect.height = textRenderer.simulateHeight();
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }
    @Override
    public void onRenderUpdate(boolean focused)
    {
        textRenderer.render();
    }
}
