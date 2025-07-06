package com.tttsaurus.ingameinfo.common.impl.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.IRenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.render.RenderMask;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.Texture2D;

public class ImageOp implements IRenderOp
{
    public Rect rect;
    public Texture2D texture;
    public boolean rounded;

    public ImageOp(Rect rect, Texture2D texture, boolean rounded)
    {
        this.rect = rect;
        this.texture = texture;
        this.rounded = rounded;
    }

    @Override
    public void execute(RenderContext context)
    {
        if (texture == null) return;
        if (!texture.isGlRegistered()) return;

        if (rounded)
        {
            RenderMask mask = new RenderMask(RenderMask.MaskShape.ROUNDED_RECT);
            mask.setRoundedRectMask(rect.x, rect.y, rect.width, rect.height, context.theme.image.cornerRadius);
            mask.startMasking();
        }

        RenderUtils.renderTexture2D(rect.x, rect.y, rect.width, rect.height, texture.getGlTextureID());

        if (rounded)
            RenderMask.endMasking();
    }
}
