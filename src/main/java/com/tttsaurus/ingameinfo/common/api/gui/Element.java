package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Alignment;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Padding;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Pivot;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import java.awt.*;

public abstract class Element
{
    // we only apply the alignment pivot if this element is the outmost element
    public Alignment alignment = Alignment.TOP_LEFT;
    // outmost represents the whole in-game screen
    public boolean outmost;
    public Pivot pivot = Pivot.TOP_LEFT;
    public Padding padding = new Padding(0, 0, 0, 0);

    // stores the actual render pos (top-left) and size
    public Rect rect = new Rect(0, 0, 0, 0);
    // stores the actual render pos before applying the pivot
    public float pivotPosX = 0, pivotPosY = 0;
    // stores the rect of the parent group
    public Rect contextRect = new Rect(0, 0, 0, 0);

    //<editor-fold desc="setters">
    public Element setAlignment(Alignment alignment) { this.alignment = alignment; return this; }
    public Element setPivot(Pivot pivot) { this.pivot = pivot; return this; }
    public Element setPadding(Padding padding) { this.padding = padding; return this; }
    //</editor-fold>

    public void resetRenderInfo()
    {
        rect.set(0, 0, 0, 0);
        contextRect.set(0, 0, 0, 0);
        pivotPosX = 0;
        pivotPosY = 0;
    }

    // this requires calcWidthHeight() to be called first
    // uses pivot and alignment to calculate the actual render pos
    public void calcRenderPos(Rect contextRect)
    {
        this.contextRect.set(contextRect.x, contextRect.y, contextRect.width, contextRect.height);

        float x = 0;
        if (outmost)
        {
            if (pivot.vertical == 0) x = padding.left;
            if (pivot.vertical == 1) x = -padding.right;
            x += contextRect.width * alignment.vertical;
        }
        float pivotOffsetX = rect.width * pivot.vertical;
        rect.x += x - pivotOffsetX;
        pivotPosX = rect.x + pivotOffsetX;

        float y = 0;
        if (outmost)
        {
            if (pivot.horizontal == 0) y = padding.top;
            if (pivot.horizontal == 1) y = -padding.bottom;
            y += contextRect.height * alignment.horizontal;
        }
        float pivotOffsetY = rect.height * pivot.horizontal;
        rect.y += y - pivotOffsetY;
        pivotPosY = rect.y + pivotOffsetY;
    }

    // update rect here
    // don't touch rect.x and rect.y
    // they are handled in calcRenderPos()
    public abstract void calcWidthHeight();

    public abstract void onFixedUpdate(double deltaTime);
    public abstract void onRenderUpdate();

    public void renderDebugRect()
    {
        RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, Color.RED.getRGB());
        RenderUtils.renderRect(pivotPosX - 1, pivotPosY - 1, 3, 3, Color.GREEN.getRGB());
    }
}
