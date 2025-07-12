package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.core.forgeevent.IgiRuntimeEntryPointEvent;
import com.tttsaurus.ingameinfo.plugin.crt.api.event.IIgiRuntimeEntryPointEvent;
import com.tttsaurus.ingameinfo.plugin.crt.impl.event.McIgiRuntimeEntryPointEvent;
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
    public static IEventHandle onIgiRuntimeEntryPoint(IEventManager manager, IEventHandler<IIgiRuntimeEntryPointEvent> event)
    {
        if (igiGuiInitEventList == null)
            igiGuiInitEventList = new EventList<IIgiRuntimeEntryPointEvent>();
        return ((EventList<IIgiRuntimeEntryPointEvent>)igiGuiInitEventList).add(event);
    }

    public static final class Handler
    {
        @SubscribeEvent
        public static void onIgiRuntimeEntryPoint(IgiRuntimeEntryPointEvent event)
        {
            if (igiGuiInitEventList == null)
                igiGuiInitEventList = new EventList<IIgiRuntimeEntryPointEvent>();
            if (((EventList<IIgiRuntimeEntryPointEvent>)igiGuiInitEventList).hasHandlers())
                ((EventList<IIgiRuntimeEntryPointEvent>)igiGuiInitEventList).publish(new McIgiRuntimeEntryPointEvent(event));
        }
    }
}
