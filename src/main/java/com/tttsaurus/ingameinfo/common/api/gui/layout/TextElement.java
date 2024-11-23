package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.render.renderer.TextRenderer;

public class TextElement extends Element
{
    private final TextRenderer simpleTextRenderer = new TextRenderer();

    public TextElement(String text, float scale, int color)
    {
        simpleTextRenderer.setText(text);
        simpleTextRenderer.setScale(scale);
        simpleTextRenderer.setColor(color);
    }

    @Override
    protected void calcWidthHeight()
    {
        rect.width = simpleTextRenderer.simulateWidth();
        rect.height = simpleTextRenderer.simulateHeight();
    }

    @Override
    protected void onFixedUpdate(double deltaTime)
    {

    }
    @Override
    protected void onRenderUpdate()
    {
        simpleTextRenderer.setX(rect.x);
        simpleTextRenderer.setY(rect.y);
        simpleTextRenderer.render();
    }
}
