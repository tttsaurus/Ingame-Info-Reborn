package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.core.forgeevent.IgiGuiLifecycleInitEvent;
import com.tttsaurus.ingameinfo.plugin.crt.api.event.IIgiGuiLifecycleInitEvent;
import com.tttsaurus.ingameinfo.plugin.crt.impl.event.McIgiGuiLifecycleInitEvent;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.event.IEventHandle;
import crafttweaker.api.event.IEventManager;
import crafttweaker.util.EventList;
import crafttweaker.util.IEventHandler;
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
    private static Object igiGuiInitEventList = null;

    @ZenMethod
    public static IEventHandle onIgiGuiLifecycleInit(IEventManager manager, IEventHandler<IIgiGuiLifecycleInitEvent> event)
    {
        if (igiGuiInitEventList == null)
            igiGuiInitEventList = new EventList<IIgiGuiLifecycleInitEvent>();
        return ((EventList<IIgiGuiLifecycleInitEvent>)igiGuiInitEventList).add(event);
    }

    public static final class Handler
    {
        @SubscribeEvent
        public static void onIgiGuiLifecycleInit(IgiGuiLifecycleInitEvent event)
        {
            if (igiGuiInitEventList == null)
                igiGuiInitEventList = new EventList<IIgiGuiLifecycleInitEvent>();
            if (((EventList<IIgiGuiLifecycleInitEvent>)igiGuiInitEventList).hasHandlers())
                ((EventList<IIgiGuiLifecycleInitEvent>)igiGuiInitEventList).publish(new McIgiGuiLifecycleInitEvent(event));
        }
    }
}
