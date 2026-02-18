package com.tttsaurus.ingameinfo.common.impl.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.gui.render.op.RenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.text.FormattedText;

public class TextOp implements RenderOp
{
    public FormattedText text;
    public float x, y, scale;
    public int color;
    public boolean shadow;

    public TextOp(FormattedText text, float x, float y, float scale, int color, boolean shadow)
    {
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.color = color;
        this.shadow = shadow;
    }

    @Override
    public void readTheme(ThemeConfig theme)
    {
        if (color == 0)
            color = theme.text.parsedColor;
    }

    @Override
    public void execute(RenderContext context)
    {
        RenderUtils.renderFormattedText(text, x, y, scale, color, shadow);
    }
}
