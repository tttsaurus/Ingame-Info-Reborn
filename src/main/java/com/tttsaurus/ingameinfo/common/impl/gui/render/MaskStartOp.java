package com.tttsaurus.ingameinfo.common.impl.gui.render;

import com.tttsaurus.ingameinfo.common.core.gui.render.IRenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;

public class MaskStartOp implements IRenderOp
{
    public float x;
    public float y;
    public float width;
    public float height;
    public boolean rounded;
    public float cornerRadius;

    @Override
    public void execute(RenderContext context)
    {

    }
}
