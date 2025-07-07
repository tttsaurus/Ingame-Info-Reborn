package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual;

public class VisualBuilder
{
    protected boolean abortRenderOp = false;

    public VisualBuilder abortRenderOp()
    {
        abortRenderOp = true;
        return this;
    }
}
