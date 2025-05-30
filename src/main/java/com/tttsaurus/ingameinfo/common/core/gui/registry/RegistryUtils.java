package com.tttsaurus.ingameinfo.common.core.gui.registry;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.property.*;
import com.tttsaurus.ingameinfo.common.core.gui.property.wrapped.IWrappedStyleProperty;
import com.tttsaurus.ingameinfo.common.core.reflection.TypeUtils;
import com.tttsaurus.ingameinfo.common.core.serialization.Deserializer;
import com.tttsaurus.ingameinfo.common.core.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.impl.serialization.BuiltinTypesDeserializer;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

@SuppressWarnings("all")
public final class RegistryUtils
{
    public static Map<String, Class<? extends Element>> handleRegisteredElements(Map<Class<? extends Element>, RegisterElement> elementAnnotations)
    {
        Map<String, Class<? extends Element>> annotatedClasses = new HashMap<>();
        InGameInfoReborn.asmDataTable.getAll(RegisterElement.class.getCanonicalName()).forEach(data ->
        {
            String className = data.getClassName();
            try
            {
                Class<?> clazz = Class.forName(className);
                if (Element.class.isAssignableFrom(clazz))
                {
                    Class<? extends Element> elementClass = clazz.asSubclass(Element.class);
                    annotatedClasses.put(clazz.getSimpleName(), elementClass);
                    elementAnnotations.put(elementClass, elementClass.getAnnotation(RegisterElement.class));
                }
            }
            catch (ClassNotFoundException e)
            {
                InGameInfoReborn.logger.throwing(e);
            }
        });
        return annotatedClasses;
    }

