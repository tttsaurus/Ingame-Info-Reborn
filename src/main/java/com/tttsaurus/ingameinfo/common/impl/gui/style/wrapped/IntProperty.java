package com.tttsaurus.ingameinfo.common.impl.gui.style.wrapped;

import com.tttsaurus.ingameinfo.common.api.gui.style.wrapped.IWrappedStyleProperty;

public final class IntProperty extends IWrappedStyleProperty<Integer>
{
    @Override
    public Integer get() { return value; }
    @Override
    public void set(Integer value) { this.value = value; }
}
