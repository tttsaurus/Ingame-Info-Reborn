package com.tttsaurus.ingameinfo.common.impl.gui.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegistryUtils;
import com.tttsaurus.ingameinfo.common.api.gui.style.ISetStyleProperty;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;
import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings("all")
public final class ElementRegistry
{
    private static final Map<String, Map<String, ISetStyleProperty>> stylePropertySetters = new HashMap<>();
    private static final Map<ISetStyleProperty, IDeserializer<?>> stylePropertyDeserializers = new HashMap<>();

    private static final List<Class<? extends Element>> registeredElements = new ArrayList<>();
    private static final List<String> elementPackages = new ArrayList<>(Arrays.asList(
            "com.tttsaurus.ingameinfo.common.api.gui",
            "com.tttsaurus.ingameinfo.common.impl.gui.control",
            "com.tttsaurus.ingameinfo.common.impl.gui.layout"
    ));

    @Nullable
    public static ISetStyleProperty getStylePropertySetter(Class<? extends Element> clazz, String name)
    {
        if (stylePropertySetters.containsKey(clazz.getName()))
        {
            Map<String, ISetStyleProperty> map = stylePropertySetters.get(clazz.getName());
            if (map.containsKey(name)) return map.get(name);
        }
        if (Element.class.isAssignableFrom(clazz.getSuperclass()))
            return getStylePropertySetter((Class<? extends Element>)clazz.getSuperclass(), name);
        else
            return null;
    }
    @Nullable
    public static IDeserializer<?> getStylePropertyDeserializer(ISetStyleProperty setter)
    {
        return stylePropertyDeserializers.get(setter);
    }

    public static ImmutableMap<String, Map<String, ISetStyleProperty>> getStylePropertySetters() { return ImmutableMap.copyOf(stylePropertySetters); }
    public static ImmutableMap<ISetStyleProperty, IDeserializer<?>> getStylePropertyDeserializers() { return ImmutableMap.copyOf(stylePropertyDeserializers); }
    public static ImmutableList<Class<? extends Element>> getRegisteredElements() { return ImmutableList.copyOf(registeredElements); }
    public static void addElementPackage(String packageName) { elementPackages.add(packageName); }

    public static void register()
    {
        registeredElements.clear();
        for (String packageName: elementPackages)
            registeredElements.addAll(RegistryUtils.findRegisteredElements(packageName));
        stylePropertySetters.clear();
        stylePropertyDeserializers.clear();
        for (Class<? extends Element> clazz: registeredElements)
            stylePropertySetters.put(clazz.getName(), RegistryUtils.findStyleProperties(clazz, stylePropertyDeserializers));
    }
}
