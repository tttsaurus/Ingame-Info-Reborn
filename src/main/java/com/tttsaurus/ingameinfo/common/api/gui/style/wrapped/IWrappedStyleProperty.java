package com.tttsaurus.ingameinfo.common.api.gui.style.wrapped;

// only do one inheritance; don't do nested inheritance
public abstract class IWrappedStyleProperty<T>
{
    protected T value;
    public abstract T get();
    public abstract void set(T value);
}
