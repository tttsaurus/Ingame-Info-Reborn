package com.tttsaurus.ingameinfo.common.api.gui.registry;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertyCallback;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertySetter;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.serialization.Deserializer;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.impl.serialization.PrimitiveTypesDeserializer;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SuppressWarnings("all")
public final class RegistryUtils
{
    public static List<Class<? extends Element>> findRegisteredElements(String packageName)
    {
        Class<RegisterElement> annotation = RegisterElement.class;
        List<Class<? extends Element>> annotatedClasses = new ArrayList<>();

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
                                        annotatedClasses.add((Class<? extends Element>) clazz);
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

    public static Map<String, IStylePropertySetter> handleStyleProperties(Class<? extends Element> clazz, Map<IStylePropertySetter, IDeserializer<?>> stylePropertyDeserializers, Map<IStylePropertySetter, IStylePropertyCallback> stylePropertySetterCallbacks)
    {
        Map<String, IStylePropertySetter> setters = new HashMap<>();

        for (Field field : clazz.getDeclaredFields())
            if (field.isAnnotationPresent(StyleProperty.class))
            {
                StyleProperty styleProperty = field.getAnnotation(StyleProperty.class);
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                MethodHandle setter;
                try
                {
                    // setter
                    Class<?> fieldClass = field.getType();
                    String fieldName = field.getName();
                    setter = lookup.findSetter(clazz, fieldName, fieldClass);
                    IStylePropertySetter wrappedSetter = (target, value) ->
                    {
                        try
                        {
                            if (value.getClass().getName().equals(field.getType().getName()))
                                setter.invoke(target, value);
                        }
                        catch (Throwable ignored) { }
                    };
                    setters.put(styleProperty.name().isEmpty() ? fieldName : styleProperty.name(), wrappedSetter);

                    // deserializer
                    if (fieldClass.isPrimitive() || fieldClass.equals(String.class))
                        stylePropertyDeserializers.put(wrappedSetter, new PrimitiveTypesDeserializer<>(fieldClass));
                    else if (fieldClass.isAnnotationPresent(Deserializer.class))
                    {
                        Deserializer deserializer = fieldClass.getAnnotation(Deserializer.class);
                        stylePropertyDeserializers.put(wrappedSetter, deserializer.value().newInstance());
                    }

                    // callback
                    String callbackName = styleProperty.setterCallback();
                    if (!callbackName.isEmpty())
                    {
                        Method callback = clazz.getMethod(callbackName);
                        IStylePropertyCallback wrappedCallback = new IStylePropertyCallback()
                        {
                            @Override
                            public void invoke(Element target)
                            {
                                try
                                {
                                    callback.invoke(target, new Object[0]);
                                }
                                catch (Exception ignored) { }
                            }

                            @Override
                            public String name() { return callbackName; }
                        };
                        stylePropertySetterCallbacks.put(wrappedSetter, wrappedCallback);
                    }
                }
                catch (Exception ignored) { }
            }

        return setters;
    }
}
