package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.core.gui.style.IStylePropertyGetter;
import com.tttsaurus.ingameinfo.common.core.gui.style.IStylePropertySetter;
import com.tttsaurus.ingameinfo.common.core.gui.registry.ElementRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ElementAccessor
{
    private final Element element;
    public ElementAccessor(Element element)
    {
        this.element = element;
    }

    @SuppressWarnings("all")
    private List<Element> getElements(String uid)
    {
        if (ElementGroup.class.isAssignableFrom(element.getClass()))
            return getElements((ElementGroup)element, uid);
        else if (element.uid.equals(uid))
            return new ArrayList<>(Arrays.asList(element));
        return new ArrayList<>();
    }
    private List<Element> getElements(ElementGroup group, String uid)
    {
        List<Element> list = new ArrayList<>();

        if (group.uid.equals(uid)) list.add(group);

        for (Element element: group.elements)
        {
            if (ElementGroup.class.isAssignableFrom(element.getClass()))
            {
                ElementGroup nextGroup = (ElementGroup)element;
                list.addAll(getElements(nextGroup, uid));
            }
            else if (element.uid.equals(uid)) list.add(element);
        }

        return list;
    }

    public void set(String propertyName, Object value)
    {
        IAction_1Param<Object> action = ElementRegistry.getStylePropertySetterFullCallback(element, propertyName);
        if (action != null) action.invoke(value);
    }
    public void set(String uid, String propertyName, Object value)
    {
        set(uid, propertyName, value, -1);
    }
    public void set(String uid, String propertyName, Object value, int ordinal)
    {
        List<Element> list = getElements(uid);

        int index = 0;
        for (Element item: list)
        {
            if (ordinal != -1 && ordinal != index++) continue;

            IAction_1Param<Object> action = ElementRegistry.getStylePropertySetterFullCallback(item, propertyName);
            if (action != null) action.invoke(value);
        }
    }

    public Object get(String propertyName)
    {
        IStylePropertySetter setter = ElementRegistry.getStylePropertySetter(element.getClass(), propertyName);
        if (setter == null) return null;
        IStylePropertyGetter getter = ElementRegistry.getStylePropertyGetter(setter);
        if (getter == null) return null;
        return getter.get(element);
    }
    public Object get(String uid, String propertyName)
    {
        return get(uid, propertyName, -1);
    }
    public Object get(String uid, String propertyName, int ordinal)
    {
        List<Element> list = getElements(uid);

        Object res = null;

        int index = 0;
        for (Element item: list)
        {
            if (ordinal != -1 && ordinal != index++) continue;

            IStylePropertySetter setter = ElementRegistry.getStylePropertySetter(item.getClass(), propertyName);
            if (setter == null) continue;
            IStylePropertyGetter getter = ElementRegistry.getStylePropertyGetter(setter);
            if (getter == null) continue;
            res = getter.get(item);
        }

        return res;
    }
}