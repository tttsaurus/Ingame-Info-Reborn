package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Alignment;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Padding;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Pivot;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import java.awt.*;

public abstract class Element
{
    public Alignment alignment = Alignment.NULL;
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
    // uses pivot to calculate the actual render pos
    public void calcRenderPos(Rect contextRect)
    {
        this.contextRect.set(contextRect.x, contextRect.y, contextRect.width, contextRect.height);

        pivotPosX = rect.x;
        rect.x -= rect.width * pivot.vertical;

        pivotPosY = rect.y;
        rect.y -= rect.height * pivot.horizontal;
    }

    // update rect.width and rect.height here
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
