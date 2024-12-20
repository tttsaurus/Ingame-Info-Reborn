package com.tttsaurus.ingameinfo.common.api.mvvm.view;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import java.util.ArrayList;
import java.util.List;

// only do one inheritance; don't do nested inheritance
public abstract class View
{
    public boolean test() { return mainGroup != null; }

    private MainGroup mainGroup = null;

    public List<Element> getElements(String uid)
    {
        return getElements(mainGroup, uid);
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

    public abstract void init(GuiLayout guiLayout);
}
