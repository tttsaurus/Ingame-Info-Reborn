package com.tttsaurus.ingameinfo.common.impl.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.gui.render.op.IRenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;

public class SeparatorOp implements IRenderOp
{
    public float x, y;
    public int color;

    public SeparatorOp(float x, float y, int color)
    {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public void readRenderContext(RenderContext context)
    {
        if (color == 0)
            color = 0; // todo: theme to be impl
    }

    @Override
    public void execute(RenderContext context)
    {
        RenderUtils.renderRect(x + 2.5f, y - 1, 1, 9, color);
    }
}
