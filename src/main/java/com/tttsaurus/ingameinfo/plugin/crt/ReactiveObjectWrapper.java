package com.tttsaurus.ingameinfo.plugin.crt;

import com.tttsaurus.ingameinfo.common.api.mvvm.binding.ReactiveObject;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ingameinfo.mvvm.ReactiveObject")
public class ReactiveObjectWrapper<T>
{
    protected ReactiveObject<T> reactiveObject;

    @SuppressWarnings("unchecked")
    public static <TLocal> ReactiveObjectWrapper<TLocal> instantiate(Types type, Class<TLocal> clazz)
    {
        ReactiveObjectWrapper<TLocal> wrapper = new ReactiveObjectWrapper<>();
        wrapper.reactiveObject = new ReactiveObject<TLocal>(){};
        switch (type)
        {
            case Int -> wrapper.reactiveObject = (ReactiveObject<TLocal>)new ReactiveObject<Integer>(){};
            case Long -> wrapper.reactiveObject = (ReactiveObject<TLocal>)new ReactiveObject<Long>(){};
            case Short -> wrapper.reactiveObject = (ReactiveObject<TLocal>)new ReactiveObject<Short>(){};
            case Byte -> wrapper.reactiveObject = (ReactiveObject<TLocal>)new ReactiveObject<Byte>(){};
            case Double -> wrapper.reactiveObject = (ReactiveObject<TLocal>)new ReactiveObject<Double>(){};
            case Float -> wrapper.reactiveObject = (ReactiveObject<TLocal>)new ReactiveObject<Float>(){};
            case Char -> wrapper.reactiveObject = (ReactiveObject<TLocal>)new ReactiveObject<Character>(){};
            case Boolean -> wrapper.reactiveObject = (ReactiveObject<TLocal>)new ReactiveObject<Boolean>(){};
            case String -> wrapper.reactiveObject = (ReactiveObject<TLocal>)new ReactiveObject<String>(){};
        }
        return wrapper;
    }

    @ZenMethod
    public T get() { return reactiveObject.get(); }
    @ZenMethod
    public void set(T value) { reactiveObject.set(value); }
}
