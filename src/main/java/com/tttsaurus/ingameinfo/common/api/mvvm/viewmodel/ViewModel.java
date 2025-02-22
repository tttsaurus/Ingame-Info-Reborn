package com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel;

import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.api.function.IFunc;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.IReactiveObjectGetter;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.VvmBinding;
import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;
import com.tttsaurus.ingameinfo.common.impl.mvvm.registry.MvvmRegistry;
import java.util.Map;

// only do one inheritance; don't do nested inheritance
// ofc T can't be View
@SuppressWarnings("all")
public abstract class ViewModel<T extends View>
{
    // getters & setters
    // will be init before start()
    public IAction_1Param<Boolean> isActiveSetter;
    public IFunc<Boolean> isActiveGetter;
    public IAction_1Param<IFunc<Boolean>> exitCallbackSetter;
    public IAction_1Param<Boolean> isFocusedSetter;
    public IFunc<Boolean> isFocusedGetter;

    private VvmBinding<T> binding = new VvmBinding<>();

    // register entry point
    private GuiLayout init(String mvvmRegistryName)
    {
        GuiLayout guiLayout = binding.init(this, mvvmRegistryName);

        Map<Reactive, IReactiveObjectGetter> reactiveObjects = MvvmRegistry.getRegisteredReactiveObjects(mvvmRegistryName);
        for (Map.Entry<Reactive, IReactiveObjectGetter> entry: reactiveObjects.entrySet())
            binding.bindReactiveObject(entry.getKey(), entry.getValue().get(this));

        return guiLayout;
    }

    public abstract void start();
    public abstract void onFixedUpdate(double deltaTime);
}
