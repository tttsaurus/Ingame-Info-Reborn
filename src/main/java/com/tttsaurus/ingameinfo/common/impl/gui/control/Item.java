package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.item.GhostableItem;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.ItemRenderer;

@RegisterElement
public class Item extends Sized
{
    private final ItemRenderer itemRenderer = new ItemRenderer();

    @StylePropertyCallback
    public void itemValidation(GhostableItem value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StylePropertyCallback
    public void setItemCallback()
    {
        itemRenderer.setItem(item);
    }
    @StyleProperty(setterCallbackPost = "setItemCallback", setterCallbackPre = "itemValidation")
    public GhostableItem item;

    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        itemRenderer.setX(rect.x);
        itemRenderer.setY(rect.y);
    }

    @Override
    public void calcWidthHeight()
    {
        super.calcWidthHeight();
        itemRenderer.setScaleX(width / 16f);
        itemRenderer.setScaleY(height / 16f);
    }

    @Override
    public void onRenderUpdate(boolean focused)
    {
        itemRenderer.render();
    }
}
