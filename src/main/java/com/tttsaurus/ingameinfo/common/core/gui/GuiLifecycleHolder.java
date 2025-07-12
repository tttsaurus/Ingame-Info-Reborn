package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.InternalMethods;
import com.tttsaurus.ingameinfo.common.core.input.InputFrameGenerator;
import com.tttsaurus.ingameinfo.common.core.mvvm.registry.MvvmRegistry;

public abstract class GuiLifecycleHolder
{
    private final String holderName;
    protected GuiLifecycleProvider lifecycleProvider;

    public String getHolderName() { return holderName; }

    public void setLifecycleProvider(GuiLifecycleProvider provider)
    {
        lifecycleProvider = provider;
        InternalMethods.instance.GuiLifecycleProvider$lifecycleHolderName$setter.invoke(provider, holderName);
    }
    public GuiLifecycleProvider getLifecycleProvider() { return lifecycleProvider; }

    protected final InputFrameGenerator inputGen;

    protected GuiLifecycleHolder(String holderName, InputFrameGenerator inputGen)
    {
        this.holderName = holderName;
        this.inputGen = inputGen;
    }

    public final void openGui(String mvvmRegistryName, MvvmRegistry mvvmRegistry)
    {
        if (lifecycleProvider == null) return;
        IgiGuiContainer igiGuiContainer = mvvmRegistry.getIgiGuiContainer(mvvmRegistryName);
        if (igiGuiContainer == null) return;

        lifecycleProvider.openIgiGui(mvvmRegistryName, igiGuiContainer);
    }

    public final void closeGui(String mvvmRegistryName)
    {
        if (lifecycleProvider == null) return;

        lifecycleProvider.closeIgiGui(mvvmRegistryName);
    }

    public abstract void update();
}
