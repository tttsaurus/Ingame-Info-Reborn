package com.tttsaurus.ingameinfo.plugin.crt.api.igievent;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.igievent.DeepResonanceEventListener")
public interface IDeepResonanceEventListener
{
    void invoke(float arg0);
}
