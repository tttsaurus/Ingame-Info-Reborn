package com.tttsaurus.ingameinfo.common.impl.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.input.MouseUtils;

@RegisterElement
public class DraggableGroup extends DraggableContainerGroup
{
    public int overrideX;
    public int overrideY;

    @Override
    public void onRenderUpdate(boolean focused)
    {
        if (!enabled) return;
        if (focused)
        {
            if (MouseUtils.isMouseDownLeft())
            {
                overrideX = MouseUtils.getMouseX();
                overrideY = MouseUtils.getMouseY();
                requestReCalc();
            }
        }
        super.onRenderUpdate(focused);
    }
}
