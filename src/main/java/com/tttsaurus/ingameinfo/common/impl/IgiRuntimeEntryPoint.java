package com.tttsaurus.ingameinfo.common.impl;

import com.tttsaurus.ingameinfo.common.core.forgeevent.IgiRuntimeEntryPointEvent;
import com.tttsaurus.ingameinfo.common.core.gui.GuiLifecycleHolder;
import com.tttsaurus.ingameinfo.common.core.mvvm.registry.MvvmRegistry;
import com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify.SpotifyViewModel;
import com.tttsaurus.ingameinfo.common.impl.mvvm.TemplateViewModel;
import com.tttsaurus.ingameinfo.config.IgiSpotifyIntegrationConfig;
import com.tttsaurus.ingameinfo.plugin.crt.impl.CrtMvvm;
import com.tttsaurus.ingameinfo.plugin.crt.impl.CrtViewModel;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class IgiRuntimeEntryPoint
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onIgiRuntimeEntryPoint(IgiRuntimeEntryPointEvent event)
    {
        MvvmRegistry mvvmRegistry = event.runtime.mvvmRegistry;
        GuiLifecycleHolder lifecycleHolder = event.runtime.lifecycleHolder;

        for (String mvvm: CrtMvvm.mvvms)
        {
            mvvmRegistry.manualRegister(mvvm, CrtViewModel.class);
            CrtViewModel crtViewModel = (CrtViewModel)mvvmRegistry.newViewModel(mvvm);
            crtViewModel.runtimeMvvm = mvvm;
            mvvmRegistry.cacheIgiGuiContainer(mvvm, crtViewModel);
        }

        if (IgiSpotifyIntegrationConfig.ENABLE_SPOTIFY_INTEGRATION)
        {
            mvvmRegistry.autoRegister("spotify", SpotifyViewModel.class);
            lifecycleHolder.openGui("spotify", mvvmRegistry);
        }

        mvvmRegistry.autoRegister("template", TemplateViewModel.class);
    }
}
