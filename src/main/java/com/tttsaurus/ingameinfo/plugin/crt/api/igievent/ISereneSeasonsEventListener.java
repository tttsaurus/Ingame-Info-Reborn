package com.tttsaurus.ingameinfo.plugin.crt.api.igievent;

import com.tttsaurus.ingameinfo.common.impl.igievent.modcompat.SereneSeasonsEvent;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.igievent.SereneSeasonsEventListener")
public interface ISereneSeasonsEventListener
{
    void invoke(SereneSeasonsEvent.SereneSeasonsData data);
}
