package com.tttsaurus.ingameinfo.common.core.gui.registry;

import com.google.common.collect.ImmutableMap;
import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.core.gui.property.lerp.ILerpablePropertyGetter;
import com.tttsaurus.ingameinfo.common.core.gui.property.lerp.LerpTarget;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.*;
import com.tttsaurus.ingameinfo.common.core.serialization.IDeserializer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings("all")
public final class ElementRegistry
{
    // key: element class name (not the simple name)
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

    // not frequently used
    //<editor-fold desc="style property getters">
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
    //</editor-fold>

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

    @Nullable
    public static IAction_1Param<Object> getStylePropertySetterFullCallback(@Nonnull Element element, String propertyName)
    {
        IStylePropertySetter setter = ElementRegistry.getStylePropertySetter(element.getClass(), propertyName);
        if (setter == null)
            return null;
        else
            return ElementRegistry.getStylePropertySetterWithCallbacksHandled(
                    setter,
                    element,
                    ElementRegistry.getStylePropertySetterCallbackPre(setter),
                    ElementRegistry.getStylePropertySetterCallbackPost(setter));
    }
    private static IAction_1Param<Object> getStylePropertySetterWithCallbacksHandled(
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

    //<editor-fold desc="style property map getters">
    public static ImmutableMap<String, Map<String, IStylePropertySetter>> getStylePropertySetters() { return ImmutableMap.copyOf(stylePropertySetters); }
    public static ImmutableMap<IStylePropertySetter, IDeserializer<?>> getStylePropertyDeserializers() { return ImmutableMap.copyOf(stylePropertyDeserializers); }
    public static ImmutableMap<IStylePropertySetter, IStylePropertyCallbackPre> getStylePropertySetterCallbacksPre() { return ImmutableMap.copyOf(stylePropertySetterCallbacksPre); }
    public static ImmutableMap<IStylePropertySetter, IStylePropertyCallbackPost> getStylePropertySetterCallbacksPost() { return ImmutableMap.copyOf(stylePropertySetterCallbacksPost); }
    public static ImmutableMap<IStylePropertySetter, Class<?>> getStylePropertyClasses() { return ImmutableMap.copyOf(stylePropertyClasses); }
    //</editor-fold>

    public static List<Class<? extends Element>> getRegisteredElements()
    {
        List<Class<? extends Element>> list = new ArrayList<>(registeredElements.values());
        String myPackage = "com.tttsaurus.ingameinfo";
        list.sort(Comparator.comparing((Class<? extends Element> c) -> !c.getName().startsWith(myPackage)).thenComparing(Class::getName));
        return list;
    }

    public static List<Class<? extends Element>> getConstructableElements()
    {
        List<Class<? extends Element>> list = new ArrayList<>();
        for (Class<? extends Element> clazz: getRegisteredElements())
        {
            RegisterElement annotation = elementAnnotations.get(clazz);
            if (annotation != null)
                if (annotation.constructable())
                    list.add(clazz);
        }
        return list;
    }

    // key: element class name (not the simple name)
    private static final Map<String, Map<ILerpablePropertyGetter, LerpTarget>> lerpablePropertyGetters = new HashMap<>();

    public static ImmutableMap<String, Map<ILerpablePropertyGetter, LerpTarget>> getLerpablePropertyGetters() { return ImmutableMap.copyOf(lerpablePropertyGetters); }

    public static List<ILerpablePropertyGetter> getLerpablePropertyGetters(Class<? extends Element> clazz)
    {
        Map<ILerpablePropertyGetter, LerpTarget> map = lerpablePropertyGetters.get(clazz.getName());
        if (map == null) return new ArrayList<>();

        if (Element.class.isAssignableFrom(clazz.getSuperclass()))
        {
            List<ILerpablePropertyGetter> list = getLerpablePropertyGetters((Class<? extends Element>)clazz.getSuperclass());
            list.addAll(new ArrayList<>(map.keySet()));
            return list;
        }
        else
            return new ArrayList<>(map.keySet());
    }

    @Nullable
    public static LerpTarget getLerpTarget(Class<? extends Element> clazz, ILerpablePropertyGetter getter)
    {
        if (lerpablePropertyGetters.containsKey(clazz.getName()))
        {
            Map<ILerpablePropertyGetter, LerpTarget> map = lerpablePropertyGetters.get(clazz.getName());
            if (map.containsKey(getter)) return map.get(getter);
        }
        if (Element.class.isAssignableFrom(clazz.getSuperclass()))
            return getLerpTarget((Class<? extends Element>)clazz.getSuperclass(), getter);
        else
            return null;
    }

    public static void register()
    {
        registeredElements.clear();
        elementAnnotations.clear();

        registeredElements.putAll(RegistryUtils.handleRegisteredElements(elementAnnotations));

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

        lerpablePropertyGetters.clear();

        for (Class<? extends Element> clazz: registeredElements.values())
            lerpablePropertyGetters.put(clazz.getName(), RegistryUtils.handleLerpableProperties(clazz));
    }
}
