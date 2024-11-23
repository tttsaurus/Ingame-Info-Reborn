package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import java.awt.*;

public abstract class Element
{
    // we only apply the alignment pivot if this element is the outmost element
    protected Pivot alignmentPivot = Pivot.TOP_LEFT;
    // outmost represents the whole in-game screen
    protected boolean outmost;
    protected Pivot selfPivot = Pivot.TOP_LEFT;
    protected Padding padding = new Padding(0, 0, 0, 0);
    protected Rect rect = new Rect(0, 0, 0, 0);

    //<editor-fold desc="setters">
    public Element setAlignmentPivot(Pivot alignmentPivot) { this.alignmentPivot = alignmentPivot; return this; }
    public Element setSelfPivot(Pivot selfPivot) { this.selfPivot = selfPivot; return this; }
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
            if (selfPivot.vertical == 0) x = padding.left;
            if (selfPivot.vertical == 1) x = -padding.right;
            x += contextRect.width * alignmentPivot.vertical;
        }
        x -= rect.width * selfPivot.vertical;
        rect.x += x;

        float y = 0;
        if (outmost)
        {
            if (selfPivot.horizontal == 0) y = padding.top;
            if (selfPivot.horizontal == 1) y = -padding.bottom;
            y += contextRect.height * alignmentPivot.horizontal;
        }
        y -= rect.height * selfPivot.horizontal;
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
