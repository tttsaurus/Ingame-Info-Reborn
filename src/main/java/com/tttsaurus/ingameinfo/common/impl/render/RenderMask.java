package com.tttsaurus.ingameinfo.common.impl.render;

import com.tttsaurus.ingameinfo.common.api.function.IAction;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import java.util.HashMap;
import java.util.Map;

public class RenderMask
{
    private static int stencilValueCounter = 0;
    private static int nextStencilValue()
    {
        stencilValueCounter++;
        if (stencilValueCounter > 255) stencilValueCounter = 1;
        return stencilValueCounter;
    }

    public enum MaskShape
    {
        RECT,
        ROUNDED_RECT,
        CUSTOM
    }
    public enum MaskMode
    {
        INCLUDE,
        EXCLUDE
    }

    public MaskShape maskShape;
    public MaskMode maskMode = MaskMode.INCLUDE;

    private final Map<MaskShape, Boolean> init = new HashMap<>();

    private int stencilValue;
    private float x;
    private float y;
    private float width;
    private float height;
    private float radius;
    private IAction drawMask;

    public RenderMask(MaskShape maskShape)
    {
        this.maskShape = maskShape;
        init.put(MaskShape.RECT, false);
        init.put(MaskShape.ROUNDED_RECT, false);
        init.put(MaskShape.CUSTOM, false);
        stencilValue = nextStencilValue();
    }
    public RenderMask(MaskShape maskShape, MaskMode maskMode)
    {
        this.maskShape = maskShape;
        this.maskMode = maskMode;
        init.put(MaskShape.RECT, false);
        init.put(MaskShape.ROUNDED_RECT, false);
        init.put(MaskShape.CUSTOM, false);
        stencilValue = nextStencilValue();
    }

    public void setRectMask(float x, float y, float width, float height)
    {
        if (maskShape != MaskShape.RECT) return;
        init.put(MaskShape.RECT, true);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void setRoundedRectMask(float x, float y, float width, float height, float radius)
    {
        if (maskShape != MaskShape.ROUNDED_RECT) return;
        init.put(MaskShape.ROUNDED_RECT, true);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.radius = radius;
    }
    public void setCustomMask(IAction drawMask)
    {
        if (maskShape != MaskShape.CUSTOM) return;
        init.put(MaskShape.CUSTOM, true);
        this.drawMask = drawMask;
    }

    public void startMasking()
    {
        switch (maskShape)
        {
            case RECT ->
            {
                if (init.get(MaskShape.RECT))
                    RenderUtils.startRectStencil(x, y, width, height, stencilValue, maskMode == MaskMode.EXCLUDE);
            }
            case ROUNDED_RECT ->
            {
                if (init.get(MaskShape.ROUNDED_RECT))
                    RenderUtils.startRoundedRectStencil(x, y, width, height, stencilValue, maskMode == MaskMode.EXCLUDE, radius);
            }
            case CUSTOM ->
            {
                if (init.get(MaskShape.CUSTOM))
                    if (drawMask != null)
                    {
                        RenderUtils.initStencilStep1(stencilValue);
                        drawMask.invoke();
                        RenderUtils.initStencilStep2(stencilValue, maskMode == MaskMode.EXCLUDE);
                    }
            }
        }
    }
    public void endMasking()
    {
        RenderUtils.endStencil();
    }
}
