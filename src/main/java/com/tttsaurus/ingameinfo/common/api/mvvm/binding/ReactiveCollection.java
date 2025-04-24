package com.tttsaurus.ingameinfo.common.api.mvvm.binding;

import com.tttsaurus.ingameinfo.common.api.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertyGetter;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertySetter;
import com.tttsaurus.ingameinfo.common.impl.gui.registry.ElementRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReactiveCollection
{
    public static class ElementAccessor
    {
        private final Element element;
        protected ElementAccessor(Element element)
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

            for (Element element: group.elements)
            {
                if (element.uid.equals(uid)) list.add(element);
                if (ElementGroup.class.isAssignableFrom(element.getClass()))
                {
                    ElementGroup nextGroup = (ElementGroup)element;
                    list.addAll(getElements(nextGroup, uid));
                }
            }

            return list;
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

    protected ElementGroup group;

    public int size()
    {
        if (group == null)
            throw new IllegalStateException("Internal object is null. The binding probably failed.");
        return group.elements.size();
    }
    public ElementAccessor get(int index)
    {
        if (group == null)
            throw new IllegalStateException("Internal object is null. The binding probably failed.");
        if (index < 0 || index > group.elements.size() - 1)
            throw new IndexOutOfBoundsException("Index " + index + " is invalid as the length of this collection is " + group.elements.size());
        return new ElementAccessor(group.elements.get(index));
    }
    // todo: add & remove
}
