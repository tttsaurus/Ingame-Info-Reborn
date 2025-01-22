package com.tttsaurus.ingameinfo.common.api.gui.registry;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.style.*;
import com.tttsaurus.ingameinfo.common.api.gui.style.wrapped.IWrappedStyleProperty;
import com.tttsaurus.ingameinfo.common.api.reflection.TypeUtils;
import com.tttsaurus.ingameinfo.common.api.serialization.Deserializer;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.impl.serialization.BuiltinTypesDeserializer;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SuppressWarnings("all")
public final class RegistryUtils
{
    public static Map<String, Class<? extends Element>> handleRegisteredElements(
            String packageName,
            Map<Class<? extends Element>, RegisterElement> elementAnnotations)
    {
        Class<RegisterElement> annotation = RegisterElement.class;
        Map<String, Class<? extends Element>> annotatedClasses = new HashMap<>();

        String path = packageName.replace(".", "/");
        URL packageURL = annotation.getClassLoader().getResource(path);

        if (packageURL != null)
        {
            String protocol = packageURL.getProtocol();
            try
            {
                if ("jar".equals(protocol))
                {
                    String jarPath = packageURL.getPath().substring(5, packageURL.getPath().indexOf("!"));
                    try (JarFile jarFile = new JarFile(jarPath))
                    {
                        Enumeration<JarEntry> entries = jarFile.entries();

                        while (entries.hasMoreElements())
                        {
                            JarEntry entry = entries.nextElement();
                            String entryName = entry.getName();

                            if (entryName.endsWith(".class") && entryName.startsWith(path))
                            {
                                String className = entryName.replace("/", ".").substring(0, entryName.length() - 6);
                                try
                                {
                                    Class<?> clazz = Class.forName(className);
                                    if (clazz.isAnnotationPresent(annotation) && Element.class.isAssignableFrom(clazz))
                                    {
                                        Class<? extends Element> elementClass = (Class<? extends Element>)clazz;
                                        annotatedClasses.put(clazz.getSimpleName(), elementClass);
                                        elementAnnotations.put(elementClass, elementClass.getAnnotation(annotation));
                                    }
                                }
                                catch (ClassNotFoundException ignored) { }
                            }
                        }
                    }
                }
            }
            catch (IOException ignored) { }
        }

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
                try
                {
                    // setter
                    Class<?> fieldClass = field.getType();
                    String fieldName = field.getName();
                    IStylePropertySetter wrappedSetter;
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

                    // getter
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

                    // deserializer
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
                        stylePropertyDeserializers.put(wrappedSetter, deserializer.value().newInstance());
                    }
                    else if (TypeUtils.isPrimitiveOrWrappedPrimitive(fieldClass) || fieldClass.equals(String.class))
                        stylePropertyDeserializers.put(wrappedSetter, new BuiltinTypesDeserializer<>(fieldClass));
                    else if (fieldClass.isAnnotationPresent(Deserializer.class))
                    {
                        Deserializer deserializer = fieldClass.getAnnotation(Deserializer.class);
                        stylePropertyDeserializers.put(wrappedSetter, deserializer.value().newInstance());
                    }

                    // setter callback pre
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
                                stylePropertySetterCallbacksPre.put(wrappedSetter, new IStylePropertyCallbackPre()
                                {
                                    @Override
                                    public void invoke(Element target, Object value, CallbackInfo callbackInfo)
                                    {
                                        try
                                        {
                                            if (finalHasInputValue)
                                                finalSetterCallbackPre.invoke(target, value, callbackInfo);
                                            else
                                                finalSetterCallbackPre.invoke(target, callbackInfo);
                                        }
                                        catch (Exception ignored) { }
                                    }

                                    @Override
                                    public String name() { return setterCallbackPreName; }
                                });
                            }
                        }
                    }

                    // setter callback post
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
                                stylePropertySetterCallbacksPost.put(wrappedSetter, new IStylePropertyCallbackPost()
                                {
                                    @Override
                                    public void invoke(Element target, Object value)
                                    {
                                        try
                                        {
                                            if (finalHasInputValue)
                                                finalSetterCallbackPost.invoke(target, value);
                                            else
                                                finalSetterCallbackPost.invoke(target, new Object[0]);
                                        }
                                        catch (Exception ignored) { }
                                    }

                                    @Override
                                    public String name() { return setterCallbackPostName; }
                                });
                            }
                        }
                    }

                    // class
                    stylePropertyClasses.put(wrappedSetter, hasWrappedClass ? wrappedClass : fieldClass);
                }
                catch (Exception ignored) { }
            }

        return setters;
    }
}
