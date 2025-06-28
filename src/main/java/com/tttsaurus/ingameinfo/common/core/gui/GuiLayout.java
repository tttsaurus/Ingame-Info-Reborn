package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.core.item.GhostableItem;
import com.tttsaurus.ingameinfo.common.core.gui.layout.MainGroup;
import com.tttsaurus.ingameinfo.common.core.gui.registry.ElementRegistry;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public final class GuiLayout
{
    private IgiGuiContainer igiGuiContainer;
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
    public GuiLayout addHeldItemWhitelist(GhostableItem item)
    {
        igiGuiContainer.heldItemWhitelist.add(item);
        return this;
    }
    public GuiLayout addHeldItemBlacklist(GhostableItem item)
    {
        igiGuiContainer.heldItemBlacklist.add(item);
        return this;
    }
    public GuiLayout setThemeName(String themeName)
    {
        igiGuiContainer.themeName = themeName;
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
        {
            mainGroup.add(groupBuffer.get(groupLayer));
            groupBuffer.remove(groupBuffer.size() - 1);
        }
        else
        {
            groupBuffer.get(groupLayer - 1).add(groupBuffer.get(groupLayer));
            groupBuffer.remove(groupBuffer.size() - 1);
        }
        return this;
    }

    public GuiLayout startGroup(ElementGroup group)
    {
        startGroupInternal(group);
        return this;
    }
    public GuiLayout startGroup(ElementGroup group, List<ElementProperty> properties)
    {
        injectStyleProperties(group, properties);
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
    public GuiLayout addElement(Element element, List<ElementProperty> properties)
    {
        injectStyleProperties(element, properties);
        addElementInternal(element);
        return this;
    }

    // todo: add inject priority
    private void injectStyleProperties(Element element, List<ElementProperty> properties)
    {
        for (ElementProperty style: properties)
            element.setStyleProperty(style.name, style.value);
    }
}
