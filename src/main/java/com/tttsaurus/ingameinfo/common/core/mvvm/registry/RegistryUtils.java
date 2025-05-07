package com.tttsaurus.ingameinfo.common.core.mvvm.registry;

import com.tttsaurus.ingameinfo.common.core.mvvm.binding.*;
import com.tttsaurus.ingameinfo.common.core.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.plugin.crt.impl.CrtViewModel;
import net.minecraft.util.Tuple;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class RegistryUtils
{
    public static Map<Reactive, IReactiveObjectGetter> findReactiveObjects(String mvvmRegistryName, Class<? extends ViewModel<?>> clazz)
    {
        Map<Reactive, IReactiveObjectGetter> reactiveObjects = new HashMap<>();

        // crt support
        if (CrtViewModel.class.isAssignableFrom(clazz))
        {
            Map<String, Tuple<Reactive, ReactiveObject<?>>> def = CrtViewModel.reactiveObjectDefs.get(mvvmRegistryName);
            if (def != null)
                for (Tuple<Reactive, ReactiveObject<?>> tuple: def.values())
                    reactiveObjects.put(tuple.getFirst(), (target) -> tuple.getSecond());
            return reactiveObjects;
        }

        Class<Reactive> annotation = Reactive.class;

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

    public static Map<Reactive, IReactiveCollectionGetter> findReactiveCollections(String mvvmRegistryName, Class<? extends ViewModel<?>> clazz)
    {
        Map<Reactive, IReactiveCollectionGetter> reactiveCollections = new HashMap<>();

        Class<Reactive> annotation = Reactive.class;

        for (Field field: clazz.getDeclaredFields())
            if (field.isAnnotationPresent(annotation))
            {
                Reactive reactive = field.getAnnotation(annotation);
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                try
                {
                    Class<?> fieldClass = field.getType();
                    String fieldName = field.getName();
                    if (ReactiveCollection.class.equals(fieldClass))
                    {
                        MethodHandle getter = lookup.findGetter(clazz, fieldName, fieldClass);
                        IReactiveCollectionGetter wrappedGetter = (target) ->
                        {
                            try
                            {
                                return (ReactiveCollection)getter.invoke(target);
                            }
                            catch (Throwable ignored) { return null; }
                        };
                        reactiveCollections.put(reactive, wrappedGetter);
                    }
                }
                catch (Exception ignored) { }
            }

        return reactiveCollections;
    }
}
