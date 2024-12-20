package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.gui.IgiGuiLifeCycle;
import com.tttsaurus.ingameinfo.common.impl.mvvm.registry.MvvmRegistry;

public final class IgiGuiManager
{
    public static String openGui(String mvvmRegistryName)
    {
        if (!MvvmRegistry.isMvvmRegistered(mvvmRegistryName)) return "";
        ViewModel<?> viewModel = MvvmRegistry.instantiateViewModel(mvvmRegistryName);
        if (viewModel == null) return "";
        GuiLayout guiLayout = viewModel.init();
        guiLayout.igiGuiContainer.viewModel = viewModel;
        return IgiGuiLifeCycle.openIgiGui(guiLayout.igiGuiContainer);
    }

    public static void closeGui(String uuid)
    {
        IgiGuiLifeCycle.closeIgiGui(uuid);
    }
}
