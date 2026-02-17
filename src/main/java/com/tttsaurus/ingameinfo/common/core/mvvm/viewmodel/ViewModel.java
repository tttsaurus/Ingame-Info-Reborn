package com.tttsaurus.ingameinfo.common.core.mvvm.viewmodel;

import com.tttsaurus.ingameinfo.common.core.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.core.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.core.function.IFunc;
import com.tttsaurus.ingameinfo.common.core.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.core.InternalMethods;
import com.tttsaurus.ingameinfo.common.core.gui.event.IUIEventListener;
import com.tttsaurus.ingameinfo.common.core.gui.event.UIEvent;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.RenderDecorator;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.*;
import com.tttsaurus.ingameinfo.common.core.mvvm.context.SharedContext;
import com.tttsaurus.ingameinfo.common.core.mvvm.view.View;
import com.tttsaurus.ingameinfo.common.core.mvvm.registry.MvvmRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// only do one inheritance; don't do nested inheritance
// ofc T can't be View itself
@SuppressWarnings("all")
public abstract class ViewModel<T extends View>
{
    private VvmBinding<T> binding = new VvmBinding<>();

    private RenderDecorator getRenderDecorator()
    {
        return binding.view.getRenderDecorator();
    }

    private String mvvmRegistryName;
    public final String getMvvmRegistryName() { return mvvmRegistryName; }

    private final List<SlotAccessor> slotAccessors = new ArrayList<>();
    private final EventListenerBinder eventListenerBinder = new EventListenerBinder();

    public final <T extends UIEvent> void bindEventListener(String uid, Class<T> type, IUIEventListener<T> listener)
    {
        eventListenerBinder.bind(binding.view, uid, type, listener, -1);
    }
    public final <T extends UIEvent> void bindEventListener(String uid, Class<T> type, IUIEventListener<T> listener, int ordinal)
    {
        eventListenerBinder.bind(binding.view, uid, type, listener, ordinal);
    }

    protected final SharedContext sharedContext = new SharedContext();

    // getters & setters for communication with IgiGuiContainer
    // will be injected by IgiGuiContainer before start()
    private IAction_1Param<Boolean> isActiveSetter = null;
    private IFunc<Boolean> isActiveGetter = null;
    private IAction_1Param<IFunc<Boolean>> exitCallbackSetter = null;
    private IAction_1Param<Boolean> isFocusedSetter = null;
    private IFunc<Boolean> isFocusedGetter = null;

    public void setActive(boolean flag)
    {
        if (isActiveSetter == null) return;
        isActiveSetter.invoke(flag);
    }
    public boolean getActive()
    {
        if (isActiveGetter == null) return false;
        return isActiveGetter.invoke();
    }
    public void setExitCallback(IFunc<Boolean> callback)
    {
        if (exitCallbackSetter == null) return;
        exitCallbackSetter.invoke(callback);
    }
    public void setFocused(boolean flag)
    {
        if (isFocusedSetter == null) return;
        isFocusedSetter.invoke(flag);
    }
    public boolean getFocused()
    {
        if (isFocusedGetter == null) return false;
        return isFocusedGetter.invoke();
    }

    // MvvmRegistry.cacheIgiGuiContainer() calls init
    private GuiLayout init(String mvvmRegistryName, MvvmRegistry mvvmRegistry)
    {
        this.mvvmRegistryName = mvvmRegistryName;
        GuiLayout guiLayout = binding.init(this, mvvmRegistryName);

        Map<Reactive, IReactiveObjectGetter> reactiveObjects = mvvmRegistry.getRegisteredReactiveObjects(mvvmRegistryName);
        for (Map.Entry<Reactive, IReactiveObjectGetter> entry: reactiveObjects.entrySet())
            binding.bindReactiveObject(entry.getKey(), entry.getValue().get(this));

        Map<Reactive, IReactiveCollectionGetter> reactiveCollections = mvvmRegistry.getRegisteredReactiveCollections(mvvmRegistryName);
        for (Map.Entry<Reactive, IReactiveCollectionGetter> entry: reactiveCollections.entrySet())
            binding.bindReactiveCollection(entry.getKey(), entry.getValue().get(this));

        this.slotAccessors.clear();
        Map<Reactive, ISlotAccessorGetter> slotAccessors = mvvmRegistry.getRegisteredSlotAccessors(mvvmRegistryName);
        for (Map.Entry<Reactive, ISlotAccessorGetter> entry: slotAccessors.entrySet())
        {
            SlotAccessor slotAccessor = entry.getValue().get(this);
            this.slotAccessors.add(slotAccessor);
            binding.bindSlotAccessor(entry.getKey(), slotAccessor);
        }

        return guiLayout;
    }

    public final void refresh(IgiGuiContainer container, MvvmRegistry mvvmRegistry)
    {
        if (binding.view == null) return;
        binding.view.refresh(container);

        Map<Reactive, IReactiveObjectGetter> reactiveObjects = mvvmRegistry.getRegisteredReactiveObjects(mvvmRegistryName);
        for (Map.Entry<Reactive, IReactiveObjectGetter> entry: reactiveObjects.entrySet())
        {
            ReactiveObject<?> reactiveObject = entry.getValue().get(this);
            InternalMethods.ReactiveObject$initiativeCallbacks$getter(reactiveObject).clear();
            InternalMethods.ReactiveObject$passiveCallbacks$getter(reactiveObject).clear();
            binding.bindReactiveObject(entry.getKey(), reactiveObject);
        }

        Map<Reactive, IReactiveCollectionGetter> reactiveCollections = mvvmRegistry.getRegisteredReactiveCollections(mvvmRegistryName);
        for (Map.Entry<Reactive, IReactiveCollectionGetter> entry: reactiveCollections.entrySet())
        {
            ReactiveCollection reactiveCollection = entry.getValue().get(this);
            InternalMethods.ReactiveCollection$group$setter(reactiveCollection, null);
            binding.bindReactiveCollection(entry.getKey(), reactiveCollection);
        }

        this.slotAccessors.clear();
        Map<Reactive, ISlotAccessorGetter> slotAccessors = mvvmRegistry.getRegisteredSlotAccessors(mvvmRegistryName);
        for (Map.Entry<Reactive, ISlotAccessorGetter> entry: slotAccessors.entrySet())
        {
            SlotAccessor slotAccessor = entry.getValue().get(this);
            this.slotAccessors.add(slotAccessor);
            InternalMethods.SlotAccessor$group$setter(slotAccessor, null);
            binding.bindSlotAccessor(entry.getKey(), slotAccessor);
        }
    }

    public abstract void onStart();
    public abstract void onFixedUpdate(double deltaTime);
    public void onGuiOpen() { }
    public void onGuiClose() { }
}
