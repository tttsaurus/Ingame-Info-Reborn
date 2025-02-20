package com.tttsaurus.ingameinfo.plugin.crt.api.igievent;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.igievent.SimpleDifficultyEventListener")
public interface ISimpleDifficultyEventListener
{
    void invoke(int arg0);
}
