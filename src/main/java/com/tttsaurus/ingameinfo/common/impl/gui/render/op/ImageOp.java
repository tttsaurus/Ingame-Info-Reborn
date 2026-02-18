package com.tttsaurus.ingameinfo.common.impl.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.RenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.render.RenderMask;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.texture.ImagePrefab;

public class ImageOp implements RenderOp
{
    public Rect rect;
    public ImagePrefab image;
    public boolean rounded;

    public ImageOp(Rect rect, ImagePrefab image, boolean rounded)
    {
        this.rect = rect;
        this.image = image;
        this.rounded = rounded;
    }

    @Override
    public void execute(RenderContext context)
    {
        if (image == null) return;

        if (rounded)
        {
            RenderMask mask = new RenderMask(RenderMask.MaskShape.ROUNDED_RECT);
            mask.setRoundedRectMask(rect.x, rect.y, rect.width, rect.height, context.theme.image.cornerRadius);
            mask.startMasking();
        }

        RenderUtils.renderImagePrefab(rect.x, rect.y, rect.width, rect.height, image);

        if (rounded)
            RenderMask.endMasking();
    }
}
