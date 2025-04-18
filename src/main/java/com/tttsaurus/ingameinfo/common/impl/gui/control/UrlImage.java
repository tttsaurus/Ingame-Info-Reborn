package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.api.render.RenderMask;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.UrlImageRenderer;

@RegisterElement
public class UrlImage extends Sized
{
    private final UrlImageRenderer urlImageRenderer = new UrlImageRenderer();
    private final RenderMask mask = new RenderMask(RenderMask.MaskShape.ROUNDED_RECT);

    @StyleProperty
    public boolean rounded;

    @StylePropertyCallback
    public void urlValidation(String value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StylePropertyCallback
    public void setUrlCallback()
    {
        urlImageRenderer.updateUrlAsync(url);
    }
    @StyleProperty(setterCallbackPost = "setUrlCallback", setterCallbackPre = "urlValidation")
    public String url;

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        urlImageRenderer.setX(rect.x);
        urlImageRenderer.setY(rect.y);
        mask.setRoundedRectMask(rect.x, rect.y, rect.width, rect.height, 3f);
    }

    @Override
    public void calcWidthHeight()
    {
        super.calcWidthHeight();
        urlImageRenderer.setWidth(width);
        urlImageRenderer.setHeight(height);
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }

    @Override
    public void onRenderUpdate(boolean focused)
    {
        if (rounded)
            mask.startMasking();
        urlImageRenderer.render();
        if (rounded)
            mask.endMasking();
    }
}
