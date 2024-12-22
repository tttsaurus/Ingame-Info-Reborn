package com.tttsaurus.ingameinfo.common.impl.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Alignment;
import com.tttsaurus.ingameinfo.common.api.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Skewness;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;

@RegisterElement
public class HorizontalGroup extends ElementGroup
{
    // stack elements horizontally

    // pivot does change how the layout is calculated when skewness is null
    // skewness determines the layout when it's set
    // elements' alignment overrides the skewness
    @StyleProperty(setterCallbackPost = "requestReCalc")
    public Skewness skewness = Skewness.NULL;

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        if (elements.isEmpty()) return;
        if (pivot.vertical == 0 || pivot.vertical == 0.5f)
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

        if ((pivot.horizontal == 0 && skewness == Skewness.NULL) || (pivot.horizontal == 0.5f && skewness == Skewness.NULL) || skewness == Skewness.LEFT)
            for (Element element: elements)
                if (element.alignment == Alignment.NULL)
                    element.rect.y += rect.y + element.padding.top;
                else
                {
                    element.rect.y += rect.y + rect.height * element.alignment.horizontal;
                    if (element.pivot.horizontal == 0 || element.pivot.horizontal == 0.5f) element.rect.y += element.padding.top;
                    if (element.pivot.horizontal == 1 || element.pivot.horizontal == 0.5f) element.rect.y -= element.padding.bottom;
                }
        if ((pivot.horizontal == 1 && skewness == Skewness.NULL) || skewness == Skewness.RIGHT)
            for (Element element: elements)
                if (element.alignment == Alignment.NULL)
                    element.rect.y += rect.y + rect.height - element.padding.bottom - element.rect.height;
                else
                {
                    element.rect.y += rect.y + rect.height * element.alignment.horizontal;
                    if (element.pivot.horizontal == 0 || element.pivot.horizontal == 0.5f) element.rect.y += element.padding.top;
                    if (element.pivot.horizontal == 1 || element.pivot.horizontal == 0.5f) element.rect.y -= element.padding.bottom;
                }

        for (Element element: elements)
        {
            element.rect.x += element.rect.width * element.pivot.vertical;
            if (element.alignment == Alignment.NULL) element.rect.y += element.rect.height * element.pivot.horizontal;
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
            rect.height = Math.max(rect.height, element.rect.height);
        }
    }
}
