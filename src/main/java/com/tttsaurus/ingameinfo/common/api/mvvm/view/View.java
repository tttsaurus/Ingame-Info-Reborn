package com.tttsaurus.ingameinfo.common.api.mvvm.view;

import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;

// only do one inheritance; don't do nested inheritance
public abstract class View
{
    private MainGroup mainGroup;

    public abstract void init(GuiLayout guiLayout);
}
