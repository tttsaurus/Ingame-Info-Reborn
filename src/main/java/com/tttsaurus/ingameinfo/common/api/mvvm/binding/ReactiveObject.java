package com.tttsaurus.ingameinfo.common.api.mvvm.binding;

import com.tttsaurus.ingameinfo.common.api.function.IAction_1Param;
import java.util.ArrayList;
import java.util.List;

public abstract class ReactiveObject<T>
{
    protected final List<IReactiveCallback> initiativeCallbacks = new ArrayList<>();
    protected final List<IReactiveCallback> passiveCallbacks = new ArrayList<>();

    private T value;
    public T get()
    {
        return value;
    }

    @SuppressWarnings("all")
    public void addListener(IAction_1Param<T> action)
    {
        passiveCallbacks.add((value) ->
        {
            T v = (T)value;
            action.invoke(v);
        });
    }

    // to view
    @SuppressWarnings("all")
    public void set(Object value)
    {
        this.value = (T)value;
        for (IReactiveCallback callback: initiativeCallbacks) callback.invoke(this.value);
    }
    // from view
    @SuppressWarnings("all")
    protected void setInternal(Object value)
    {
        this.value = (T)value;
        for (IReactiveCallback callback: passiveCallbacks) callback.invoke(this.value);
    }
}
