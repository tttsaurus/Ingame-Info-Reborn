package com.tttsaurus.ingameinfo.common.api.mvvm.view;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.api.internal.InternalMethods;
import com.tttsaurus.ingameinfo.common.api.serialization.ixml.RawIxmlUtils;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import com.tttsaurus.ingameinfo.common.impl.serialization.GuiLayoutDeserializer;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// only do one inheritance; don't do nested inheritance
@SuppressWarnings("all")
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

    public String getDefaultIxml() { return ""; }

    // searching the file under ./config/ingameinfo/
    // and .ixml is the suffix
    public abstract String getIxmlFileName();

    private GuiLayout init()
    {
        try
        {
            File directory = new File("config/ingameinfo");
            if (!directory.exists()) directory.mkdirs();

            String filePath = "config/ingameinfo/" + getIxmlFileName() + ".ixml";
            File testFile = new File(filePath);
            boolean writeDefault = !testFile.exists();

            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            StringBuilder builder = new StringBuilder();

            if (writeDefault)
            {
                file.write(getDefaultIxml().getBytes(StandardCharsets.UTF_8));
                builder.append(getDefaultIxml().replace("\n", ""));
            }
            else
            {
                String line = file.readLine();
                while (line != null)
                {
                    builder.append(line);
                    line = file.readLine();
                }
            }

            file.close();

            return (new GuiLayoutDeserializer()).deserialize(RawIxmlUtils.deleteComments(builder.toString()), "ixml");
        }
        catch (Exception ignored) { return InternalMethods.instance.GuiLayout$constructor.invoke(); }
    }
}
