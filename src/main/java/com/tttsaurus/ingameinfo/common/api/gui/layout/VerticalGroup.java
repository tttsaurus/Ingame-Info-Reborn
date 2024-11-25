package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.Element;

public class VerticalGroup extends ElementGroup
{
    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        if (elements.isEmpty()) return;
        if (pivot.horizontal == 0)
        {
            Element first = elements.get(0);
            first.rect.y += rect.y + first.padding.top;
            for (int i = 1; i < elements.size(); i++)
            {
                Element element = elements.get(i);
                Element prevElement = elements.get(i - 1);
                element.rect.y += prevElement.rect.y + prevElement.rect.height + prevElement.padding.bottom + element.padding.top;
            }
        }
        if (pivot.horizontal == 1)
        {
            Element first = elements.get(elements.size() - 1);
            first.rect.y += rect.y + rect.height - first.padding.bottom - first.rect.height;
            for (int i = elements.size() - 1; i > 0; i--)
            {
                Element element = elements.get(i);
                Element nextElement = elements.get(i - 1);
                nextElement.rect.y = element.rect.y - element.padding.top - nextElement.padding.bottom - nextElement.rect.height;
            }
        }
        if (pivot.vertical == 0)
            for (Element element: elements)
                element.rect.x += rect.x + element.padding.left;
        if (pivot.vertical == 1)
            for (Element element: elements)
                element.rect.x += rect.x + rect.width - element.padding.right - element.rect.width;

        for (Element element: elements)
        {
            element.rect.x += element.rect.width * element.pivot.vertical;
            element.rect.y += element.rect.height * element.pivot.horizontal;
            element.calcRenderPos(rect);
        }
    }

    @Override
    public void calcWidthHeight()
    {
        super.calcWidthHeight();
        for (Element element: elements)
        {
            rect.width = Math.max(rect.width, element.rect.width + element.padding.left + element.padding.right);
            rect.height += element.rect.height + element.padding.top + element.padding.bottom;
        }
    }
}
