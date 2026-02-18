package com.tttsaurus.ingameinfo.common.impl.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.RenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.render.RenderMask;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.texture.Texture2DImpl;

public class UrlImageOp implements RenderOp
{
    public Rect rect;
    public Texture2DImpl texture;
    public boolean rounded;

    public UrlImageOp(Rect rect, Texture2DImpl texture, boolean rounded)
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
            mask.setRoundedRectMask(rect.x, rect.y, rect.width, rect.height, context.theme.urlImage.cornerRadius);
            mask.startMasking();
        }

        RenderUtils.renderTexture2D(rect.x, rect.y, rect.width, rect.height, texture.getGlTextureID());

        if (rounded)
            RenderMask.endMasking();
    }
}
