package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.core.gui.ElementAccessor;
import com.tttsaurus.ingameinfo.plugin.crt.impl.types.WrapUnwrapUtils;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ingameinfo.gui.ElementAccessor")
public final class ElementAccessorWrapper
{
    protected final ElementAccessor elementAccessor;

    public ElementAccessorWrapper(ElementAccessor elementAccessor)
    {
        this.elementAccessor = elementAccessor;
    }

    @ZenMethod
    public void set(String propertyName, Object value)
    {
        elementAccessor.set(propertyName, WrapUnwrapUtils.safeUnwrap(value));
    }
    @ZenMethod
    public void set(String uid, String propertyName, Object value)
    {
        elementAccessor.set(uid, propertyName, WrapUnwrapUtils.safeUnwrap(value));
    }
    @ZenMethod
    public void set(String uid, String propertyName, Object value, int ordinal)
    {
        elementAccessor.set(uid, propertyName, WrapUnwrapUtils.safeUnwrap(value), ordinal);
    }

    @ZenMethod
    public Object get(String propertyName)
    {
        return WrapUnwrapUtils.safeWrap(elementAccessor.get(propertyName));
    }
    @ZenMethod
    public Object get(String uid, String propertyName)
    {
        return WrapUnwrapUtils.safeWrap(elementAccessor.get(uid, propertyName));
    }
    @ZenMethod
    public Object get(String uid, String propertyName, int ordinal)
    {
        return WrapUnwrapUtils.safeWrap(elementAccessor.get(uid, propertyName, ordinal));
    }
}
