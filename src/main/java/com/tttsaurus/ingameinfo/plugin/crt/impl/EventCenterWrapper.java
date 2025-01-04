package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.impl.igievent.EventCenter;
import com.tttsaurus.ingameinfo.plugin.crt.api.igievent.IIgiGuiFpsEventListener;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ingameinfo.event.EventCenter")
public final class EventCenterWrapper
{
    @ZenMethod
    public static void addIgiGuiFpsEventListener(IIgiGuiFpsEventListener listener)
    {
        EventCenter.igiGuiFpsEvent.addListener(listener::invoke);
    }
}
