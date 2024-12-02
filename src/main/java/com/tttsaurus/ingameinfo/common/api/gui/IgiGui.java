package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.impl.gui.IgiGuiLifeCycle;

public final class IgiGui
{
    public static GuiLayoutBuilder getBuilder()
    {
        return new GuiLayoutBuilder();
    }
    public static void openGui(GuiLayoutBuilder builder)
    {
        IgiGuiLifeCycle.openIgiGui(builder.igiGuiContainer);
    }
    public static void closeGui(GuiLayoutBuilder builder)
    {
        IgiGuiLifeCycle.closeIgiGui(builder.igiGuiContainer);
    }

}
