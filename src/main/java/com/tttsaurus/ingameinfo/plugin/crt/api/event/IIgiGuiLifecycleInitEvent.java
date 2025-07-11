package com.tttsaurus.ingameinfo.plugin.crt.api.event;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.event.IEventCancelable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenRegister
@ZenClass("mods.ingameinfo.event.IgiGuiLifecycleInitEvent")
public interface IIgiGuiLifecycleInitEvent extends IEventCancelable
{
    @ZenGetter("lifecycleOwner")
    String getLifecycleOwner();
}
