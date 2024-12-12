package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Alignment;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Padding;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Pivot;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import java.awt.*;

@RegisterElement
public abstract class Element
{
    //<editor-fold desc="runtime variables">
    // stores the actual render pos (top-left) and size
    public Rect rect = new Rect(0, 0, 0, 0);
    // stores the actual render pos before applying the pivot
    public float pivotPosX = 0, pivotPosY = 0;
    // stores the rect of the parent group
    public Rect contextRect = new Rect(0, 0, 0, 0);
    //</editor-fold>

    @StyleProperty
    public Alignment alignment = Alignment.NULL;
    @StyleProperty
    public Pivot pivot = Pivot.TOP_LEFT;
    @StyleProperty
    public Padding padding = new Padding(0, 0, 0, 0);

    // determines how the background is drawn (optional)
    @StyleProperty
    public String backgroundStyle;

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
    public abstract void onRenderUpdate(boolean focused);

    public void renderBackground()
    {
        if (backgroundStyle == null) return;
        if (backgroundStyle.isEmpty()) return;

        switch (backgroundStyle)
        {
            case "box" -> RenderUtils.renderRect(rect.x, rect.y, rect.width, rect.height, 0x383838FF);
            case "outlineBox" ->
            {
                RenderUtils.renderRect(rect.x, rect.y, rect.width, rect.height, 0x383838FF);
                RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, 0x232323FF);
            }
            case "roundedBox" -> RenderUtils.renderRoundedRect(rect.x, rect.y, rect.width, rect.height, 3f, 0x383838FF);
            case "roundedOutlineBox" ->
            {
                RenderUtils.renderRoundedRect(rect.x, rect.y, rect.width, rect.height, 3f, 0x383838FF);
                RenderUtils.renderRoundedRectOutline(rect.x, rect.y, rect.width, rect.height, 3f, 1.0f, 0x232323FF);
            }
        }
    }

    public void renderDebugRect()
    {
        RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, Color.RED.getRGB());
        RenderUtils.renderRect(pivotPosX - 1, pivotPosY - 1, 3, 3, Color.GREEN.getRGB());
    }
}
