package com.tttsaurus.ingameinfo.plugin.crt.api.event;

import com.tttsaurus.ingameinfo.common.core.IgiRuntime;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.event.IEventCancelable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenRegister
@ZenClass("mods.ingameinfo.event.IgiRuntimeEntryPointEvent")
public interface IgiRuntimeEntryPointEvent extends IEventCancelable
{
    @ZenGetter("initPhase")
    IgiRuntime.InitPhaseEntry getInitPhase();

    @ZenGetter("livePhase")
    IgiRuntime.LivePhaseEntry getLivePhase();
}
