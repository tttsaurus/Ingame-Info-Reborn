package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.mvvm.registry.MvvmRegistry;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class IgiGuiManager
{
    private static GuiLifecycleProvider lifecycleProvider;
    public static void setLifecycleProvider(GuiLifecycleProvider provider) { lifecycleProvider = provider; }
    public static GuiLifecycleProvider getLifecycleProvider() { return lifecycleProvider; }

    public static void openGui(String mvvmRegistryName)
    {
        if (lifecycleProvider == null) return;
        IgiGuiContainer igiGuiContainer = MvvmRegistry.getIgiGuiContainer(mvvmRegistryName);
        if (igiGuiContainer == null) return;

        lifecycleProvider.openIgiGui(mvvmRegistryName, igiGuiContainer);
    }

    public static void closeGui(String uuid)
    {
        if (lifecycleProvider == null) return;

        lifecycleProvider.closeIgiGui(uuid);
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        if (lifecycleProvider == null) return;

        lifecycleProvider.update();
    }
}
