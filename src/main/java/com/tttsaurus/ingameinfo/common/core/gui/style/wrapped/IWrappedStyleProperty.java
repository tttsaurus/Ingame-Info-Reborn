package com.tttsaurus.ingameinfo.common.core.gui.style.wrapped;

// only do one inheritance; don't do nested inheritance
public abstract class IWrappedStyleProperty<T>
{
    protected T value;
    public T get() { return value; }
    public void set(T value) { this.value = value; }
}
