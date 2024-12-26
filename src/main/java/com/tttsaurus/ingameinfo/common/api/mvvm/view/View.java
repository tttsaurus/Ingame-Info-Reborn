package com.tttsaurus.ingameinfo.common.api.mvvm.view;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.api.internal.InternalMethods;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import com.tttsaurus.ingameinfo.common.impl.serialization.GuiLayoutDeserializer;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

// only do one inheritance; don't do nested inheritance
public abstract class View
{
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

    public abstract String getIxmlFileName();

    private GuiLayout init()
    {
        try
        {
            RandomAccessFile file = new RandomAccessFile("config/ingameinfo/" + getIxmlFileName(), "rw");

            StringBuilder builder = new StringBuilder();

            String line = file.readLine();
            while (line != null)
            {
                builder.append(line);
                line = file.readLine();
            }

            GuiLayoutDeserializer deserializer = new GuiLayoutDeserializer();
            GuiLayout guiLayout = deserializer.deserialize(builder.toString(), "ixml");

            file.close();

            return guiLayout;
        }
        catch (Exception ignored) { return InternalMethods.instance.GuiLayout$constructor.invoke(); }
    }
}
