package com.tttsaurus.ingameinfo.common.impl.gui.render;

import com.tttsaurus.ingameinfo.common.core.gui.render.IRenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;

public class TextOp implements IRenderOp
{
    public String text;
    public float x, y, scale;
    public int color;
    public boolean shadow;

    public TextOp(String text, float x, float y, float scale, int color, boolean shadow)
    {
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.color = color;
        this.shadow = shadow;
    }

    @Override
    public void execute(RenderContext context)
    {
        if (color == 0)
            color = context.theme.text.parsedColor;

        RenderUtils.renderText(text, x, y, scale, color, shadow);
    }
}
