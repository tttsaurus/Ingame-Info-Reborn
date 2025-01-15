package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.api.event.IgiGuiInitEvent;
import com.tttsaurus.ingameinfo.plugin.crt.api.event.IIgiGuiInitEvent;
import com.tttsaurus.ingameinfo.plugin.crt.impl.event.McIgiGuiInitEvent;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.event.IEventHandle;
import crafttweaker.api.event.IEventManager;
import crafttweaker.util.EventList;
import crafttweaker.util.IEventHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ingameinfo.IEventManager")
@ZenExpansion("crafttweaker.events.IEventManager")
@SuppressWarnings("all")
public final class CrtEventManager
{
    private static final boolean loaded = Loader.isModLoaded("crafttweaker");
    private static Object igiGuiInitEventList = null;

    @ZenMethod
    public static IEventHandle onIgiGuiInit(IEventManager manager, IEventHandler<IIgiGuiInitEvent> event)
    {
        if (igiGuiInitEventList == null)
            igiGuiInitEventList = new EventList<IIgiGuiInitEvent>();
        return ((EventList<IIgiGuiInitEvent>)igiGuiInitEventList).add(event);
    }

    public static final class Handler
    {
        @SubscribeEvent
        public static void onIgiGuiInit(IgiGuiInitEvent event)
        {
            if (!loaded) return;
            if (igiGuiInitEventList == null)
                igiGuiInitEventList = new EventList<IIgiGuiInitEvent>();
            if (((EventList<IIgiGuiInitEvent>)igiGuiInitEventList).hasHandlers())
                ((EventList<IIgiGuiInitEvent>)igiGuiInitEventList).publish(new McIgiGuiInitEvent(event));
        }
    }
}
