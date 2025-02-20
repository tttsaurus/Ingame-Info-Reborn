package com.tttsaurus.ingameinfo.plugin.crt.api.igievent;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.igievent.ToughAsNailsEventListener")
public interface IToughAsNailsEventListener
{
    void invoke(int arg0, int arg1);
}
