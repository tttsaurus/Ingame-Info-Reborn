package com.tttsaurus.ingameinfo.common.core.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;

public interface IRenderOp
{
    default void readRenderContext(RenderContext context) { }
    void execute(RenderContext context);
}
