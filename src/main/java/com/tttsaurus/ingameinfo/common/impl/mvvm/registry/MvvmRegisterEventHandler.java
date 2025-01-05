package com.tttsaurus.ingameinfo.common.impl.mvvm.registry;

import com.tttsaurus.ingameinfo.common.api.event.MvvmRegisterEvent;
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
            MvvmRegistry.setIgiGuiContainer(mvvm, crtViewModel);
        }

        //MvvmRegistry.autoRegister("test", TestViewModel.class);
    }
}
