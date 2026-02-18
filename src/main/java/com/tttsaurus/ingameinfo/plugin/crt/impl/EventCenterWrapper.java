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
    public static void addIgiGuiFpsEventListener(IgiGuiFpsEventListener listener)
    {
        EventCenter.igiGuiFpsEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addIgiGuiFboRefreshRateEventListener(IgiGuiFboRefreshRateEventListener listener)
    {
        EventCenter.igiGuiFboRefreshRateEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addGameFpsEventListener(GameFpsEventListener listener)
    {
        EventCenter.gameFpsEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addGameMemoryEventListener(GameMemoryEventListener listener)
    {
        EventCenter.gameMemoryEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addGameTpsMsptEventListener(GameTpsMsptEventListener listener)
    {
        EventCenter.gameTpsMsptEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addEnterBiomeEventListener(EnterBiomeEventListener listener)
    {
        EventCenter.enterBiomeEvent.addListener(listener::invoke);
    }

    @ZenMethod
    public static void addBloodMagicEventListener(BloodMagicEventListener listener)
    {
        EventCenter.bloodMagicEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addSereneSeasonsEventListener(SereneSeasonsEventListener listener)
    {
        EventCenter.sereneSeasonsEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addThaumcraftEventListener(ThaumcraftEventListener listener)
    {
        EventCenter.thaumcraftEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addRfToolsDimEventListener(RfToolsDimEventListener listener)
    {
        EventCenter.rftoolsdimEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addDeepResonanceEventListener(DeepResonanceEventListener listener)
    {
        EventCenter.deepresonanceEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addToughAsNailsEventListener(ToughAsNailsEventListener listener)
    {
        EventCenter.toughasnailsEvent.addListener(listener::invoke);
    }
    @ZenMethod
    public static void addSimpleDifficultyEventListener(SimpleDifficultyEventListener listener)
    {
        EventCenter.simpledifficultyEvent.addListener(listener::invoke);
    }
}
