package com.tttsaurus.ingameinfo.common.core.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import java.awt.*;

public class DebugRectOp implements IRenderOp
{
    public boolean isGroup;
    public Rect rect;
    public float pivotX, pivotY;

    public DebugRectOp(boolean isGroup, Rect rect, float pivotX, float pivotY)
    {
        this.isGroup = isGroup;
        this.rect = rect;
        this.pivotX = pivotX;
        this.pivotY = pivotY;
    }

    @Override
    public void execute(RenderContext context)
    {
        if (isGroup)
        {
            RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, (new Color(255, 194, 38, 77)).getRGB());
            RenderUtils.renderRect(pivotX - 1, pivotY - 1, 3, 3, Color.GREEN.getRGB());
        }
        else
        {
            RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, (new Color(241, 58, 30, 128)).getRGB());
            RenderUtils.renderRect(pivotX - 1, pivotY - 1, 3, 3, Color.GREEN.getRGB());
        }
    }
}
