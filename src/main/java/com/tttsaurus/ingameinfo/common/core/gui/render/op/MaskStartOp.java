package com.tttsaurus.ingameinfo.common.core.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.render.RenderMask;

public class MaskStartOp implements RenderOp
{
    public boolean rounded;
    public Rect rect;
    public float cornerRadius;

    public MaskStartOp(Rect rect)
    {
        this.rect = rect;
        rounded = false;
    }
    public MaskStartOp(Rect rect, float cornerRadius)
    {
        this.rect = rect;
        this.cornerRadius = cornerRadius;
        rounded = true;
    }

    @Override
    public void execute(RenderContext context)
    {
        RenderMask mask;
        if (rounded)
        {
            mask = new RenderMask(RenderMask.MaskShape.ROUNDED_RECT);
            mask.setRoundedRectMask(rect.x, rect.y, rect.width, rect.height, cornerRadius);
        }
        else
        {
            mask = new RenderMask(RenderMask.MaskShape.RECT);
            mask.setRectMask(rect.x, rect.y, rect.width, rect.height);
        }

        mask.startMasking();
    }
}
