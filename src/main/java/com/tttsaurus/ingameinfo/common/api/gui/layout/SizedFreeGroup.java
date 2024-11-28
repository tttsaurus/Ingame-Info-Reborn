package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.Element;

public class SizedFreeGroup extends ElementGroup
{
    // pivot doesn't change how the layout is calculated

    protected float width;
    protected float height;

    protected SizedFreeGroup()
    {

    }

    public SizedFreeGroup(float width, float height)
    {
        this.width = width;
        this.height = height;
    }

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        if (elements.isEmpty()) return;
        for (Element element: elements)
        {
            element.rect.x = rect.x + rect.width * element.alignment.vertical;
            element.rect.y = rect.y + rect.height * element.alignment.horizontal;
            if (element.pivot.vertical == 0 || element.pivot.vertical == 0.5f) element.rect.x += element.padding.left;
            if (element.pivot.vertical == 1 || element.pivot.vertical == 0.5f) element.rect.x -= element.padding.right;
            if (element.pivot.horizontal == 0 || element.pivot.horizontal == 0.5f) element.rect.y += element.padding.top;
            if (element.pivot.horizontal == 1 || element.pivot.horizontal == 0.5f) element.rect.y -= element.padding.bottom;
            element.calcRenderPos(rect);
        }
    }

    @Override
    public void calcWidthHeight()
    {
        super.calcWidthHeight();
        rect.width = width;
        rect.height = height;
    }
}
