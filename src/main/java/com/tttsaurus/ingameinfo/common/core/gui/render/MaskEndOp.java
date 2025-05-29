package com.tttsaurus.ingameinfo.common.core.gui.render;

import com.tttsaurus.ingameinfo.common.core.render.RenderMask;

public class MaskEndOp implements IRenderOp
{
    @Override
    public void execute(RenderContext context)
    {
        RenderMask.endMasking();
    }
}
