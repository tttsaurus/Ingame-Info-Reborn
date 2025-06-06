package com.tttsaurus.ingameinfo.common.core.gui.property.lerp;

public abstract class LerpableProperty<T>
{
    protected T prevValue = null;
    protected T currValue = null;

    public final T getCurrValue() { return currValue; }

    public final void setPrevValue(Object value) { prevValue = (T)value; }
    public final void setCurrValue(Object value) { currValue = (T)value; }

    public abstract T lerp(float percentage);
}
