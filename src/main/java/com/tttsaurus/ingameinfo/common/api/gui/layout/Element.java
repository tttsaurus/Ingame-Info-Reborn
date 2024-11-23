package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import java.awt.*;

public abstract class Element
{
    // we only apply the alignment pivot if this element is the outmost element
    protected Alignment alignment = Alignment.TOP_LEFT;
    // outmost represents the whole in-game screen
    protected boolean outmost;
    protected Pivot pivot = Pivot.TOP_LEFT;
    protected Padding padding = new Padding(0, 0, 0, 0);
    protected Rect rect = new Rect(0, 0, 0, 0);

    //<editor-fold desc="setters">
    public Element setAlignment(Alignment alignment) { this.alignment = alignment; return this; }
    public Element setPivot(Pivot pivot) { this.pivot = pivot; return this; }
    public Element setPadding(Padding padding) { this.padding = padding; return this; }
    //</editor-fold>

    // this requires calcWidthHeight() to be called first
    // take the pivot and do the normalization
    // also handles the outmost layout
    protected void calcRenderPos(Rect contextRect)
    {
        float x = 0;
        if (outmost)
        {
            if (pivot.vertical == 0) x = padding.left;
            if (pivot.vertical == 1) x = -padding.right;
            x += contextRect.width * alignment.vertical;
        }
        x -= rect.width * pivot.vertical;
        rect.x += x;

        float y = 0;
        if (outmost)
        {
            if (pivot.horizontal == 0) y = padding.top;
            if (pivot.horizontal == 1) y = -padding.bottom;
            y += contextRect.height * alignment.horizontal;
        }
        y -= rect.height * pivot.horizontal;
        rect.y += y;
    }

    // update rect here
    // don't touch rect.x and rect.y
    // they are handled in calcRenderPos()
    protected abstract void calcWidthHeight();

    protected abstract void onFixedUpdate(double deltaTime);
    protected abstract void onRenderUpdate();

    protected void renderDebugRect() { RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, Color.RED.getRGB()); }
}
