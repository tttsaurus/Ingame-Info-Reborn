package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ElementGroup extends Element
{
    public final List<Element> elements = new ArrayList<>();

    public void add(Element element)
    {
        elements.add(element);
    }

    @Override
    public void resetRenderInfo()
    {
        super.resetRenderInfo();
        for (Element element: elements)
            element.resetRenderInfo();
    }

    // how calcRenderPos() is implemented depends on my specific group types

    @Override
    public void calcWidthHeight()
    {
        for (Element element: elements)
        {
            element.calcWidthHeight();
            if (!element.enabled)
            {
                element.cachedWidth = element.rect.width;
                element.cachedHeight = element.rect.height;
                element.rect.width = 0f;
                element.rect.height = 0f;
            }
        }
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {
        if (!enabled) return;
        for (Element element: elements)
            if (element.enabled)
                element.onFixedUpdate(deltaTime);
    }
    @Override
    public void onRenderUpdate(boolean focused)
    {
        if (!enabled) return;
        for (Element element: elements)
            if (element.enabled)
                element.onRenderUpdate(focused);
    }

    @Override
    public void renderBackground()
    {
        super.renderBackground();
        for (Element element: elements)
            element.renderBackground();
    }

    @Override
    public boolean getNeedReCalc()
    {
        if (super.getNeedReCalc()) return true;
        for (Element element: elements)
            if (element.getNeedReCalc()) return true;
        return false;
    }
    @Override
    public void finishReCalc()
    {
        super.finishReCalc();
        for (Element element: elements)
            element.finishReCalc();
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
