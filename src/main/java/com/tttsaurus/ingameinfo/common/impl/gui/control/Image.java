package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.render.RenderMask;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.ImageRenderer;
import net.minecraft.util.ResourceLocation;

@RegisterElement
public class Image extends Sized
{
    private final ImageRenderer imageRenderer = new ImageRenderer();
    private final RenderMask mask = new RenderMask(RenderMask.MaskShape.ROUNDED_RECT);

    @StyleProperty
    public boolean rounded;

    @StylePropertyCallback
    public void rlValidation(String value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StylePropertyCallback
    public void setRlCallback()
    {
        imageRenderer.updateRl(new ResourceLocation(rl));
    }
    @StyleProperty(setterCallbackPost = "setRlCallback", setterCallbackPre = "rlValidation")
    public String rl;

    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        imageRenderer.setX(rect.x);
        imageRenderer.setY(rect.y);
        mask.setRoundedRectMask(rect.x, rect.y, rect.width, rect.height, themeConfig.image.cornerRadius);
    }

    @Override
    public void calcWidthHeight()
    {
        super.calcWidthHeight();
        imageRenderer.setWidth(width);
        imageRenderer.setHeight(height);
    }

    @Override
    public void onRenderUpdate(boolean focused)
    {
        if (rounded)
            mask.startMasking();
        imageRenderer.render();
        if (rounded)
            mask.endMasking();
    }
}
