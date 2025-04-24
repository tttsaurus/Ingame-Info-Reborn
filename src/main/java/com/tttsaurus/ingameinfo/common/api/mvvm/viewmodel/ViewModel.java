package com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel;

import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.api.function.IFunc;
import com.tttsaurus.ingameinfo.common.api.internal.InternalMethods;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.*;
import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;
import com.tttsaurus.ingameinfo.common.impl.mvvm.registry.MvvmRegistry;
import java.util.Map;

// only do one inheritance; don't do nested inheritance
// ofc T can't be View itself
@SuppressWarnings("all")
public abstract class ViewModel<T extends View>
{
    protected String mvvmRegistryName;

    // getters & setters for communication with gui container
    // will be init before start()
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

    // register entry point
    private final GuiLayout init(String mvvmRegistryName)
    {
        this.mvvmRegistryName = mvvmRegistryName;
        GuiLayout guiLayout = binding.init(this, mvvmRegistryName);

        Map<Reactive, IReactiveObjectGetter> reactiveObjects = MvvmRegistry.getRegisteredReactiveObjects(mvvmRegistryName);
        for (Map.Entry<Reactive, IReactiveObjectGetter> entry: reactiveObjects.entrySet())
            binding.bindReactiveObject(entry.getKey(), entry.getValue().get(this));

        Map<Reactive, IReactiveCollectionGetter> reactiveCollections = MvvmRegistry.getRegisteredReactiveCollections(mvvmRegistryName);
        for (Map.Entry<Reactive, IReactiveCollectionGetter> entry: reactiveCollections.entrySet())
            binding.bindReactiveCollection(entry.getKey(), entry.getValue().get(this));

        return guiLayout;
    }

    public final void refresh()
    {
        if (binding.view == null) return;
        binding.view.refreshMainGroup();

        Map<Reactive, IReactiveObjectGetter> reactiveObjects = MvvmRegistry.getRegisteredReactiveObjects(mvvmRegistryName);
        for (Map.Entry<Reactive, IReactiveObjectGetter> entry: reactiveObjects.entrySet())
        {
            ReactiveObject<?> reactiveObject = entry.getValue().get(this);
            InternalMethods.instance.ReactiveObject$initiativeCallbacks$getter.invoke(reactiveObject).clear();
            binding.bindReactiveObject(entry.getKey(), reactiveObject);
        }

        Map<Reactive, IReactiveCollectionGetter> reactiveCollections = MvvmRegistry.getRegisteredReactiveCollections(mvvmRegistryName);
        for (Map.Entry<Reactive, IReactiveCollectionGetter> entry: reactiveCollections.entrySet())
        {
            ReactiveCollection reactiveCollection = entry.getValue().get(this);
            InternalMethods.instance.ReactiveCollection$group$setter.invoke(reactiveCollection, null);
            binding.bindReactiveCollection(entry.getKey(), reactiveCollection);
        }
    }

    public abstract void start();
    public abstract void onFixedUpdate(double deltaTime);
}
