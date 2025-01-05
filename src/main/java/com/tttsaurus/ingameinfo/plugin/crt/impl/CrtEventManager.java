package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.api.event.IgiGuiInitEvent;
import com.tttsaurus.ingameinfo.plugin.crt.api.event.IIgiGuiInitEvent;
import com.tttsaurus.ingameinfo.plugin.crt.impl.event.McIgiGuiInitEvent;
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
public class CrtEventManager
{
    private static final EventList<IIgiGuiInitEvent> igiGuiInitEventList = new EventList<>();

    @ZenMethod
    public static IEventHandle onIgiGuiInit(IEventManager manager, IEventHandler<IIgiGuiInitEvent> event)
    {
        return igiGuiInitEventList.add(event);
    }

    public static final class Handler
    {
        @SubscribeEvent
        public static void onIgiGuiInit(IgiGuiInitEvent event)
        {
            if (igiGuiInitEventList.hasHandlers())
                igiGuiInitEventList.publish(new McIgiGuiInitEvent(event));
        }
    }
}
