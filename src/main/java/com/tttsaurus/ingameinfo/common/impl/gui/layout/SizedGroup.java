package com.tttsaurus.ingameinfo.common.impl.gui.layout;

import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.MaskEndOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.MaskStartOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;

@RegisterElement
public class SizedGroup extends ElementGroup
{
    // pivot doesn't change how the layout is calculated

    @StyleProperty
    public boolean useMask = true;

    @StyleProperty
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
    }

    @Override
    public void calcWidthHeight()
    {
        super.calcWidthHeight();
        rect.width = width;
        rect.height = height;
    }

    @Override
    public void onRenderUpdate(RenderOpQueue queue, boolean focused)
    {
        if (!enabled) return;

        if (useMask)
        {
            if (isMaskRounded)
                queue.enqueue(new MaskStartOp(rect, 3f));
            else
                queue.enqueue(new MaskStartOp(rect));
        }

        super.onRenderUpdate(queue, focused);

        if (useMask)
            queue.enqueue(new MaskEndOp());
    }
}
