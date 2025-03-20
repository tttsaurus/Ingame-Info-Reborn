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
// ofc T can't be View itself
@SuppressWarnings("all")
public abstract class ViewModel<T extends View>
{
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
