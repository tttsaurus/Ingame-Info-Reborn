package com.tttsaurus.ingameinfo.common.api.gui.registry;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.style.ISetStyleProperty;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
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

    public static Map<String, ISetStyleProperty> findStylePropertySetters(Class<? extends Element> clazz)
    {
        Class<StyleProperty> annotation = StyleProperty.class;
        Map<String, ISetStyleProperty> setters = new HashMap<>();

        for (Field field : clazz.getDeclaredFields())
            if (field.isAnnotationPresent(annotation))
            {
                StyleProperty styleProperty = field.getAnnotation(annotation);
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                MethodHandle setter;
                try
                {
                    setter = lookup.findSetter(clazz, field.getName(), field.getType());
                    setters.put(styleProperty.name(), (target, value) ->
                    {
                        try
                        {
                            if (value.getClass().getName().equals(field.getType().getName()))
                                setter.invoke(target, value);
                        }
                        catch (Throwable ignored) { }
                    });
                }
                catch (Exception ignored) { }
            }

        return setters;
    }
}
