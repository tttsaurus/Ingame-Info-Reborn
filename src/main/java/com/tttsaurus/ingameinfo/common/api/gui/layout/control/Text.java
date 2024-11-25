package com.tttsaurus.ingameinfo.common.api.gui.layout.control;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Element;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.render.renderer.TextRenderer;

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
    protected void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        textRenderer.setX(rect.x);
        textRenderer.setY(rect.y);
    }

    @Override
    protected void calcWidthHeight()
    {
        rect.width = textRenderer.simulateWidth();
        rect.height = textRenderer.simulateHeight();
    }

    @Override
    protected void onFixedUpdate(double deltaTime)
    {

    }
    @Override
    protected void onRenderUpdate()
    {
        textRenderer.render();
    }
}
