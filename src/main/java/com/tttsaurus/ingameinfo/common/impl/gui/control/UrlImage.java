package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.UrlImageRenderer;

@RegisterElement
public class UrlImage extends Sized
{
    private final UrlImageRenderer urlImageRenderer = new UrlImageRenderer();

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
        urlImageRenderer.render();
    }
}
