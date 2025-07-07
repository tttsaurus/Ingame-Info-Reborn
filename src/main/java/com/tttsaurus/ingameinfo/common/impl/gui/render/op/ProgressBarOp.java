package com.tttsaurus.ingameinfo.common.impl.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.IRenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.render.RenderMask;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;

public class ProgressBarOp implements IRenderOp
{
    public Rect rect;
    public float percentage;
    public int backgroundColor, fillerColor, outlineColor;

    public ProgressBarOp(Rect rect, float percentage, int backgroundColor, int fillerColor, int outlineColor)
    {
        this.rect = rect;
        this.percentage = percentage;
        this.backgroundColor = backgroundColor;
        this.fillerColor = fillerColor;
        this.outlineColor = outlineColor;
    }

    @Override
    public void readRenderContext(RenderContext context)
    {
        if (fillerColor == 0)
            fillerColor =  context.theme.progressBar.parsedFillerColor;
        if (backgroundColor == 0)
            backgroundColor = context.theme.progressBar.parsedBackgroundColor;
        if (outlineColor == 0)
            outlineColor = context.theme.progressBar.parsedOutlineColor;
    }

    @Override
    public void execute(RenderContext context)
    {
        RenderUtils.renderRoundedRect(rect.x, rect.y, rect.width, rect.height, rect.height / 2f, backgroundColor, context.polygonSmoothHint);
        if (percentage != 0)
        {
            RenderMask mask = new RenderMask(RenderMask.MaskShape.ROUNDED_RECT);
            mask.setRoundedRectMask(rect.x, rect.y, rect.width, rect.height, rect.height / 2f);
            mask.startMasking();
            RenderUtils.renderRect(rect.x, rect.y, rect.width * percentage, rect.height, fillerColor);
            RenderMask.endMasking();
        }
        RenderUtils.renderRoundedRectOutline(rect.x, rect.y, rect.width, rect.height, rect.height / 2f, 1.0f, outlineColor, context.lineSmoothHint);
    }
}
