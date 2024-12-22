package com.tttsaurus.ingameinfo.common.impl.gui.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegistryUtils;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertyCallback;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertySetter;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;
import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings("all")
public final class ElementRegistry
{
    // key: element class name
    private static final Map<String, Map<String, IStylePropertySetter>> stylePropertySetters = new HashMap<>();

    // IStylePropertySetter is the primary key here
    private static final Map<IStylePropertySetter, IDeserializer<?>> stylePropertyDeserializers = new HashMap<>();
    private static final Map<IStylePropertySetter, IStylePropertyCallback> stylePropertySetterCallbacksPre = new HashMap<>();
    private static final Map<IStylePropertySetter, IStylePropertyCallback> stylePropertySetterCallbacksPost = new HashMap<>();
    private static final Map<IStylePropertySetter, Class<?>> stylePropertyClasses = new HashMap<>();

    private static final List<Class<? extends Element>> registeredElements = new ArrayList<>();
    private static final List<String> elementPackages = new ArrayList<>(Arrays.asList(
            "com.tttsaurus.ingameinfo.common.api.gui",
            "com.tttsaurus.ingameinfo.common.impl.gui.control",
            "com.tttsaurus.ingameinfo.common.impl.gui.layout"
    ));

    @Nullable
    public static IStylePropertySetter getStylePropertySetter(Class<? extends Element> clazz, String name)
    {
        if (stylePropertySetters.containsKey(clazz.getName()))
        {
            Map<String, IStylePropertySetter> map = stylePropertySetters.get(clazz.getName());
            if (map.containsKey(name)) return map.get(name);
        }
        if (Element.class.isAssignableFrom(clazz.getSuperclass()))
            return getStylePropertySetter((Class<? extends Element>)clazz.getSuperclass(), name);
        else
            return null;
    }
    @Nullable
    public static IDeserializer<?> getStylePropertyDeserializer(IStylePropertySetter setter)
    {
        return stylePropertyDeserializers.get(setter);
    }
    @Nullable
    public static IStylePropertyCallback getStylePropertySetterCallbackPre(IStylePropertySetter setter)
    {
        return stylePropertySetterCallbacksPre.get(setter);
    }
    @Nullable
    public static IStylePropertyCallback getStylePropertySetterCallbackPost(IStylePropertySetter setter)
    {
        return stylePropertySetterCallbacksPost.get(setter);
    }
    @Nullable
    public static Class<?> getStylePropertyClass(IStylePropertySetter setter)
    {
        return stylePropertyClasses.get(setter);
    }

    public static ImmutableMap<String, Map<String, IStylePropertySetter>> getStylePropertySetters() { return ImmutableMap.copyOf(stylePropertySetters); }
    public static ImmutableMap<IStylePropertySetter, IDeserializer<?>> getStylePropertyDeserializers() { return ImmutableMap.copyOf(stylePropertyDeserializers); }
    public static ImmutableMap<IStylePropertySetter, IStylePropertyCallback> getStylePropertySetterCallbacksPre() { return ImmutableMap.copyOf(stylePropertySetterCallbacksPre); }
    public static ImmutableMap<IStylePropertySetter, IStylePropertyCallback> getStylePropertySetterCallbacksPost() { return ImmutableMap.copyOf(stylePropertySetterCallbacksPost); }
    public static ImmutableMap<IStylePropertySetter, Class<?>> getStylePropertyClasses() { return ImmutableMap.copyOf(stylePropertyClasses); }

    public static ImmutableList<Class<? extends Element>> getRegisteredElements() { return ImmutableList.copyOf(registeredElements); }
    public static void addElementPackage(String packageName) { elementPackages.add(packageName); }

    public static void register()
    {
        registeredElements.clear();

        for (String packageName: elementPackages)
            registeredElements.addAll(RegistryUtils.findRegisteredElements(packageName));

        stylePropertySetters.clear();
        stylePropertyDeserializers.clear();
        stylePropertySetterCallbacksPre.clear();
        stylePropertySetterCallbacksPost.clear();
        stylePropertyClasses.clear();

        for (Class<? extends Element> clazz: registeredElements)
            stylePropertySetters.put(clazz.getName(), RegistryUtils.handleStyleProperties(
                    clazz,
                    stylePropertyDeserializers,
                    stylePropertySetterCallbacksPre,
                    stylePropertySetterCallbacksPost,
                    stylePropertyClasses));
    }
}
