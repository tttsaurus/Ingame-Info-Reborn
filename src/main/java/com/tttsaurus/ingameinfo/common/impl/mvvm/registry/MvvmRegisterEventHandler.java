package com.tttsaurus.ingameinfo.common.impl.mvvm.registry;

import com.tttsaurus.ingameinfo.common.api.event.MvvmRegisterEvent;
import com.tttsaurus.ingameinfo.plugin.crt.impl.CrtViewModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class MvvmRegisterEventHandler
{
    @SubscribeEvent
    public static void onMvvmRegister(MvvmRegisterEvent event)
    {
        MvvmRegistry.register("crt", CrtViewModel.class);

        //MvvmRegistry.register("test", TestViewModel.class);
    }
}
