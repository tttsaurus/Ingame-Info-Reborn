package com.tttsaurus.ingameinfo.common.api.mvvm.registry;

import com.tttsaurus.ingameinfo.common.api.mvvm.binding.IReactiveObjectGetter;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class RegistryUtils
{
    public static Map<Reactive, IReactiveObjectGetter> findReactiveObjects(Class<? extends ViewModel<?>> clazz)
    {
        Class<Reactive> annotation = Reactive.class;
        Map<Reactive, IReactiveObjectGetter> reactiveObjects = new HashMap<>();

        for (Field field: clazz.getDeclaredFields())
            if (field.isAnnotationPresent(annotation))
            {
                Reactive reactive = field.getAnnotation(annotation);
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                try
                {
                    Class<?> fieldClass = field.getType();
                    String fieldName = field.getName();
                    if (ReactiveObject.class.equals(fieldClass))
                    {
                        MethodHandle getter = lookup.findGetter(clazz, fieldName, fieldClass);
                        IReactiveObjectGetter wrappedGetter = (target) ->
                        {
                            try
                            {
                                return (ReactiveObject<?>)getter.invoke(target);
                            }
                            catch (Throwable ignored) { return null; }
                        };
                        reactiveObjects.put(reactive, wrappedGetter);
                    }
                }
                catch (Exception ignored) { }
            }

        return reactiveObjects;
    }
}
