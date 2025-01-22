package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Alignment;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Padding;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Pivot;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertySyncTo;
import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@RegisterElement(constructable = false)
public abstract class Element
{
    public static int DEFAULT_COLOR_LIGHT = Color.decode("0xd2d2d2").getRGB();
    public static int DEFAULT_COLOR_DARK = Color.decode("0x383838").getRGB();
    public static int DEFAULT_COLOR_DARKER = Color.decode("0x232323").getRGB();

    //<editor-fold desc="runtime variables">

    // stores width & height when enabled is false
    public float cachedWidth = 0f, cachedHeight = 0f;

    // stores padding when enabled is false
    public Padding cachedPadding = new Padding(0f, 0f, 0f, 0f);

    // stores the actual render pos (top-left) and size
    public Rect rect = new Rect(0f, 0f, 0f, 0f);

    // stores the actual render pos before applying the pivot
    public float pivotPosX = 0f, pivotPosY = 0f;

    // stores the rect of the parent group
    public Rect contextRect = new Rect(0f, 0f, 0f, 0f);

    private boolean needReCalc = false;

    private Map<String, IStylePropertySyncTo> syncToMap = new HashMap<>();
    //</editor-fold>

    //<editor-fold desc="style properties">
    @StylePropertyCallback
    public void requestReCalc() { needReCalc = true; }

    @StylePropertyCallback
    public void setEnabledCallbackPre(boolean value, CallbackInfo callbackInfo)
    {
        if (!enabled && value)
        {
            rect.width = cachedWidth;
            rect.height = cachedHeight;
            padding.set(cachedPadding.top, cachedPadding.bottom, cachedPadding.left, cachedPadding.right);
            requestReCalc();
        }
        if (enabled && !value)
        {
            cachedWidth = rect.width;
            cachedHeight = rect.height;
            rect.width = 0f;
            rect.height = 0f;
            cachedPadding.set(padding.top, padding.bottom, padding.left, padding.right);
            padding.set(0f, 0f, 0f, 0f);
            requestReCalc();
        }
    }
    @StyleProperty(setterCallbackPre = "setEnabledCallbackPre")
    public boolean enabled = true;

    @StylePropertyCallback
    public void uidValidation(String value, CallbackInfo callbackInfo)
    {
        if (value == null)
        {
            callbackInfo.cancel = true;
            return;
        }
        if (value.isEmpty()) callbackInfo.cancel = true;
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
    public void setPaddingCallbackPre(Padding value, CallbackInfo callbackInfo)
    {
        if (value == null)
        {
            callbackInfo.cancel = true;
            return;
        }
        if (!enabled)
        {
            cachedPadding.set(value.top, value.bottom, value.left, value.right);
            callbackInfo.cancel = true;
        }
    }
    @StyleProperty(setterCallbackPost = "requestReCalc", setterCallbackPre = "setPaddingCallbackPre")
    public Padding padding = new Padding(0f, 0f, 0f, 0f);

    // determines how the background is drawn (optional)
    @StylePropertyCallback
    public void backgroundStyleValidation(String value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
    @StyleProperty(setterCallbackPre = "backgroundStyleValidation")
    public String backgroundStyle;
    //</editor-fold>

    // sync to not sync from
    public final void syncTo(String stylePropertyName)
    {
        IStylePropertySyncTo sync = syncToMap.get(stylePropertyName);
        if (sync != null) sync.sync();
    }

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
            case "box" -> RenderUtils.renderRect(rect.x, rect.y, rect.width, rect.height, DEFAULT_COLOR_DARK);
            case "boxWithOutline" ->
            {
                RenderUtils.renderRect(rect.x, rect.y, rect.width, rect.height, DEFAULT_COLOR_DARK);
                RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, DEFAULT_COLOR_DARKER);
            }
            case "roundedBox" -> RenderUtils.renderRoundedRect(rect.x, rect.y, rect.width, rect.height, 3f, DEFAULT_COLOR_DARK);
            case "roundedBoxWithOutline" ->
            {
                RenderUtils.renderRoundedRect(rect.x, rect.y, rect.width, rect.height, 3f, DEFAULT_COLOR_DARK);
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
