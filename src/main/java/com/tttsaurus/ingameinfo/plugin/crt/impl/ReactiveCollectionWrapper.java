package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.core.mvvm.binding.ReactiveCollection;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ingameinfo.mvvm.ReactiveCollection")
public final class ReactiveCollectionWrapper
{
    protected final ReactiveCollection reactiveCollection;

    public ReactiveCollectionWrapper(ReactiveCollection reactiveCollection)
    {
        this.reactiveCollection = reactiveCollection;
    }

    @ZenMethod
    public int size()
    {
        return reactiveCollection.size();
    }
    @ZenMethod
    public ElementAccessorWrapper get(int index)
    {
        return new ElementAccessorWrapper(reactiveCollection.get(index));
    }
}
