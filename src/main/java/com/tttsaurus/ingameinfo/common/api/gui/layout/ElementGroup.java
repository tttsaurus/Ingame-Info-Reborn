package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ElementGroup extends Element
{
    protected final List<Element> elements = new ArrayList<>();

    public ElementGroup add(Element element)
    {
        elements.add(element);
        return this;
    }
    public ElementGroup remove(Element element)
    {
        elements.remove(element);
        return this;
    }

    @Override
    public void resetRenderInfo()
    {
        super.resetRenderInfo();
        for (Element element: elements)
            element.resetRenderInfo();
    }

    // how calcRenderPos() is implemented depends on my specific group type

    @Override
    public void calcWidthHeight()
    {
        for (Element element: elements)
            element.calcWidthHeight();
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {
        for (Element element: elements)
            element.onFixedUpdate(deltaTime);
    }
    @Override
    public void onRenderUpdate(boolean focused)
    {
        for (Element element: elements)
            element.onRenderUpdate(focused);
    }

    @Override
    public void renderDebugRect()
    {
        RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, Color.ORANGE.getRGB());
        RenderUtils.renderRect(pivotPosX - 1, pivotPosY - 1, 3, 3, Color.GREEN.getRGB());
        for (Element element: elements)
            element.renderDebugRect();
    }
}