    public static Map<String, IStylePropertySetter> handleStyleProperties(
            Class<? extends Element> clazz,
            Map<IStylePropertySetter, IDeserializer<?>> stylePropertyDeserializers,
            Map<IStylePropertySetter, IStylePropertyCallbackPre> stylePropertySetterCallbacksPre,
            Map<IStylePropertySetter, IStylePropertyCallbackPost> stylePropertySetterCallbacksPost,
            Map<IStylePropertySetter, Class<?>> stylePropertyClasses,
            Map<IStylePropertySetter, IStylePropertyGetter> stylePropertyGetters)
    {
        Map<String, IStylePropertySetter> setters = new HashMap<>();

        for (Field field : clazz.getDeclaredFields())
            if (field.isAnnotationPresent(StyleProperty.class))
            {
                StyleProperty styleProperty = field.getAnnotation(StyleProperty.class);
                MethodHandles.Lookup lookup = MethodHandles.lookup();

                Class<?> fieldClass = field.getType();
                String fieldName = field.getName();
                // setter is the primary key
                IStylePropertySetter wrappedSetter = null;

                //<editor-fold desc="setter">
                try
                {
                    if (IWrappedStyleProperty.class.isAssignableFrom(fieldClass))
                    {
                        MethodHandle getter = lookup.findGetter(clazz, fieldName, fieldClass);
                        wrappedSetter = (target, value) ->
                        {
                            try
                            {
                                IWrappedStyleProperty fieldValue = (IWrappedStyleProperty)getter.invoke(target);
                                fieldValue.set(value);
                            }
                            catch (Throwable ignored) { }
                        };
                    }
                    else
                    {
                        MethodHandle setter = lookup.findSetter(clazz, fieldName, fieldClass);
                        wrappedSetter = (target, value) ->
                        {
                            try
                            {
                                setter.invoke(target, value);
                            }
                            catch (Throwable ignored) { }
                        };
                    }
                    setters.put(styleProperty.name().isEmpty() ? fieldName : styleProperty.name(), wrappedSetter);
                }
                catch (Exception ignored) { }
                //</editor-fold>

                //<editor-fold desc="getter">
                try
                {
                    MethodHandle getter = lookup.findGetter(clazz, fieldName, fieldClass);
                    IStylePropertyGetter wrappedGetter = (target) ->
                    {
                        try
                        {
                            return getter.invoke(target);
                        }
                        catch (Throwable ignored) { return null; }
                    };
                    stylePropertyGetters.put(wrappedSetter, wrappedGetter);
                }
                catch (Exception ignored) { }
                //</editor-fold>

                //<editor-fold desc="deserializer">
                boolean hasWrappedClass = false;
                boolean isWrappedClassPrimitive = false;
                Class<?> wrappedClass = null;

                if (IWrappedStyleProperty.class.isAssignableFrom(fieldClass))
                {
                    hasWrappedClass = true;
                    wrappedClass = (Class<?>)((ParameterizedType)fieldClass.getGenericSuperclass()).getActualTypeArguments()[0];
                    if (TypeUtils.isPrimitiveOrWrappedPrimitive(wrappedClass) || wrappedClass.equals(String.class)) isWrappedClassPrimitive = true;
                }

                if (hasWrappedClass && isWrappedClassPrimitive)
                    stylePropertyDeserializers.put(wrappedSetter, new BuiltinTypesDeserializer<>(wrappedClass));
                else if (hasWrappedClass && wrappedClass.isAnnotationPresent(Deserializer.class))
                {
                    Deserializer deserializer = wrappedClass.getAnnotation(Deserializer.class);
                    try
                    {
                        stylePropertyDeserializers.put(wrappedSetter, deserializer.value().newInstance());
                    }
                    catch (Exception ignored) { }
                }
                else if (TypeUtils.isPrimitiveOrWrappedPrimitive(fieldClass) || fieldClass.equals(String.class))
                    stylePropertyDeserializers.put(wrappedSetter, new BuiltinTypesDeserializer<>(fieldClass));
                else if (fieldClass.isAnnotationPresent(Deserializer.class))
                {
                    Deserializer deserializer = fieldClass.getAnnotation(Deserializer.class);
                    try
                    {
                        stylePropertyDeserializers.put(wrappedSetter, deserializer.value().newInstance());
                    }
                    catch (Exception ignored) { }
                }
                //</editor-fold>

                //<editor-fold desc="setter callback pre">
                String setterCallbackPreName = styleProperty.setterCallbackPre();
                if (!setterCallbackPreName.isEmpty())
                {
                    boolean hasInputValue = false;
                    Method setterCallbackPre = null;
                    try
                    {
                        setterCallbackPre = clazz.getMethod(setterCallbackPreName, CallbackInfo.class);
                        hasInputValue = false;
                    }
                    catch (Exception ignored) { }
                    if (setterCallbackPre == null)
                        try
                        {
                            if (hasWrappedClass)
                                setterCallbackPre = clazz.getMethod(setterCallbackPreName, wrappedClass, CallbackInfo.class);
                            else
                                setterCallbackPre = clazz.getMethod(setterCallbackPreName, fieldClass, CallbackInfo.class);
                            hasInputValue = true;
                        }
                    catch (Exception ignored) { }

                    if (setterCallbackPre != null)
                    {
                        if (setterCallbackPre.isAnnotationPresent(StylePropertyCallback.class) && setterCallbackPre.getReturnType().equals(void.class))
                        {
                            boolean finalHasInputValue = hasInputValue;
                            Method finalSetterCallbackPre = setterCallbackPre;
                            try
                            {
                                MethodHandle handle = lookup.unreflect(finalSetterCallbackPre);
                                stylePropertySetterCallbacksPre.put(wrappedSetter, new IStylePropertyCallbackPre()
                                {
                                    @Override
                                    public void invoke(Element target, Object value, CallbackInfo callbackInfo)
                                    {
                                        try
                                        {
                                            if (finalHasInputValue)
                                                handle.invoke(target, value, callbackInfo);
                                            else
                                                handle.invoke(target, callbackInfo);
                                        }
                                        catch (Throwable ignored) { }
                                    }

                                    @Override
                                    public String name() { return setterCallbackPreName; }
                                });
                            }
                            catch (Exception ignored) { }
                        }
                    }
                }
                //</editor-fold>

                //<editor-fold desc="setter callback post">
                String setterCallbackPostName = styleProperty.setterCallbackPost();
                if (!setterCallbackPostName.isEmpty())
                {
                    boolean hasInputValue = false;
                    Method setterCallbackPost = null;
                    try
                    {
                        setterCallbackPost = clazz.getMethod(setterCallbackPostName);
                        hasInputValue = false;
                    }
                    catch (Exception ignored) { }
                    if (setterCallbackPost == null)
                        try
                        {
                            if (hasWrappedClass)
                                setterCallbackPost = clazz.getMethod(setterCallbackPostName, wrappedClass);
                            else
                                setterCallbackPost = clazz.getMethod(setterCallbackPostName, fieldClass);
                            hasInputValue = true;
                        }
                    catch (Exception ignored) { }

                    if (setterCallbackPost != null)
                    {
                        if (setterCallbackPost.isAnnotationPresent(StylePropertyCallback.class) && setterCallbackPost.getReturnType().equals(void.class))
                        {
                            boolean finalHasInputValue = hasInputValue;
                            Method finalSetterCallbackPost = setterCallbackPost;
                            try
                            {
                                MethodHandle handle = lookup.unreflect(finalSetterCallbackPost);
                                stylePropertySetterCallbacksPost.put(wrappedSetter, new IStylePropertyCallbackPost()
                                {
                                    @Override
                                    public void invoke(Element target, Object value)
                                    {
                                        try
                                        {
                                            if (finalHasInputValue)
                                                handle.invoke(target, value);
                                            else
                                                handle.invoke(target);
                                        }
                                        catch (Throwable ignored) { }
                                    }

                                    @Override
                                    public String name() { return setterCallbackPostName; }
                                });
                            }
                            catch (Exception ignored) { }
                        }
                    }
                }
                //</editor-fold>

                //<editor-fold desc="class">
                stylePropertyClasses.put(wrappedSetter, hasWrappedClass ? wrappedClass : fieldClass);
                //</editor-fold>
            }

        return setters;
    }
}
