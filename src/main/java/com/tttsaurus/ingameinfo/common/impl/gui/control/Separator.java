package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.impl.gui.render.op.SeparatorOp;

@RegisterElement
public class Separator extends Element
{
    @StyleProperty
    public int color;

    @Override
    public void calcWidthHeight()
    {
        rect.width = 6;
        rect.height = 9;
    }

    @Override
    public void onRenderUpdate(RenderOpQueue queue, boolean focused)
    {
        super.onRenderUpdate(queue, focused);
        queue.enqueue(new SeparatorOp(rect.x, rect.y, color));
    }
}
