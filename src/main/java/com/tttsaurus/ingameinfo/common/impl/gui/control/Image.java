package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.GuiResources;
import com.tttsaurus.ingameinfo.common.core.gui.control.Sized;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.render.texture.ImagePrefab;
import com.tttsaurus.ingameinfo.common.impl.gui.render.op.ImageOp;
import net.minecraft.util.ResourceLocation;

@RegisterElement
public class Image extends Sized
{
    private ImagePrefab image = null;

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
        if (GuiResources.exists(rl))
            image = GuiResources.get(rl);
        else
            image = GuiResources.tryRegisterTexture(new ResourceLocation(rl));
    }
    @StyleProperty(setterCallbackPost = "setRlCallback", setterCallbackPre = "rlValidation")
    public String rl;

    @Override
    public void onRenderUpdate(RenderOpQueue queue, boolean focused)
    {
        super.onRenderUpdate(queue, focused);
        queue.enqueue(new ImageOp(rect, image, rounded));
    }
}
