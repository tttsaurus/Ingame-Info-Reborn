package com.tttsaurus.ingameinfo.common.core.mvvm.viewmodel;

import com.tttsaurus.ingameinfo.common.core.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.core.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.core.function.IFunc;
import com.tttsaurus.ingameinfo.common.core.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.core.InternalMethods;
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
    private String mvvmRegistryName;
    public String getMvvmRegistryName() { return mvvmRegistryName; }

    private final List<SlotAccessor> slotAccessors = new ArrayList<>();

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

    private VvmBinding<T> binding = new VvmBinding<>();

    // MvvmRegistry.cacheIgiGuiContainer() calls init
    private GuiLayout init(String mvvmRegistryName)
    {
        this.mvvmRegistryName = mvvmRegistryName;
        GuiLayout guiLayout = binding.init(this, mvvmRegistryName);

        Map<Reactive, IReactiveObjectGetter> reactiveObjects = MvvmRegistry.getRegisteredReactiveObjects(mvvmRegistryName);
        for (Map.Entry<Reactive, IReactiveObjectGetter> entry: reactiveObjects.entrySet())
            binding.bindReactiveObject(entry.getKey(), entry.getValue().get(this));

        Map<Reactive, IReactiveCollectionGetter> reactiveCollections = MvvmRegistry.getRegisteredReactiveCollections(mvvmRegistryName);
        for (Map.Entry<Reactive, IReactiveCollectionGetter> entry: reactiveCollections.entrySet())
            binding.bindReactiveCollection(entry.getKey(), entry.getValue().get(this));

        this.slotAccessors.clear();
        Map<Reactive, ISlotAccessorGetter> slotAccessors = MvvmRegistry.getRegisteredSlotAccessors(mvvmRegistryName);
        for (Map.Entry<Reactive, ISlotAccessorGetter> entry: slotAccessors.entrySet())
        {
            SlotAccessor slotAccessor = entry.getValue().get(this);
            this.slotAccessors.add(slotAccessor);
            binding.bindSlotAccessor(entry.getKey(), slotAccessor);
        }

        return guiLayout;
    }

    public final void refresh(IgiGuiContainer container)
    {
        if (binding.view == null) return;
        binding.view.refresh(container);

        Map<Reactive, IReactiveObjectGetter> reactiveObjects = MvvmRegistry.getRegisteredReactiveObjects(mvvmRegistryName);
        for (Map.Entry<Reactive, IReactiveObjectGetter> entry: reactiveObjects.entrySet())
        {
            ReactiveObject<?> reactiveObject = entry.getValue().get(this);
            InternalMethods.instance.ReactiveObject$initiativeCallbacks$getter.invoke(reactiveObject).clear();
            InternalMethods.instance.ReactiveObject$passiveCallbacks$getter.invoke(reactiveObject).clear();
            binding.bindReactiveObject(entry.getKey(), reactiveObject);
        }

        Map<Reactive, IReactiveCollectionGetter> reactiveCollections = MvvmRegistry.getRegisteredReactiveCollections(mvvmRegistryName);
        for (Map.Entry<Reactive, IReactiveCollectionGetter> entry: reactiveCollections.entrySet())
        {
            ReactiveCollection reactiveCollection = entry.getValue().get(this);
            InternalMethods.instance.ReactiveCollection$group$setter.invoke(reactiveCollection, null);
            binding.bindReactiveCollection(entry.getKey(), reactiveCollection);
        }

        this.slotAccessors.clear();
        Map<Reactive, ISlotAccessorGetter> slotAccessors = MvvmRegistry.getRegisteredSlotAccessors(mvvmRegistryName);
        for (Map.Entry<Reactive, ISlotAccessorGetter> entry: slotAccessors.entrySet())
        {
            SlotAccessor slotAccessor = entry.getValue().get(this);
            this.slotAccessors.add(slotAccessor);
            InternalMethods.instance.SlotAccessor$group$setter.invoke(slotAccessor, null);
            binding.bindSlotAccessor(entry.getKey(), slotAccessor);
        }
    }

    public abstract void onStart();
    public abstract void onFixedUpdate(double deltaTime);
}
