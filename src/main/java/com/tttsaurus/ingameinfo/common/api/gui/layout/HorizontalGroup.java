package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.Element;

public class HorizontalGroup extends ElementGroup
{
    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        if (elements.isEmpty()) return;
        if (pivot.vertical == 0)
        {
            Element first = elements.get(0);
            first.rect.x += rect.x + first.padding.left;
            for (int i = 1; i < elements.size(); i++)
            {
                Element element = elements.get(i);
                Element prevElement = elements.get(i - 1);
                element.rect.x = prevElement.rect.x + prevElement.rect.width + prevElement.padding.right + element.padding.left;
            }
        }
        if (pivot.vertical == 1)
        {
            Element first = elements.get(elements.size() - 1);
            first.rect.x += rect.x + rect.width - first.padding.right - first.rect.width;
            for (int i = elements.size() - 1; i > 0; i--)
            {
                Element element = elements.get(i);
                Element nextElement = elements.get(i - 1);
                nextElement.rect.x = element.rect.x - element.padding.left - nextElement.padding.right - nextElement.rect.width;
            }
        }
        if (pivot.horizontal == 0)
            for (Element element: elements)
                element.rect.y += rect.y + element.padding.top;
        if (pivot.horizontal == 1)
            for (Element element: elements)
                element.rect.y += rect.y + rect.height - element.padding.bottom - element.rect.height;

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
            rect.width += element.rect.width + element.padding.left + element.padding.right;
            rect.height = Math.max(rect.height, element.rect.height + element.padding.top + element.padding.bottom);
        }
    }
}
