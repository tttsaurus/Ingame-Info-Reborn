package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.input.InputFrameGenerator;
import com.tttsaurus.ingameinfo.common.core.mvvm.registry.MvvmRegistry;

public abstract class GuiLifecycleHolder
{
    private final String holderName;
    protected GuiLifecycleProvider lifecycleProvider;

    public String getHolderName() { return holderName; }
    public void setLifecycleProvider(GuiLifecycleProvider provider) { lifecycleProvider = provider; }
    public GuiLifecycleProvider getLifecycleProvider() { return lifecycleProvider; }

    protected final InputFrameGenerator inputGen;

    protected GuiLifecycleHolder(String holderName, InputFrameGenerator inputGen)
    {
        this.holderName = holderName;
        this.inputGen = inputGen;
    }

    public void openGui(String mvvmRegistryName, MvvmRegistry mvvmRegistry)
    {
        if (lifecycleProvider == null) return;
        IgiGuiContainer igiGuiContainer = mvvmRegistry.getIgiGuiContainer(mvvmRegistryName);
        if (igiGuiContainer == null) return;

        lifecycleProvider.openIgiGui(mvvmRegistryName, igiGuiContainer);
    }

    public void closeGui(String mvvmRegistryName)
    {
        if (lifecycleProvider == null) return;

        lifecycleProvider.closeIgiGui(mvvmRegistryName);
    }

    public abstract void update();
}
