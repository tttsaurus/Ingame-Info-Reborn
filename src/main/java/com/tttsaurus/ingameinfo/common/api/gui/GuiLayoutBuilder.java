package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.api.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.HorizontalGroup;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.SizedGroup;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.VerticalGroup;
import java.util.ArrayList;
import java.util.List;

public class GuiLayoutBuilder
{
    protected final IgiGuiContainer igiGuiContainer;
    private final MainGroup mainGroup;

    protected GuiLayoutBuilder()
    {
        igiGuiContainer = new IgiGuiContainer();
        mainGroup = igiGuiContainer.mainGroup;
    }

    public GuiLayoutBuilder setDebug(boolean debug)
    {
        igiGuiContainer.debug = debug;
        return this;
    }
    public GuiLayoutBuilder setExitKeyForFocusedGui(int keycode)
    {
        igiGuiContainer.exitKeyForFocusedGui = keycode;
        return this;
    }
    public GuiLayoutBuilder setFocused(boolean focused)
    {
        igiGuiContainer.isFocused = focused;
        return this;
    }
    public GuiLayoutBuilder setHasFocusBackground(boolean hasFocusBackground)
    {
        igiGuiContainer.hasFocusBackground = hasFocusBackground;
        return this;
    }
    public GuiLayoutBuilder setBackgroundColor(int color)
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
    public GuiLayoutBuilder endGroup()
    {
        groupLayer--;
        if (groupLayer - 1 < 0)
            mainGroup.add(groupBuffer.get(groupLayer));
        else
            groupBuffer.get(groupLayer - 1).add(groupBuffer.get(groupLayer));
        return this;
    }

    public GuiLayoutBuilder startHorizontalGroup()
    {
        startGroup(new HorizontalGroup());
        return this;
    }
    public GuiLayoutBuilder startVerticalGroup()
    {
        startGroup(new VerticalGroup());
        return this;
    }
    public GuiLayoutBuilder startSizedGroup(float width, float height)
    {
        startGroup(new SizedGroup(width, height));
        return this;
    }

    public GuiLayoutBuilder addElement(Element element)
    {
        if (groupLayer - 1 < 0)
            mainGroup.add(element);
        else
            groupBuffer.get(groupLayer - 1).add(element);
        return this;
    }
}
