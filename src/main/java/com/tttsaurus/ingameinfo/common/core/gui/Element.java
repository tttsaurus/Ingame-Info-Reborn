package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Alignment;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Padding;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Pivot;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.property.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.property.IStylePropertySyncTo;
import com.tttsaurus.ingameinfo.common.core.gui.property.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.gui.property.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.render.BackgroundOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.DebugRectOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.core.gui.registry.ElementRegistry;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.VvmBinding;
import com.tttsaurus.ingameinfo.common.core.gui.render.IRenderOp;
import java.util.HashMap;
import java.util.Map;

@RegisterElement(constructable = false)
public abstract class Element
{
    //<editor-fold desc="runtime variables">
    /**
     * <code>cachedWidth</code> stores the width of this element when <code>enabled</code> is set to false.
     */
    public float cachedWidth = 0f;

    /**
     * <code>cachedHeight</code> stores the height of this element when <code>enabled</code> is set to false.
     */
    public float cachedHeight = 0f;

    /**
     * <code>cachedPadding</code> stores the padding of this element when <code>enabled</code> is set to false.
     */
    public Padding cachedPadding = new Padding(0f, 0f, 0f, 0f);

    /**
     * <code>rect</code> stores the actual render position (upper-left corner), <code>x</code> <code>y</code>, and <code>width</code> <code>height</code> of this element.
     * They are under Minecraft's scaled resolution coordinate system.
     */
    public Rect rect = new Rect(0f, 0f, 0f, 0f);

    /**
     * <code>pivotPosX</code> stores the pivot position x of this element.
     * Pivot position will be used to calculate the actual render position (upper-left corner) during {@link Element#calcRenderPos(Rect)}.
     */
    public float pivotPosX = 0f;

    /**
     * <code>pivotPosY</code> stores the pivot position y of this element.
     * Pivot position will be used to calculate the actual render position (upper-left corner) during {@link Element#calcRenderPos(Rect)}.
     */
    public float pivotPosY = 0f;

    /**
     * <code>contextRect</code> stores the position and size of its parent, which is a {@link com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup}.
     */
    public Rect contextRect = new Rect(0f, 0f, 0f, 0f);

    /**
     * This is the indicator of whether the layout of this element needs to be calculated again.
     * Set <code>needReCalc</code> by {@link Element#requestReCalc()}.
     */
    private boolean needReCalc = false;

    /**
     * <code>syncToMap</code> stores callbacks of setting style properties of this element.
     * Those callbacks sync the style properties from <code>View</code> (which is this element) to <code>ViewModel</code>.
     * Those callbacks will be injected by {@link VvmBinding#bindReactiveObject(Reactive, ReactiveObject)}.
     * {@link Element#setStyleProperty(String, Object)} mostly relies on <code>syncToMap</code>, and <code>syncToMap</code> will be ready at an early stage.
     * As a result, you can safely call {@link Element#setStyleProperty(String, Object)} to set style properties with syncing on.
     *
     * @see Element#setStyleProperty(String, Object)
     */
    @SuppressWarnings("all")
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
    public String backgroundStyle = "";

    @StyleProperty
    public boolean drawBackground = false;
    //</editor-fold>

    /**
     * The most safe way to set style properties since this method guarantees the
     * callbacks and syncing between <code>View</code> and <code>ViewModel</code> to be handled correctly.
     *
     * @param propertyName The name of the style property.
     * @param value The value to be set.
     */
    public final void setStyleProperty(String propertyName, Object value)
    {
        IAction_1Param<Object> action = ElementRegistry.getStylePropertySetterFullCallback(this, propertyName);
        if (action != null)
        {
            action.invoke(value);
            IStylePropertySyncTo sync = syncToMap.get(propertyName);
            if (sync != null) sync.sync();
        }
    }

