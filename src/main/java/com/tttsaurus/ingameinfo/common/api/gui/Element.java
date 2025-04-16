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
import com.tttsaurus.ingameinfo.common.api.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.api.render.RenderHints;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.gui.GuiResources;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
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

    // stores width & height when `enabled` is false
    public float cachedWidth = 0f, cachedHeight = 0f;

    // stores padding when `enabled` is false
    public Padding cachedPadding = new Padding(0f, 0f, 0f, 0f);

    // stores the actual render pos (top-left) and size
    public Rect rect = new Rect(0f, 0f, 0f, 0f);

    // stores the actual render pos before applying the pivot
    public float pivotPosX = 0f, pivotPosY = 0f;

    // stores the rect of the parent group
    public Rect contextRect = new Rect(0f, 0f, 0f, 0f);

    private boolean needReCalc = false;

    // vvm binding will inject
    @SuppressWarnings("all")
    private Map<String, IStylePropertySyncTo> syncToMap = new HashMap<>();

    // theme reference
    @SuppressWarnings("all")
    private ThemeConfig themeConfig = null;
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
    public void paddingValidation(Padding value, CallbackInfo callbackInfo)
    {
        if (value == null) callbackInfo.cancel = true;
    }
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
    public String backgroundStyle = "";

    @StyleProperty
    public boolean drawBackground = false;
    //</editor-fold>

    // sync to view model, not sync from
    public final void sync(String stylePropertyName)
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
        if (backgroundStyle.isEmpty()) return;
        if (!drawBackground) return;

        switch (backgroundStyle)
        {
            case "box" -> RenderUtils.renderRect(rect.x, rect.y, rect.width, rect.height, themeConfig.backgroundStyles.box.parsedColor);
            case "box-with-outline" ->
            {
                RenderUtils.renderRect(rect.x, rect.y, rect.width, rect.height, themeConfig.backgroundStyles.boxWithOutline.parsedColor);
                RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, themeConfig.backgroundStyles.boxWithOutline.parsedOutlineColor);
            }
            case "rounded-box" -> RenderUtils.renderRoundedRect(rect.x, rect.y, rect.width, rect.height, themeConfig.backgroundStyles.roundedBox.cornerRadius, themeConfig.backgroundStyles.roundedBox.parsedColor);
            case "rounded-box-with-outline" ->
            {
                RenderUtils.renderRoundedRect(rect.x, rect.y, rect.width, rect.height, themeConfig.backgroundStyles.roundedBoxWithOutline.cornerRadius, themeConfig.backgroundStyles.roundedBoxWithOutline.parsedColor);
                RenderUtils.renderRoundedRectOutline(rect.x, rect.y, rect.width, rect.height, themeConfig.backgroundStyles.roundedBoxWithOutline.cornerRadius, 1.0f, themeConfig.backgroundStyles.roundedBoxWithOutline.parsedOutlineColor);
            }
            case "mc-vanilla" ->
            {
                float ppu = RenderHints.getHint_pixelPerUnit();

                float width1 = GuiResources.mcVanillaBgTopLeft.getWidth() / ppu;
                float height1 = GuiResources.mcVanillaBgTopLeft.getHeight() / ppu;
                RenderUtils.renderTexture2D(rect.x, rect.y, width1, height1, GuiResources.mcVanillaBgTopLeft);

                float width2 = GuiResources.mcVanillaBgTopRight.getWidth() / ppu;
                float height2 = GuiResources.mcVanillaBgTopRight.getHeight() / ppu;
                RenderUtils.renderTexture2D(rect.x + rect.width - width2, rect.y, width2, height2, GuiResources.mcVanillaBgTopRight);

                if (rect.width - width1 - width2 > 0)
                {
                    float height3 = GuiResources.mcVanillaBgTopCenter.getHeight() / ppu;
                    RenderUtils.renderTexture2D(rect.x + width1, rect.y, rect.width - width1 - width2, height3, GuiResources.mcVanillaBgTopCenter);
                }

                float width4 = GuiResources.mcVanillaBgBottomLeft.getWidth() / ppu;
                float height4 = GuiResources.mcVanillaBgBottomLeft.getHeight() / ppu;
                RenderUtils.renderTexture2D(rect.x, rect.y + rect.height - height4, width4, height4, GuiResources.mcVanillaBgBottomLeft);

                float width5 = GuiResources.mcVanillaBgBottomRight.getWidth() / ppu;
                float height5 = GuiResources.mcVanillaBgBottomRight.getHeight() / ppu;
                RenderUtils.renderTexture2D(rect.x + rect.width - width5, rect.y + rect.height - height5, width5, height5, GuiResources.mcVanillaBgBottomRight);

                if (rect.width - width4 - width5 > 0)
                {
                    float height6 = GuiResources.mcVanillaBgBottomCenter.getHeight() / ppu;
                    RenderUtils.renderTexture2D(rect.x + width4, rect.y + rect.height - height6, rect.width - width4 - width5, height6, GuiResources.mcVanillaBgBottomCenter);
                }

                if (rect.height - height1 - height4 > 0)
                {
                    float width7 = GuiResources.mcVanillaBgCenterLeft.getWidth() / ppu;
                    RenderUtils.renderTexture2D(rect.x, rect.y + height1, width7, rect.height - height1 - height4, GuiResources.mcVanillaBgCenterLeft);
                }

                if (rect.height - height2 - height5 > 0)
                {
                    float width8 = GuiResources.mcVanillaBgCenterRight.getWidth() / ppu;
                    RenderUtils.renderTexture2D(rect.x + rect.width - width8, rect.y + height2, width8, rect.height - height2 - height5, GuiResources.mcVanillaBgCenterRight);
                }

                if (rect.width - width1 - width2 > 0 && rect.height - height1 - height4 > 0)
                {
                    RenderUtils.renderTexture2D(rect.x + width1, rect.y + height1, rect.width - width1 - width2, rect.height - height1 - height4, GuiResources.mcVanillaBgCenter);
                }
            }
        }
    }

    public boolean getNeedReCalc() { return needReCalc; }
    public void finishReCalc() { needReCalc = false; }

    public void loadTheme(ThemeConfig themeConfig)
    {
        this.themeConfig = themeConfig;
        if (this instanceof MainGroup) return;

        if (backgroundStyle.isEmpty())
        {
            backgroundStyle = themeConfig.element.backgroundStyle;
            sync("backgroundStyle");
        }
    }

    public void renderDebugRect()
    {
        RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, (new Color(241, 58, 30, 128)).getRGB());
        RenderUtils.renderRect(pivotPosX - 1, pivotPosY - 1, 3, 3, Color.GREEN.getRGB());
    }
}
