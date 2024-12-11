package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.impl.gui.IgiGuiLifeCycle;

public final class IgiGui
{
    public static GuiLayout getBuilder()
    {
        return new GuiLayout();
    }
    public static void openGui(GuiLayout builder)
    {
        IgiGuiLifeCycle.openIgiGui(builder.igiGuiContainer);
    }
    public static void closeGui(GuiLayout builder)
    {
        IgiGuiLifeCycle.closeIgiGui(builder.igiGuiContainer);
    }
}
