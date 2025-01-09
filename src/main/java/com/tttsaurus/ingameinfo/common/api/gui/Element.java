package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Alignment;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Padding;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Pivot;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.render.RenderMask;
import java.awt.*;

@RegisterElement
public abstract class Element
{
    public static int DEFAULT_COLOR_LIGHT = Color.decode("0xd2d2d2").getRGB();
    public static int DEFAULT_COLOR_DARK = Color.decode("0x383838").getRGB();
    public static int DEFAULT_COLOR_DARKER = Color.decode("0x232323").getRGB();

    private final RenderMask mask = new RenderMask(RenderMask.MaskShape.ROUNDED_RECT);

    //<editor-fold desc="runtime variables">
    // stores the actual render pos (top-left) and size
    public Rect rect = new Rect(0, 0, 0, 0);
    // stores the actual render pos before applying the pivot
    public float pivotPosX = 0, pivotPosY = 0;
    // stores the rect of the parent group
    public Rect contextRect = new Rect(0, 0, 0, 0);
    private boolean needReCalc = false;
    //</editor-fold>

    @StylePropertyCallback
    public void requestReCalc() { needReCalc = true; }

    @StyleProperty
    public boolean enabled = true;

    @StylePropertyCallback
    public void uidValidation(String value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StyleProperty(setterCallbackPre = "uidValidation")
    public String uid = "";

    @StylePropertyCallback
    public void alignmentValidation(Alignment value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StyleProperty(setterCallbackPost = "requestReCalc", setterCallbackPre = "alignmentValidation")
    public Alignment alignment = Alignment.NULL;

    @StylePropertyCallback
    public void pivotValidation(Pivot value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StyleProperty(setterCallbackPost = "requestReCalc", setterCallbackPre = "pivotValidation")
    public Pivot pivot = Pivot.TOP_LEFT;

    @StylePropertyCallback
    public void paddingValidation(Padding value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StyleProperty(setterCallbackPost = "requestReCalc", setterCallbackPre = "paddingValidation")
    public Padding padding = new Padding(0, 0, 0, 0);

    // determines how the background is drawn (optional)
    @StylePropertyCallback
    public void backgroundStyleValidation(String value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StyleProperty(setterCallbackPre = "backgroundStyleValidation")
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

        mask.setRoundedRectMask(rect.x, rect.y, rect.width, rect.height, 3f);
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
            case "box" -> RenderUtils.renderRect(rect.x, rect.y, rect.width, rect.height, DEFAULT_COLOR_DARK);
            case "boxWithOutline" ->
            {
                RenderUtils.renderRect(rect.x, rect.y, rect.width, rect.height, DEFAULT_COLOR_DARK);
                RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, DEFAULT_COLOR_DARKER);
            }
            case "roundedBox" ->
            {
                // todo: avoid using stencil
                mask.startMasking();
                RenderUtils.renderRect(rect.x, rect.y, rect.width, rect.height, DEFAULT_COLOR_DARK);
                mask.endMasking();
            }
            case "roundedBoxWithOutline" ->
            {
                mask.startMasking();
                RenderUtils.renderRect(rect.x, rect.y, rect.width, rect.height, DEFAULT_COLOR_DARK);
                mask.endMasking();
                RenderUtils.renderRoundedRectOutline(rect.x, rect.y, rect.width, rect.height, 3f, 1.0f, DEFAULT_COLOR_DARKER);
            }
        }
    }

    public boolean getNeedReCalc() { return needReCalc; }
    public void finishReCalc() { needReCalc = false; }

    public void renderDebugRect()
    {
        RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, Color.RED.getRGB());
        RenderUtils.renderRect(pivotPosX - 1, pivotPosY - 1, 3, 3, Color.GREEN.getRGB());
    }
}
