package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.api.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.api.gui.style.ISetStyleProperty;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.HorizontalGroup;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.SizedGroup;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.VerticalGroup;
import com.tttsaurus.ingameinfo.common.impl.gui.registry.ElementRegistry;
import com.tttsaurus.ingameinfo.common.impl.serialization.RawElementStylesDeserializer;
import net.minecraft.util.Tuple;
import java.util.ArrayList;
import java.util.List;

public class GuiLayout
{
    protected final IgiGuiContainer igiGuiContainer;
    private final MainGroup mainGroup;

    protected GuiLayout()
    {
        igiGuiContainer = new IgiGuiContainer();
        mainGroup = igiGuiContainer.mainGroup;
    }

    public GuiLayout setDebug(boolean debug)
    {
        igiGuiContainer.debug = debug;
        return this;
    }
    public GuiLayout setExitKeyForFocusedGui(int keycode)
    {
        igiGuiContainer.exitKeyForFocusedGui = keycode;
        return this;
    }
    public GuiLayout setFocused(boolean focused)
    {
        igiGuiContainer.isFocused = focused;
        return this;
    }
    public GuiLayout setHasFocusBackground(boolean hasFocusBackground)
    {
        igiGuiContainer.hasFocusBackground = hasFocusBackground;
        return this;
    }
    public GuiLayout setBackgroundColor(int color)
    {
        igiGuiContainer.backgroundColor = color;
        return this;
    }

    private ElementGroup group;
    private int groupLayer = 0;
    private List<ElementGroup> groupBuffer = new ArrayList<>();

    private void startGroup(ElementGroup group)
    {
        groupBuffer.add(group);
        groupLayer++;
    }
    public GuiLayout endGroup()
    {
        groupLayer--;
        if (groupLayer - 1 < 0)
            mainGroup.add(groupBuffer.get(groupLayer));
        else
            groupBuffer.get(groupLayer - 1).add(groupBuffer.get(groupLayer));
        return this;
    }

    public GuiLayout startHorizontalGroup()
    {
        startGroup(new HorizontalGroup());
        return this;
    }
    public GuiLayout startVerticalGroup()
    {
        startGroup(new VerticalGroup());
        return this;
    }
    public GuiLayout startSizedGroup(float width, float height)
    {
        startGroup(new SizedGroup(width, height));
        return this;
    }

    private void addElement(Element element)
    {
        if (groupLayer - 1 < 0)
            mainGroup.add(element);
        else
            groupBuffer.get(groupLayer - 1).add(element);
    }
    public GuiLayout addElement(Element element, ElementStyle... styles)
    {
        for (ElementStyle style: styles)
        {
            ISetStyleProperty setter = ElementRegistry.getStylePropertySetter(element.getClass(), style.name);
            if (setter != null) setter.set(element, style.value);
        }

        addElement(element);
        return this;
    }
    public GuiLayout addElement(Element element, String rawStyles)
    {
        RawElementStylesDeserializer deserializer = new RawElementStylesDeserializer();
        List<Tuple<String, String>> list = deserializer.deserialize(rawStyles, "json");
        for (Tuple<String, String> pair: list)
        {
            //InGameInfoReborn.LOGGER.info(pair.getFirst() + ":" + pair.getSecond());
            ISetStyleProperty setter = ElementRegistry.getStylePropertySetter(element.getClass(), pair.getFirst());
            if (setter != null)
            {
                IDeserializer<?> stylePropertyDeserializer = ElementRegistry.getStylePropertyDeserializer(setter);
                if (stylePropertyDeserializer != null)
                {
                    Object obj = stylePropertyDeserializer.deserialize(pair.getSecond(), "json");
                    if (obj != null) setter.set(element, obj);
                }
            }
        }

        addElement(element);
        return this;
    }
}
