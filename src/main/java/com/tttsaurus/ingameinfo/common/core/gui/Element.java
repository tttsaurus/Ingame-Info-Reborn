package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.core.gui.event.IUIEventListener;
import com.tttsaurus.ingameinfo.common.core.gui.event.UIEvent;
import com.tttsaurus.ingameinfo.common.core.gui.event.UIEventListenerType;
import com.tttsaurus.ingameinfo.common.core.input.InputState;
import com.tttsaurus.ingameinfo.common.core.gui.layout.*;
import com.tttsaurus.ingameinfo.common.core.gui.property.lerp.*;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.IStylePropertySyncTo;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.BackgroundOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.DebugRectOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.core.gui.registry.ElementRegistry;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.VvmBinding;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.IRenderOp;
import com.tttsaurus.ingameinfo.common.core.reflection.FieldUtils;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.*;

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
    private final Map<String, IStylePropertySyncTo> syncToMap = new HashMap<>();

    /**
     * <code>lerpTargetGetters</code> stores the getters of {@link LerpableProperty}'s target.
     * {@link Element#onCollectLerpInfo()} will cache getters to <code>lerpTargetGetters</code> at runtime.
     *
     * @see Element#onCollectLerpInfo()
     */
    private Map<LerpableProperty<?>, ITargetingLerpTarget> lerpTargetGetters = null;

    /**
     * <code>uiEventListeners</code> stores event listeners.
     * They will be fired by {@link Element#fireEvent(UIEvent)}.
     * They will be added by {@link Element#addEventListener(Class, IUIEventListener)}.
     *
     * @see Element#fireEvent(UIEvent)
     * @see Element#addEventListener(Class, IUIEventListener)
     */
    private final Map<Class<? extends UIEvent>, List<IUIEventListener<?>>> uiEventListeners = new HashMap<>();

    /**
     * The parent node. Will be injected by {@link ElementGroup#add(Element)}.
     */
    @SuppressWarnings("all")
    private ElementGroup parent = null;
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

    //<editor-fold desc="final method: ui event listener">
    /**
     * Execute {@link IUIEventListener} of the corresponding event.
     * <ul>
     *     <li>Local listeners will be executed immediately first</li>
     *     <li>Then capturing process happens</li>
     *     <li>Then target listeners will be executed</li>
     *     <li>Then bubbling process happens</li>
     * </ul>
     * This method should be called during {@link Element#onPropagateInput(InputState)}.
     *
     * @param event The {@link UIEvent}.
     * @param <T> Generic parameter of the type of the {@link UIEvent}.
     *
     * @see Element#uiEventListeners
     * @see Element#onPropagateInput(InputState)
     */
    @SuppressWarnings("unchecked")
    protected final <T extends UIEvent> void fireEvent(T event)
    {
        List<IUIEventListener<?>> list = uiEventListeners.get(event.getClass());

        // local
        if (list != null)
            for (IUIEventListener<?> listener: list)
                if (listener.type() == UIEventListenerType.LOCAL)
                    ((IUIEventListener<T>)listener).handle(event);

        if (event.isConsumed()) return;

        Deque<Element> stack = new ArrayDeque<>();
        Element parent = this;
        stack.push(parent);
        while (parent.parent != null)
        {
            parent = parent.parent;
            stack.push(parent);
        }

        // capture
        while (!stack.isEmpty())
        {
            Element element = stack.pop();
            List<IUIEventListener<?>> list2 = element.uiEventListeners.get(event.getClass());
            if (list2 != null)
                for (IUIEventListener<?> listener: list2)
                    if (listener.type() == UIEventListenerType.CAPTURE)
                        ((IUIEventListener<T>)listener).handle(event);
        }

        if (event.isConsumed()) return;

        // target
        if (list != null)
            for (IUIEventListener<?> listener: list)
                if (listener.type() == UIEventListenerType.TARGET)
                    ((IUIEventListener<T>)listener).handle(event);

        if (event.isConsumed()) return;

        // bubble
        parent = this;
        while (parent.parent != null)
        {
            List<IUIEventListener<?>> list2 = parent.uiEventListeners.get(event.getClass());
            if (list2 != null)
                for (IUIEventListener<?> listener: list2)
                    if (listener.type() == UIEventListenerType.BUBBLE)
                        ((IUIEventListener<T>)listener).handle(event);
            parent = parent.parent;
        }
    }

    /**
     * Add event listener for the corresponding type of {@link UIEvent} to {@link Element#uiEventListeners}.
     *
     * @param type Type of the {@link UIEvent}.
     * @param listener UI event listener.
     * @param <T> Generic parameter of the type of the {@link UIEvent}.
     *
     * @see Element#uiEventListeners
     */
    public final <T extends UIEvent> void addEventListener(Class<T> type, IUIEventListener<T> listener)
    {
        uiEventListeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }
    //</editor-fold>

    //<editor-fold desc="final method: style property">
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
    //</editor-fold>

    /**
     * This method will be executed right after {@link Element#onFixedUpdate(double)}
     * to update {@link LerpableProperty} for interpolation purposes.
     *
     * @see LerpableProperty
     * @see IgiGuiContainer#onFixedUpdate(double)
     */
    public void onCollectLerpInfo()
    {
        if (lerpTargetGetters == null)
        {
            lerpTargetGetters = new HashMap<>();
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            List<ILerpablePropertyGetter> getters = ElementRegistry.getLerpablePropertyGetters(getClass());
            for (ILerpablePropertyGetter getter: getters)
            {
                LerpTarget lerpTarget = ElementRegistry.getLerpTarget(getClass(), getter);
                if (lerpTarget == null) continue;

                LerpableProperty<?> property = getter.get(this);
                ITargetingLerpTarget targetGetter = null;

                try
                {
                    Field field = FieldUtils.getFieldByNameIncludingSuperclasses(getClass(), lerpTarget.value());
                    MethodHandle handle = lookup.unreflectGetter(field);
                    boolean copy = lerpTarget.copyIfPossible() && ICopyableLerpTarget.class.isAssignableFrom(field.getType());
                    boolean copyArray = lerpTarget.copyIfPossible() && field.getType().getComponentType() != null && ICopyableLerpTarget.class.isAssignableFrom(field.getType().getComponentType());
                    if (lerpTarget.inner0().isEmpty())
                    {
                        targetGetter = () ->
                        {
                            try
                            {
                                Object res = handle.invoke(this);
                                if (copy || copyArray) return LerpTargetCopyUtils.copy(res);
                                return res;
                            }
                            catch (Throwable ignored) { return null; }
                        };
                    }
                    else
                    {
                        Field fieldInner0 = FieldUtils.getFieldByNameIncludingSuperclasses(field.getType(), lerpTarget.inner0());
                        MethodHandle handleInner0 = lookup.unreflectGetter(fieldInner0);
                        boolean copyInner0 = lerpTarget.copyIfPossible() && ICopyableLerpTarget.class.isAssignableFrom(fieldInner0.getType());
                        boolean copyArrayInner0 = lerpTarget.copyIfPossible() && fieldInner0.getType().getComponentType() != null && ICopyableLerpTarget.class.isAssignableFrom(fieldInner0.getType().getComponentType());
                        if (lerpTarget.inner1().isEmpty())
                        {
                            targetGetter = () ->
                            {
                                try
                                {
                                    Object res = handleInner0.invoke(handle.invoke(this));
                                    if (copyInner0 || copyArrayInner0) return LerpTargetCopyUtils.copy(res);
                                    return res;
                                }
                                catch (Throwable ignored) { return null; }
                            };
                        }
                        else
                        {
                            Field fieldInner1 = FieldUtils.getFieldByNameIncludingSuperclasses(fieldInner0.getType(), lerpTarget.inner1());
                            MethodHandle handleInner1 = lookup.unreflectGetter(fieldInner1);
                            boolean copyInner1 = lerpTarget.copyIfPossible() && ICopyableLerpTarget.class.isAssignableFrom(fieldInner1.getType());
                            boolean copyArrayInner1 = lerpTarget.copyIfPossible() && fieldInner1.getType().getComponentType() != null && ICopyableLerpTarget.class.isAssignableFrom(fieldInner1.getType().getComponentType());
                            targetGetter = () ->
                            {
                                try
                                {
                                    Object res = handleInner1.invoke(handleInner0.invoke(handle.invoke(this)));
                                    if (copyInner1 || copyArrayInner1) return LerpTargetCopyUtils.copy(res);
                                    return res;
                                }
                                catch (Throwable ignored) { return null; }
                            };
                        }
                    }
                }
                catch (Exception ignored) { }

                if (targetGetter != null)
                {
                    // first method handle call will be slow
                    // so comsuming it here
                    targetGetter.getTarget();
                    lerpTargetGetters.put(property, targetGetter);
                }
            }
        }
        for (Map.Entry<LerpableProperty<?>, ITargetingLerpTarget> entry: lerpTargetGetters.entrySet())
        {
            LerpableProperty<?> property = entry.getKey();
            ITargetingLerpTarget getter = entry.getValue();

            Object target = getter.getTarget();
            if (property.getCurrValue() == null)
                property.setPrevValue(target);
            else
                property.setPrevValue(property.getCurrValue());
            property.setCurrValue(target);
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
     * Propagate input states to child elements.
     *
     * @param inputState The current input state.
     */
    public void onPropagateInput(InputState inputState)
    {

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

    /**
     * Getter of the parent node.
     *
     * @return The parent node.
     */
    public ElementGroup getParent()
    {
        return parent;
    }
}
