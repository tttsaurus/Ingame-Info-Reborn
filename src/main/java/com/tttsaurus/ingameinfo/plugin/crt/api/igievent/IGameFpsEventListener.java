package com.tttsaurus.ingameinfo.plugin.crt.api.igievent;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.event.IGameFpsEventListener")
public interface IGameFpsEventListener
{
    void invoke(int arg0);
}
