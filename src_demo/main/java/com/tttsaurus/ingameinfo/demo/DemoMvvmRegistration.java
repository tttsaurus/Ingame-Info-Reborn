package com.tttsaurus.ingameinfo.demo;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.forgeevent.IgiGuiInitEvent;
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

        InGameInfoReborn.LOGGER.info("In-Game Info Reborn mvvm demos registered.");
    }

    @SubscribeEvent
    public static void onIgiGuiInit(IgiGuiInitEvent event)
    {
        //IgiGuiManager.openGui("eg1");
    }
}
