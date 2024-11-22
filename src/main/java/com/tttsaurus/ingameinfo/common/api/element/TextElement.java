package com.tttsaurus.ingameinfo.common.api.element;

import com.tttsaurus.ingameinfo.common.api.render.renderer.TextRenderer;

public class TextElement extends Element
{
    private final TextRenderer simpleTextRenderer = new TextRenderer();

    public TextElement(String text, float scale, int color)
    {
        simpleTextRenderer.setText(text);
        simpleTextRenderer.setScale(scale);
        simpleTextRenderer.setColor(color);
        rect.width = simpleTextRenderer.simulateWidth();
        rect.height = simpleTextRenderer.simulateHeight();
    }

    @Override
    public void onRenderUpdate()
    {
        simpleTextRenderer.setX(rect.x);
        simpleTextRenderer.setY(rect.y);
        simpleTextRenderer.render();
    }
}