    /**
     * Reset {@link Element#rect}, {@link Element#contextRect}, {@link Element#pivotPosX} and {@link Element#pivotPosY}.
     * This method will be called before re-calculate the layout.
     */
    public void resetRenderInfo()
    {
        rect.set(0, 0, 0, 0);
        contextRect.set(0, 0, 0, 0);
        pivotPosX = 0;
        pivotPosY = 0;
    }

    /**
     * {@link Element#calcWidthHeight()} will be called before calling this method, so
     * <code>width</code> and <code>height</code> from {@link Element#rect} will be ready during this method.
     * This method by itself uses pivot position to calculate the actual render position, which are <code>x</code> and <code>y</code> from {@link Element#rect}.
     * You should override this method if you want to further manipulate the render position.
     *
     * @see Element#calcWidthHeight()
     * @param contextRect The position and size of its parent.
     */
    public void calcRenderPos(Rect contextRect)
    {
        this.contextRect.set(contextRect.x, contextRect.y, contextRect.width, contextRect.height);

        pivotPosX = rect.x;
        rect.x -= rect.width * pivot.vertical;

        pivotPosY = rect.y;
        rect.y -= rect.height * pivot.horizontal;
    }

    /**
     * You should set <code>width</code> and <code>height</code> from {@link Element#rect} in this method.
     * Must not touch <code>x</code> and <code>y</code> from {@link Element#rect} during this method.
     * 
     * @see Element#calcRenderPos(Rect)
     */
    public abstract void calcWidthHeight();

    /**
     * Override to handle animation calculation and stuff here.
     *
     * @see IgiGuiContainer#onFixedUpdate(double)
     * @param deltaTime The time between the last fixed update and this one.
     */
    public void onFixedUpdate(double deltaTime)
    {

    }

    /**
     * Override to enqueue {@link IRenderOp} to {@link RenderOpQueue}.
     * All rendering logic are handled in {@link IRenderOp}.
     * Must not render stuff explicitly in this method.
     *
     * @see IRenderOp
     * @see IgiGuiContainer#onRenderUpdate(boolean)
     * @param queue The queue that stores all the render commands of this {@link IgiGuiContainer}.
     * @param focused Whether this {@link IgiGuiContainer} is focused and stack at the top.
     */
    public void onRenderUpdate(RenderOpQueue queue, boolean focused)
    {
        if (drawBackground)
            queue.enqueue(new BackgroundOp(backgroundStyle, rect));
    }

    /**
     * Whether the layout of this element needs to be re-calculated.
     *
     * @return {@link Element#needReCalc}
     */
    public boolean getNeedReCalc() { return needReCalc; }

    /**
     * Set {@link Element#needReCalc} to <code>true</code>.
     */
    public void finishReCalc() { needReCalc = false; }

    /**
     * {@link ThemeConfig} should be split into Logic Theme and Visual Theme
     * where Visual Theme is handled during {@link Element#onRenderUpdate(RenderOpQueue, boolean)} inside {@link IRenderOp}.
     * Logic Theme affects how the layout is being calculated and should be applied at an early stage which is this method.
     * Only override this method to apply Logic Theme (probably using {@link Element#setStyleProperty(String, Object)}.)
     *
     * @see Element#setStyleProperty(String, Object)
     * @param themeConfig The GUI theme.
     */
    public void applyLogicTheme(ThemeConfig themeConfig)
    {
        if (backgroundStyle.isEmpty())
            setStyleProperty("backgroundStyle", themeConfig.element.backgroundStyle);
    }

    /**
     * Similar to {@link Element#onRenderUpdate(RenderOpQueue, boolean)}, but
     * this method will only be called when {@link IgiGuiContainer#debug} equals <code>true</code>.
     *
     * @see Element#onRenderUpdate(RenderOpQueue, boolean)
     * @see IgiGuiContainer#debug
     * @param queue The queue that stores all the render commands of this {@link IgiGuiContainer}.
     */
    public void renderDebugRect(RenderOpQueue queue)
    {
        queue.enqueue(new DebugRectOp(false, rect, pivotPosX, pivotPosY));
    }
}
