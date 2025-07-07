package com.tttsaurus.ingameinfo.common.core.mvvm.view;

import com.tttsaurus.ingameinfo.common.core.commonutils.FileUtils;
import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.core.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.core.InternalMethods;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.RenderDecorator;
import com.tttsaurus.ingameinfo.common.core.serialization.ixml.RawIxmlUtils;
import com.tttsaurus.ingameinfo.common.core.gui.layout.MainGroup;
import com.tttsaurus.ingameinfo.common.impl.serialization.GuiLayoutDeserializer;
import javax.annotation.Nullable;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// only do one inheritance; don't do nested inheritance
@SuppressWarnings("all")
public abstract class View
{
    // injected by VvmBinding.init()
    private RenderDecorator renderDecorator;

    public void initRenderDecorator(RenderDecorator renderDecorator) { }

    public RenderDecorator getRenderDecorator()
    {
        return renderDecorator;
    }

    // injected by VvmBinding.init()
    // alias of IgiGuiContainer.mainGroup
    private MainGroup mainGroup = null;

    public final List<Element> getElements(String uid)
    {
        return getElements(mainGroup, uid);
    }
    private final List<Element> getElements(ElementGroup group, String uid)
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

    public final void refresh(IgiGuiContainer container)
    {
        if (mainGroup == null) return;

        GuiLayout guiLayout = init(container);
        MainGroup mainGroup = InternalMethods.instance.GuiLayout$mainGroup$getter.invoke(guiLayout);

        // doesnt break the alias relationship between IgiGuiContainer.mainGroup and View.mainGroup
        this.mainGroup.elements.clear();
        for (Element element: mainGroup.elements)
            this.mainGroup.add(element);
    }

    public String getDefaultIxml() { return ""; }

    // creating the file under ./config/ingameinfo/ixml/
    // and .ixml is the suffix
    public abstract String getIxmlFileName();

    private final GuiLayout init(@Nullable IgiGuiContainer container)
    {
        try
        {
            File testFile = FileUtils.getFile(getIxmlFileName() + ".ixml", "ixml");
            boolean writeDefault = !testFile.exists();

            RandomAccessFile file = new RandomAccessFile(testFile, "rw");
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

            return (new GuiLayoutDeserializer(container)).deserialize(RawIxmlUtils.deleteComments(builder.toString()));
        }
        catch (Exception ignored) { return InternalMethods.instance.GuiLayout$constructor.invoke(); }
    }
}
