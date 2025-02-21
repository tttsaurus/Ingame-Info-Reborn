package com.tttsaurus.ingameinfo.common.impl.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.impl.render.RenderMask;

@RegisterElement
public class SizedGroup extends ElementGroup
{
    // pivot doesn't change how the layout is calculated

    private final RenderMask mask = new RenderMask(RenderMask.MaskShape.RECT);

    @StyleProperty
    public boolean useMask = true;

    public void setIsMaskRoundedCallback()
    {
        if (isMaskRounded)
            mask.maskShape = RenderMask.MaskShape.ROUNDED_RECT;
        else
            mask.maskShape = RenderMask.MaskShape.RECT;
    }
    @StyleProperty(setterCallbackPost = "setIsMaskRoundedCallback")
    public boolean isMaskRounded = false;

    @StylePropertyCallback
    public void nonNegativeFloatValidation(float value, CallbackInfo callbackInfo)
    {
        if (value < 0) callbackInfo.cancel = true;
    }

    @StyleProperty(setterCallbackPost = "requestReCalc", setterCallbackPre = "nonNegativeFloatValidation")
    public float width;

    @StyleProperty(setterCallbackPost = "requestReCalc", setterCallbackPre = "nonNegativeFloatValidation")
    public float height;

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        if (elements.isEmpty()) return;
        for (Element element: elements)
        {
            element.rect.x = rect.x + rect.width * element.alignment.vertical;
            element.rect.y = rect.y + rect.height * element.alignment.horizontal;
            if (element.pivot.vertical == 0 || element.pivot.vertical == 0.5f) element.rect.x += element.padding.left;
            if (element.pivot.vertical == 1 || element.pivot.vertical == 0.5f) element.rect.x -= element.padding.right;
            if (element.pivot.horizontal == 0 || element.pivot.horizontal == 0.5f) element.rect.y += element.padding.top;
            if (element.pivot.horizontal == 1 || element.pivot.horizontal == 0.5f) element.rect.y -= element.padding.bottom;
            element.calcRenderPos(rect);
        }

        if (isMaskRounded)
            mask.setRoundedRectMask(rect.x, rect.y, rect.width, rect.height, 3f);
        else
            mask.setRectMask(rect.x, rect.y, rect.width, rect.height);
    }

    @Override
    public void calcWidthHeight()
    {
        super.calcWidthHeight();
        rect.width = width;
        rect.height = height;
    }

    @Override
    public void onRenderUpdate(boolean focused)
    {
        if (useMask)
            mask.startMasking();
        super.onRenderUpdate(focused);
        if (useMask)
            mask.endMasking();
    }
}
