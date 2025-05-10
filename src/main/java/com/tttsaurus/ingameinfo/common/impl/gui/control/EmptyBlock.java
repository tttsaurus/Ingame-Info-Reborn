package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;

@RegisterElement
public class EmptyBlock extends Sized
{
    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }

    @Override
    public void onRenderUpdate(boolean focused)
    {
        super.onRenderUpdate(focused);
    }
}
