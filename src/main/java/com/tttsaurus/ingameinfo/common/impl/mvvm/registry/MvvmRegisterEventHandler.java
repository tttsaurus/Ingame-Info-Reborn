package com.tttsaurus.ingameinfo.common.impl.mvvm.registry;

import com.tttsaurus.ingameinfo.common.core.forgeevent.MvvmRegisterEvent;
import com.tttsaurus.ingameinfo.common.core.gui.IgiGuiManager;
import com.tttsaurus.ingameinfo.common.core.mvvm.registry.MvvmRegistry;
import com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify.SpotifyViewModel;
import com.tttsaurus.ingameinfo.common.impl.mvvm.TemplateViewModel;
import com.tttsaurus.ingameinfo.config.IgiSpotifyIntegrationConfig;
import com.tttsaurus.ingameinfo.plugin.crt.impl.CrtMvvm;
import com.tttsaurus.ingameinfo.plugin.crt.impl.CrtViewModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class MvvmRegisterEventHandler
{
    @SubscribeEvent
    public static void onMvvmRegister(MvvmRegisterEvent event)
    {
        for (String mvvm: CrtMvvm.mvvms)
        {
            MvvmRegistry.manualRegister(mvvm, CrtViewModel.class);
            CrtViewModel crtViewModel = (CrtViewModel)MvvmRegistry.newViewModel(mvvm);
            crtViewModel.runtimeMvvm = mvvm;
            MvvmRegistry.cacheIgiGuiContainer(mvvm, crtViewModel);
        }

        if (IgiSpotifyIntegrationConfig.ENABLE_SPOTIFY_INTEGRATION)
        {
            MvvmRegistry.autoRegister("spotify", SpotifyViewModel.class);
            IgiGuiManager.openGui("spotify");
        }

        MvvmRegistry.autoRegister("template", TemplateViewModel.class);
    }
}
