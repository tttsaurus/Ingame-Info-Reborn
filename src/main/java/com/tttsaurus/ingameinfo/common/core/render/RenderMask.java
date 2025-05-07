package com.tttsaurus.ingameinfo.common.core.render;

import com.tttsaurus.ingameinfo.common.core.function.IAction;
import org.lwjgl.opengl.GL11;
import java.util.*;

public final class RenderMask
{
    private static final Stack<RenderMask> maskStack = new Stack<>();
    private static int stencilValueCounter = 0;
    private static int nextStencilValue()
    {
        stencilValueCounter++;
        if (stencilValueCounter > 254)
        {
            stencilValueCounter = 1;
            GL11.glClearStencil(0);
            GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        }
        return stencilValueCounter;
    }

    public enum MaskShape
    {
        RECT,
        ROUNDED_RECT,
        CUSTOM
    }

    public MaskShape maskShape;
    private final Map<MaskShape, Boolean> init = new HashMap<>();

    private final int stencilValue;
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

    public void setRectMask(float x, float y, float width, float height)
    {
        init.put(MaskShape.RECT, true);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void setRoundedRectMask(float x, float y, float width, float height, float radius)
    {
        init.put(MaskShape.ROUNDED_RECT, true);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.radius = radius;
    }
    public void setCustomMask(IAction drawMask)
    {
        init.put(MaskShape.CUSTOM, true);
        this.drawMask = drawMask;
    }

    private static void drawStencilArea(RenderMask mask)
    {
        switch (mask.maskShape)
        {
            case RECT ->
            {
                if (mask.init.get(MaskShape.RECT))
                    RenderUtils.drawRectStencilArea(mask.x, mask.y, mask.width, mask.height);
            }
            case ROUNDED_RECT ->
            {
                if (mask.init.get(MaskShape.ROUNDED_RECT))
                    RenderUtils.drawRoundedRectStencilArea(mask.x, mask.y, mask.width, mask.height, mask.radius);
            }
            case CUSTOM ->
            {
                if (mask.init.get(MaskShape.CUSTOM))
                    if (mask.drawMask != null)
                        mask.drawMask.invoke();
            }
        }
    }

    public void startMasking()
    {
        if (maskStack.isEmpty())
            maskStack.push(this);
        else if (maskStack.peek() != this)
            maskStack.push(this);

        RenderUtils.prepareStencilToWrite(stencilValue);
        drawStencilArea(this);

        if (maskStack.size() > 1)
        {
            ListIterator<RenderMask> iterator = maskStack.listIterator(maskStack.size());
            iterator.previous();
            while (iterator.hasPrevious())
            {
                RenderMask prevMask = iterator.previous();
                RenderUtils.prepareStencilToIncrease(stencilValue);
                drawStencilArea(prevMask);
                RenderUtils.prepareStencilToZero(stencilValue);
                drawStencilArea(this);
                RenderUtils.prepareStencilToDecrease(stencilValue + 1);
                drawStencilArea(this);
            }
        }

        RenderUtils.prepareStencilToRender(stencilValue);
    }
    public void endMasking()
    {
        RenderUtils.endStencil();
        maskStack.pop();
        if (!maskStack.isEmpty())
            maskStack.peek().startMasking();
    }
}
