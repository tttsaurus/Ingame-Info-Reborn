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
    // IStylePropertySetter is the primary key here
    // key: element class name
    private static final Map<String, Map<String, IStylePropertySetter>> stylePropertySetters = new HashMap<>();
    private static final Map<IStylePropertySetter, IDeserializer<?>> stylePropertyDeserializers = new HashMap<>();
    private static final Map<IStylePropertySetter, IStylePropertyCallback> stylePropertySetterCallbacks = new HashMap<>();

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
    public static IStylePropertyCallback getStylePropertySetterCallback(IStylePropertySetter setter)
    {
        return stylePropertySetterCallbacks.get(setter);
    }

    public static ImmutableMap<String, Map<String, IStylePropertySetter>> getStylePropertySetters() { return ImmutableMap.copyOf(stylePropertySetters); }
    public static ImmutableMap<IStylePropertySetter, IDeserializer<?>> getStylePropertyDeserializers() { return ImmutableMap.copyOf(stylePropertyDeserializers); }
    public static ImmutableMap<IStylePropertySetter, IStylePropertyCallback> getStylePropertySetterCallbacks() { return ImmutableMap.copyOf(stylePropertySetterCallbacks); }

    public static ImmutableList<Class<? extends Element>> getRegisteredElements() { return ImmutableList.copyOf(registeredElements); }
    public static void addElementPackage(String packageName) { elementPackages.add(packageName); }

    public static void register()
    {
        registeredElements.clear();
        for (String packageName: elementPackages)
            registeredElements.addAll(RegistryUtils.findRegisteredElements(packageName));
        stylePropertySetters.clear();
        stylePropertyDeserializers.clear();
        stylePropertySetterCallbacks.clear();
        for (Class<? extends Element> clazz: registeredElements)
            stylePropertySetters.put(clazz.getName(), RegistryUtils.handleStyleProperties(clazz, stylePropertyDeserializers, stylePropertySetterCallbacks));
    }
}
