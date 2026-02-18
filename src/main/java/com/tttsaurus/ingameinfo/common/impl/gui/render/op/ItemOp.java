package com.tttsaurus.ingameinfo.common.impl.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.gui.render.op.RenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.commonutils.GhostableItem;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;

public class ItemOp implements RenderOp
{
    public GhostableItem item;
    public float x, y, scaleX, scaleY;

    public ItemOp(GhostableItem item, float x, float y, float scaleX, float scaleY)
    {
        this.item = item;
        this.x = x;
        this.y = y;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public void execute(RenderContext context)
    {
        if (item == null) return;
        if (item.getItemStack() == null) return;

        RenderUtils.renderItem(item.getItemStack(), x, y, scaleX, scaleY);
    }
}
