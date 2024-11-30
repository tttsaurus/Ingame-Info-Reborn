package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.Element;

public class VerticalGroup extends ElementGroup
{
    // stack elements vertically

    // pivot does change how the layout is calculated when skewness is null
    // skewness determines the layout when it's set
    // elements' alignment overrides the skewness
    protected Skewness skewness = Skewness.NULL;
    public VerticalGroup setSkewness(Skewness skewness) { this.skewness = skewness; return this; }

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        if (elements.isEmpty()) return;
        if (pivot.horizontal == 0 || pivot.horizontal == 0.5f)
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

        if ((pivot.vertical == 0 && skewness == Skewness.NULL) || (pivot.vertical == 0.5f && skewness == Skewness.NULL) || skewness == Skewness.LEFT)
            for (Element element: elements)
                if (element.alignment == Alignment.NULL)
                    element.rect.x += rect.x + element.padding.left;
                else
                {
                    element.rect.x += rect.x + rect.width * element.alignment.vertical;
                    if (element.pivot.vertical == 0 || element.pivot.vertical == 0.5f) element.rect.x += element.padding.left;
                    if (element.pivot.vertical == 1 || element.pivot.vertical == 0.5f) element.rect.x -= element.padding.right;
                }
        if ((pivot.vertical == 1 && skewness == Skewness.NULL) || skewness == Skewness.RIGHT)
            for (Element element: elements)
                if (element.alignment == Alignment.NULL)
                    element.rect.x += rect.x + rect.width - element.padding.right - element.rect.width;
                else
                {
                    element.rect.x += rect.x + rect.width * element.alignment.vertical;
                    if (element.pivot.vertical == 0 || element.pivot.vertical == 0.5f) element.rect.x += element.padding.left;
                    if (element.pivot.vertical == 1 || element.pivot.vertical == 0.5f) element.rect.x -= element.padding.right;
                }

        for (Element element: elements)
        {
            if (element.alignment == Alignment.NULL) element.rect.x += element.rect.width * element.pivot.vertical;
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
            rect.width = Math.max(rect.width, element.rect.width);
            rect.height += element.rect.height + element.padding.top + element.padding.bottom;
        }
    }
}
