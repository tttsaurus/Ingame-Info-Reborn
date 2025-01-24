package com.tttsaurus.ingameinfo.common.impl.gui.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegistryUtils;
import com.tttsaurus.ingameinfo.common.api.gui.style.*;
import com.tttsaurus.ingameinfo.common.api.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings("all")
public final class ElementRegistry
{
    // key: element class name
    private static final Map<String, Map<String, IStylePropertySetter>> stylePropertySetters = new HashMap<>();

    // IStylePropertySetter is the primary key here
    private static final Map<IStylePropertySetter, IDeserializer<?>> stylePropertyDeserializers = new HashMap<>();
    private static final Map<IStylePropertySetter, IStylePropertyCallbackPre> stylePropertySetterCallbacksPre = new HashMap<>();
    private static final Map<IStylePropertySetter, IStylePropertyCallbackPost> stylePropertySetterCallbacksPost = new HashMap<>();
    private static final Map<IStylePropertySetter, Class<?>> stylePropertyClasses = new HashMap<>();
    private static final Map<IStylePropertySetter, IStylePropertyGetter> stylePropertyGetters = new HashMap<>();

    // key: element class simple name
    private static final Map<String, Class<? extends Element>> registeredElements = new HashMap<>();
    private static final Map<Class<? extends Element>, RegisterElement> elementAnnotations = new HashMap<>();

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
    public static IStylePropertyCallbackPre getStylePropertySetterCallbackPre(IStylePropertySetter setter)
    {
        return stylePropertySetterCallbacksPre.get(setter);
    }
    @Nullable
    public static IStylePropertyCallbackPost getStylePropertySetterCallbackPost(IStylePropertySetter setter)
    {
        return stylePropertySetterCallbacksPost.get(setter);
    }
    @Nullable
    public static Class<?> getStylePropertyClass(IStylePropertySetter setter)
    {
        return stylePropertyClasses.get(setter);
    }
    @Nullable
    public static IStylePropertyGetter getStylePropertyGetter(IStylePropertySetter setter)
    {
        return stylePropertyGetters.get(setter);
    }
    @Nullable
    public static Element newElement(String name)
    {
        Class<? extends Element> clazz = registeredElements.get(name);
        if (clazz == null) return null;
        RegisterElement annotation = elementAnnotations.get(clazz);
        if (annotation == null) return null;
        if (!annotation.constructable()) return null;
        try
        {
            return clazz.newInstance();
        }
        catch (Exception e) { return null; }
    }

    public static IAction_1Param<Object> getStylePropertySetterWithCallbacksHandled(
            @Nonnull IStylePropertySetter setter,
            @Nonnull Element element,
            IStylePropertyCallbackPre setterCallbackPre,
            IStylePropertyCallbackPost setterCallbackPost)
    {
        return (value) ->
        {
            CallbackInfo callbackInfo = new CallbackInfo();
            if (setterCallbackPre != null) setterCallbackPre.invoke(element, value, callbackInfo);
            if (!callbackInfo.cancel)
            {
                setter.set(element, value);
                if (setterCallbackPost != null) setterCallbackPost.invoke(element, value);
            }
        };
    }

    public static ImmutableMap<String, Map<String, IStylePropertySetter>> getStylePropertySetters() { return ImmutableMap.copyOf(stylePropertySetters); }
    public static ImmutableMap<IStylePropertySetter, IDeserializer<?>> getStylePropertyDeserializers() { return ImmutableMap.copyOf(stylePropertyDeserializers); }
    public static ImmutableMap<IStylePropertySetter, IStylePropertyCallbackPre> getStylePropertySetterCallbacksPre() { return ImmutableMap.copyOf(stylePropertySetterCallbacksPre); }
    public static ImmutableMap<IStylePropertySetter, IStylePropertyCallbackPost> getStylePropertySetterCallbacksPost() { return ImmutableMap.copyOf(stylePropertySetterCallbacksPost); }
    public static ImmutableMap<IStylePropertySetter, Class<?>> getStylePropertyClasses() { return ImmutableMap.copyOf(stylePropertyClasses); }

    public static ImmutableList<Class<? extends Element>> getRegisteredElements() { return ImmutableList.copyOf(registeredElements.values()); }
    public static List<Class<? extends Element>> getConstructableElements()
    {
        List<Class<? extends Element>> list = new ArrayList<>();
        for (Class<? extends Element> clazz: registeredElements.values())
        {
            RegisterElement annotation = elementAnnotations.get(clazz);
            if (annotation != null)
                if (annotation.constructable())
                    list.add(clazz);
        }
        return list;
    }

    public static void addElementPackage(String packageName) { elementPackages.add(packageName); }

    public static void register()
    {
        registeredElements.clear();
        elementAnnotations.clear();

        registeredElements.putAll(RegistryUtils.handleRegisteredElements(elementPackages, elementAnnotations));

        stylePropertySetters.clear();
        stylePropertyDeserializers.clear();
        stylePropertySetterCallbacksPre.clear();
        stylePropertySetterCallbacksPost.clear();
        stylePropertyClasses.clear();
        stylePropertyGetters.clear();

        for (Class<? extends Element> clazz: registeredElements.values())
            stylePropertySetters.put(clazz.getName(), RegistryUtils.handleStyleProperties(
                    clazz,
                    stylePropertyDeserializers,
                    stylePropertySetterCallbacksPre,
                    stylePropertySetterCallbacksPost,
                    stylePropertyClasses,
                    stylePropertyGetters));
    }
}
