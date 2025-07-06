package com.tttsaurus.ingameinfo.common.core.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.render.RenderMask;

public class MaskEndOp implements IRenderOp
{
    @Override
    public void execute(RenderContext context)
    {
        RenderMask.endMasking();
    }
}
