package com.tttsaurus.ingameinfo.common.impl.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.gui.property.lerp.LerpableProperty;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.IRenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.text.FormattedText;

public class SlidingTextOp implements IRenderOp
{
    public FormattedText text;
    public float x, y, scale;
    public LerpableProperty<Float> xShift = null;
    public boolean xShiftSign; // plus or minus
    public int color;
    public boolean shadow;

    public SlidingTextOp(FormattedText text, float x, float y, LerpableProperty<Float> xShift, boolean xShiftSign, float scale, int color, boolean shadow)
    {
        this.text = text;
        this.x = x;
        this.y = y;
        this.xShift = xShift;
        this.xShiftSign = xShiftSign;
        this.scale = scale;
        this.color = color;
        this.shadow = shadow;
    }
    public SlidingTextOp(FormattedText text, float x, float y, float scale, int color, boolean shadow)
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
            color = theme.slidingText.parsedColor;
    }

    @Override
    public void execute(RenderContext context)
    {
        float finalX = x;
        if (xShift != null)
        {
            if (xShiftSign)
                finalX += xShift.lerp(context.lerpAlpha);
            else
                finalX -= xShift.lerp(context.lerpAlpha);
        }

        RenderUtils.renderFormattedText(text, finalX, y, scale, color, shadow);
    }
}
