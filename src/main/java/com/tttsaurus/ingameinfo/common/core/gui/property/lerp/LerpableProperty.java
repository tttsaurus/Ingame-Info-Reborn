package com.tttsaurus.ingameinfo.common.core.gui.property.lerp;

public abstract class LerpableProperty<T>
{
    protected T prevValue = null;
    protected T currValue = null;

    public final T getCurrValue() { return currValue; }

    public final void setPrevValue(Object value) { prevValue = (T)value; }
    public final void setCurrValue(Object value) { currValue = (T)value; }

    /**
     * If <code>lerp</code> is being called during render update, then {@link #prevValue} and {@link #currValue} are non-null for sure.
     *
     * @param percentage The interpolation alpha. Range: [0, 1]
     * @return The interpolated value.
     */
    public abstract T lerp(float percentage);
}
