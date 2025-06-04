package com.tttsaurus.ingameinfo.common.core.gui.property.lerp;

public abstract class LerpableProperty<T>
{
    protected T prevValue;
    protected T currValue;

    public final void setPrevValue(T value) { prevValue = value; }
    public final void setCurrValue(T value) { currValue = value; }

    public abstract T lerp(float percentage);
}
