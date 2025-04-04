package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.impl.gui.IgiGuiLifeCycle;
import com.tttsaurus.ingameinfo.common.impl.mvvm.registry.MvvmRegistry;

public final class IgiGuiManager
{
    public static void openGui(String mvvmRegistryName)
    {
        IgiGuiContainer igiGuiContainer = MvvmRegistry.getIgiGuiContainer(mvvmRegistryName);
        if (igiGuiContainer == null) return;
        IgiGuiLifeCycle.openIgiGui(mvvmRegistryName, igiGuiContainer);
    }

    public static void closeGui(String uuid)
    {
        IgiGuiLifeCycle.closeIgiGui(uuid);
    }
}
