package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.impl.igievent.EventCenter;
import com.tttsaurus.ingameinfo.plugin.crt.api.igievent.*;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ingameinfo.igievent.EventCenter")
public final class EventCenterWrapper
{
    @ZenMethod
    public static void addIgiGuiFpsEventListener(IIgiGuiFpsEventListener listener)
    {
        EventCenter.igiGuiFpsEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addIgiGuiFboRefreshRateEventListener(IIgiGuiFboRefreshRateEventListener listener)
    {
        EventCenter.igiGuiFboRefreshRateEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addGameFpsEventListener(IGameFpsEventListener listener)
    {
        EventCenter.gameFpsEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addGameMemoryEventListener(IGameMemoryEventListener listener)
    {
        EventCenter.gameMemoryEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addGameTpsMsptEventListener(IGameTpsMsptEventListener listener)
    {
        EventCenter.gameTpsMsptEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addEnterBiomeEventListener(IEnterBiomeEventListener listener)
    {
        EventCenter.enterBiomeEvent.addListener(listener::invoke);
    }

    @ZenMethod
    public static void addBloodMagicEventListener(IBloodMagicEventListener listener)
    {
        EventCenter.bloodMagicEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addSereneSeasonsEventListener(ISereneSeasonsEventListener listener)
    {
        EventCenter.sereneSeasonsEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addThaumcraftEventListener(IThaumcraftEventListener listener)
    {
        EventCenter.thaumcraftEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addRfToolsDimEventListener(IRfToolsDimEventListener listener)
    {
        EventCenter.rftoolsdimEvent.addListener(listener::invoke);
    }
}
