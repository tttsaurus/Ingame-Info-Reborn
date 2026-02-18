package com.tttsaurus.ingameinfo.plugin.crt.api.igievent;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.igievent.IgiGuiFpsEventListener")
public interface IgiGuiFpsEventListener
{
    void invoke(int arg0, int arg1);
}
