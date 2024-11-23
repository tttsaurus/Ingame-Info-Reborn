package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ElementGroup extends Element
{
    protected final List<Element> elements = new ArrayList<>();

    public ElementGroup add(Element element)
    {
        element.outmost = false;
        elements.add(element);
        return this;
    }
    public ElementGroup remove(Element element)
    {
        elements.remove(element);
        return this;
    }

    @Override
    protected void calcWidthHeight()
    {
        for (Element element: elements)
            element.calcWidthHeight();
    }

    @Override
    protected void onFixedUpdate(double deltaTime)
    {
        for (Element element: elements)
            element.onFixedUpdate(deltaTime);
    }
    @Override
    protected void onRenderUpdate()
    {
        for (Element element: elements)
            element.onRenderUpdate();
    }

    @Override
    protected void renderDebugRect()
    {
        RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, Color.ORANGE.getRGB());
        for (Element element: elements)
            element.renderDebugRect();
    }
}
