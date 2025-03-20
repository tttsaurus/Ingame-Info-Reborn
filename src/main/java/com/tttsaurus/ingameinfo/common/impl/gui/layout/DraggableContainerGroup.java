package com.tttsaurus.ingameinfo.common.impl.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;

@RegisterElement
public class DraggableContainerGroup extends SizedGroup
{
    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        for (Element element: elements)
        {
            if (element instanceof DraggableGroup draggableGroup)
            {
                element.resetRenderInfo();
                if (draggableGroup.restrictiveDragging)
                {
                    if (draggableGroup.overrideX < rect.x)
                        draggableGroup.overrideX = rect.x;
                    if (draggableGroup.overrideY < rect.y)
                        draggableGroup.overrideY = rect.y;
                    if (draggableGroup.overrideX + draggableGroup.rect.width > rect.x + rect.width)
                        draggableGroup.overrideX = rect.x + rect.width - draggableGroup.rect.width;
                    if (draggableGroup.overrideY + draggableGroup.rect.height > rect.y + rect.height)
                        draggableGroup.overrideY = rect.y + rect.height - draggableGroup.rect.height;
                }
                element.rect.x = draggableGroup.overrideX;
                element.rect.y = draggableGroup.overrideY;
                element.calcWidthHeight();
                element.calcRenderPos(rect);
            }
        }
    }
}
