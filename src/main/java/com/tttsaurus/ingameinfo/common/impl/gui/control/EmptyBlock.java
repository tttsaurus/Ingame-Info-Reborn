package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;

@RegisterElement
public class EmptyBlock extends Sized
{
    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }

    @Override
    public void onRenderUpdate(RenderOpQueue queue, boolean focused)
    {
        super.onRenderUpdate(queue, focused);
    }
}
