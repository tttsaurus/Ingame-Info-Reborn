package com.tttsaurus.ingameinfo.plugin.crt.api.igievent;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.event.IGameMemoryEventListener")
public interface IGameMemoryEventListener
{
    void invoke(long arg0);
}
