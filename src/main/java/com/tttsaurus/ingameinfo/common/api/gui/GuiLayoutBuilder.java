package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;

public class GuiLayoutBuilder
{
    protected final IgiGuiContainer igiGuiContainer;
    private final MainGroup mainGroup;

    protected GuiLayoutBuilder()
    {
        igiGuiContainer = new IgiGuiContainer();
        mainGroup = igiGuiContainer.mainGroup;
    }
}
