package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.render.renderer.TextRenderer;

public class TextElement extends Element
{
    private final TextRenderer textRenderer = new TextRenderer();

    public TextElement(String text, float scale, int color)
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
