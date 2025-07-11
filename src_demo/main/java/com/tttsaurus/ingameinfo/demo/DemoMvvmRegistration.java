package com.tttsaurus.ingameinfo.demo;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.forgeevent.IgiGuiLifecycleInitEvent;
import com.tttsaurus.ingameinfo.common.core.forgeevent.MvvmRegisterEvent;
import com.tttsaurus.ingameinfo.common.core.gui.IgiGuiManager;
import com.tttsaurus.ingameinfo.common.core.mvvm.registry.MvvmRegistry;
import com.tttsaurus.ingameinfo.demo.eg1.Eg1ViewModel;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public final class DemoMvvmRegistration
{
    @SubscribeEvent
    public static void onMvvmRegister(MvvmRegisterEvent event)
    {
        MvvmRegistry.autoRegister("eg1", Eg1ViewModel.class);

        InGameInfoReborn.LOGGER.info("In-Game Info Reborn MVVM demos registered.");
    }

    @SubscribeEvent
    public static void onIgiGuiLifecycleInit(IgiGuiLifecycleInitEvent event)
    {
        if (event.lifecycleOwner.equals(IgiGuiManager.OWNER_NAME))
        {
            IgiGuiManager.openGui("eg1");
        }
    }
}
