package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual;

public class VisualBuilderAccessor
{
    private VisualBuilder visualBuilder;

    protected VisualBuilderAccessor() { }

    public void setVisualBuilder(VisualBuilder visualBuilder)
    {
        this.visualBuilder = visualBuilder;
    }

    public boolean getAbortRenderOp()
    {
        return visualBuilder.abortRenderOp;
    }
}
