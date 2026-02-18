package com.tttsaurus.ingameinfo.common.core.mvvm.binding;

import com.tttsaurus.ingameinfo.common.core.function.Action1Param;
import java.util.ArrayList;
import java.util.List;

public abstract class ReactiveObject<T>
{
    protected final List<ReactiveCallback> initiativeCallbacks = new ArrayList<>();
    protected final List<ReactiveCallback> passiveCallbacks = new ArrayList<>();

    private T value;
    public final T get()
    {
        return value;
    }

    @SuppressWarnings("all")
    public final void addListener(Action1Param<T> action)
    {
        passiveCallbacks.add((value) ->
        {
            T v = (T)value;
            action.invoke(v);
        });
    }

    // to view
    @SuppressWarnings("all")
    public final void set(Object value)
    {
        this.value = (T)value;
        for (ReactiveCallback callback: initiativeCallbacks) callback.invoke(this.value);
    }
    // from view
    @SuppressWarnings("all")
    protected final void setInternal(Object value)
    {
        this.value = (T)value;
        for (ReactiveCallback callback: passiveCallbacks) callback.invoke(this.value);
    }
}
