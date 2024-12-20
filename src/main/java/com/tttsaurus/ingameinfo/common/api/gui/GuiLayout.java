package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.api.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertyCallback;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertySetter;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import com.tttsaurus.ingameinfo.common.impl.gui.registry.ElementRegistry;
import com.tttsaurus.ingameinfo.common.impl.serialization.ElementStylesDeserializer;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class GuiLayout
{
    protected final IgiGuiContainer igiGuiContainer;
    private MainGroup mainGroup;

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
    public GuiLayout setHeldItemWhitelist(boolean flag)
    {
        igiGuiContainer.useHeldItemWhitelist = flag;
        return this;
    }
    public GuiLayout setHeldItemBlacklist(boolean flag)
    {
        igiGuiContainer.useHeldItemBlacklist = flag;
        return this;
    }
    public GuiLayout addHeldItemWhitelist(ItemStack itemStack)
    {
        igiGuiContainer.heldItemWhitelist.add(itemStack);
        return this;
    }
    public GuiLayout addHeldItemBlacklist(ItemStack itemStack)
    {
        igiGuiContainer.heldItemBlacklist.add(itemStack);
        return this;
    }

    private int groupLayer = 0;
    private final List<ElementGroup> groupBuffer = new ArrayList<>();

    private void startGroupInternal(ElementGroup group)
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

    public GuiLayout startGroup(ElementGroup group)
    {
        startGroupInternal(group);
        return this;
    }
    public GuiLayout startGroup(ElementGroup group, String rawStyles)
    {
        injectStyles(group, (new ElementStylesDeserializer(group.getClass())).deserialize(rawStyles, "json"));
        startGroupInternal(group);
        return this;
    }

    private void addElementInternal(Element element)
    {
        if (groupLayer - 1 < 0)
            mainGroup.add(element);
        else
            groupBuffer.get(groupLayer - 1).add(element);
    }
    public GuiLayout addElement(Element element)
    {
        addElementInternal(element);
        return this;
    }
    public GuiLayout addElement(Element element, String rawStyles)
    {
        injectStyles(element, (new ElementStylesDeserializer(element.getClass())).deserialize(rawStyles, "json"));
        addElementInternal(element);
        return this;
    }

    private void injectStyles(Element element, List<ElementStyle> styles)
    {
        for (ElementStyle style: styles)
        {
            IStylePropertySetter setter = ElementRegistry.getStylePropertySetter(element.getClass(), style.name);
            if (setter != null)
            {
                setter.set(element, style.value);
                IStylePropertyCallback callback = ElementRegistry.getStylePropertySetterCallback(setter);
                if (callback != null) callback.invoke(element);
            }
        }
    }
}
