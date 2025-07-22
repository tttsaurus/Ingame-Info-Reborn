package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.control.Sized;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.texture.Texture2D;
import com.tttsaurus.ingameinfo.common.impl.gui.render.op.UrlImageOp;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

@RegisterElement
public class UrlImage extends Sized
{
    private BufferedImage asyncImage = null;
    private Texture2D texture = null;

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
        if (url.isEmpty()) return;
        CompletableFuture.supplyAsync(() ->
        {
            try
            {
                URL imageUrl = new URL(url);
                InputStream in = imageUrl.openStream();
                return ImageIO.read(in);
            }
            catch (Exception e)
            {
                return null;
            }
        }).thenAccept(image ->
        {
            if (image != null) asyncImage = image;
        });
    }
    @StyleProperty(setterCallbackPost = "setUrlCallback", setterCallbackPre = "urlValidation")
    public String url;

    @Override
    public void onRenderUpdate(RenderOpQueue queue, boolean focused)
    {
        super.onRenderUpdate(queue, focused);

        if (asyncImage != null)
        {
            if (texture != null) texture.dispose();
            texture = RenderUtils.createTexture2D(asyncImage);
            asyncImage = null;
        }

        queue.enqueue(new UrlImageOp(rect, texture, rounded));
    }
}
