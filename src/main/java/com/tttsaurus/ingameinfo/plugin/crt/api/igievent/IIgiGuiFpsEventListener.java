package com.tttsaurus.ingameinfo.plugin.crt.api.igievent;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.event.IIgiGuiFpsEventListener")
public interface IIgiGuiFpsEventListener
{
    void invoke(int arg0);
}
