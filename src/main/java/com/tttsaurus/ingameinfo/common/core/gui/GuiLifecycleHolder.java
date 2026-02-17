package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.InternalMethods;
import com.tttsaurus.ingameinfo.common.core.input.InputFrameGenerator;
import com.tttsaurus.ingameinfo.common.core.mvvm.registry.MvvmRegistry;

public abstract class GuiLifecycleHolder
{
    private final String holderName;
    private GuiLifecycleProvider lifecycleProvider;

    public final String getHolderName() { return holderName; }

    public final void setLifecycleProvider(GuiLifecycleProvider provider)
    {
        lifecycleProvider = provider;
        InternalMethods.GuiLifecycleProvider$lifecycleHolderName$setter(provider, holderName);
    }
    public final GuiLifecycleProvider getLifecycleProvider() { return lifecycleProvider; }

    protected final InputFrameGenerator inputGen;

    protected GuiLifecycleHolder(String holderName, InputFrameGenerator inputGen)
    {
        this.holderName = holderName;
        this.inputGen = inputGen;
    }

    public final void openGui(String mvvmRegistryName, MvvmRegistry mvvmRegistry)
    {
        if (lifecycleProvider == null) return;
        IgiGuiContainer container = mvvmRegistry.getIgiGuiContainer(mvvmRegistryName);
        if (container == null) return;

        container.prepareGuiOpen();
        lifecycleProvider.openIgiGui(mvvmRegistryName, container);
    }

    public final void closeGui(String mvvmRegistryName, MvvmRegistry mvvmRegistry)
    {
        if (lifecycleProvider == null) return;
        IgiGuiContainer container = mvvmRegistry.getIgiGuiContainer(mvvmRegistryName);
        if (container == null) return;

        container.onGuiClose();
        lifecycleProvider.closeIgiGui(mvvmRegistryName);
    }

    public abstract void update();
}
