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
                element.rect.x = draggableGroup.overrideX;
                element.rect.y = draggableGroup.overrideY;
                element.calcWidthHeight();
                element.calcRenderPos(rect);
            }
        }
    }
}
