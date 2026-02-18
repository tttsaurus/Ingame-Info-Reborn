package com.tttsaurus.ingameinfo.plugin.crt.api.igievent;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.igievent.GameTpsMtpsEventListener")
public interface GameTpsMsptEventListener
{
    void invoke(int arg0, double arg1);
}
