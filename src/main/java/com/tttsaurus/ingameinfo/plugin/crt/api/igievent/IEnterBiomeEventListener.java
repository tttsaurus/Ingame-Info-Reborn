package com.tttsaurus.ingameinfo.plugin.crt.api.igievent;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.igievent.EnterBiomeEventListener")
public interface IEnterBiomeEventListener
{
    void invoke(String arg0, String arg1);
}
