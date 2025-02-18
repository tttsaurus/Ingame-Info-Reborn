package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.ImageRenderer;

@RegisterElement
public class Image extends Sized
{
    private final ImageRenderer imageRenderer = new ImageRenderer();

    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }

    @Override
    public void onRenderUpdate(boolean focused)
    {

    }
}
