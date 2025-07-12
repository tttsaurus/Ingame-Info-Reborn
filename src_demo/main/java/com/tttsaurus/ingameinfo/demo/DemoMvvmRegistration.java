package com.tttsaurus.ingameinfo.demo;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.forgeevent.IgiRuntimeEntryPointEvent;
import com.tttsaurus.ingameinfo.demo.eg1.Eg1ViewModel;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public final class DemoMvvmRegistration
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onIgiRuntimeEntryPoint(IgiRuntimeEntryPointEvent event)
    {
        event.runtime.unifiedEntry
                .registerMvvm("eg1", Eg1ViewModel.class)
                .openGuiOnStartup("eg1");

        InGameInfoReborn.LOGGER.info("In-Game Info Reborn MVVM demos registered.");
    }
}
