package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.api.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.plugin.crt.api.Types;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ingameinfo.mvvm.ReactiveObject")
public class ReactiveObjectWrapper
{
    protected ReactiveObject<?> reactiveObject;

    public ReactiveObjectWrapper(ReactiveObject<?> reactiveObject)
    {
        this.reactiveObject = reactiveObject;
    }

    private ReactiveObjectWrapper() { }

    public static <T> ReactiveObjectWrapper instantiate(Types type)
    {
        ReactiveObjectWrapper wrapper = new ReactiveObjectWrapper();
        switch (type)
        {
            case Int -> wrapper.reactiveObject = new ReactiveObject<Integer>(){};
            case Long -> wrapper.reactiveObject = new ReactiveObject<Long>(){};
            case Short -> wrapper.reactiveObject = new ReactiveObject<Short>(){};
            case Byte -> wrapper.reactiveObject = new ReactiveObject<Byte>(){};
            case Double -> wrapper.reactiveObject = new ReactiveObject<Double>(){};
            case Float -> wrapper.reactiveObject = new ReactiveObject<Float>(){};
            case Char -> wrapper.reactiveObject = new ReactiveObject<Character>(){};
            case Boolean -> wrapper.reactiveObject = new ReactiveObject<Boolean>(){};
            case String -> wrapper.reactiveObject = new ReactiveObject<String>(){};
        }
        return wrapper;
    }

    @ZenMethod
    public Object get() { return reactiveObject.get(); }
    @ZenMethod
    public void set(Object value) { reactiveObject.set(value); }
}
