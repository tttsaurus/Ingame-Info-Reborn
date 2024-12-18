package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.impl.gui.IgiGuiLifeCycle;

public final class IgiGui
{
    public static GuiLayout getBuilder()
    {
        return new GuiLayout();
    }
    public static String openGui(GuiLayout builder)
    {
        return IgiGuiLifeCycle.openIgiGui(builder.igiGuiContainer);
    }
    public static void closeGui(String uuid)
    {
        IgiGuiLifeCycle.closeIgiGui(uuid);
    }
}
