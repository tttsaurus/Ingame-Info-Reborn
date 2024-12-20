package com.tttsaurus.ingameinfo.common.api.mvvm.binding;

import java.util.ArrayList;
import java.util.List;

public class ReactiveObject<T>
{
    protected final List<IReactiveCallback> setterCallbacks = new ArrayList<>();

    private T value;
    public T get()
    {
        return value;
    }
    public void set(T value)
    {
        this.value = value;
        for (IReactiveCallback callback: setterCallbacks) callback.invoke();
    }
}
