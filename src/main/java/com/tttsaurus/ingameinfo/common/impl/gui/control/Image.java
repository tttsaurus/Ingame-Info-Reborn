package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.control.Sized;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.Texture2D;
import com.tttsaurus.ingameinfo.common.impl.gui.render.op.ImageOp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@RegisterElement
public class Image extends Sized
{
    private Texture2D texture = null;

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
        ResourceLocation resourceLocation = new ResourceLocation(rl);
        if (texture != null) texture.dispose();
        try
        {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation);
            InputStream inputStream = resource.getInputStream();

            BufferedImage bufferedImage = ImageIO.read(inputStream);

            texture = RenderUtils.createTexture2D(bufferedImage);
        }
        catch (IOException ignored) { }
    }
    @StyleProperty(setterCallbackPost = "setRlCallback", setterCallbackPre = "rlValidation")
    public String rl;

    @Override
    public void onRenderUpdate(RenderOpQueue queue, boolean focused)
    {
        super.onRenderUpdate(queue, focused);
        queue.enqueue(new ImageOp(rect, texture, rounded));
    }
}
