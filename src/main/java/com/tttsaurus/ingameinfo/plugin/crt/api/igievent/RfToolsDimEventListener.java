package com.tttsaurus.ingameinfo.plugin.crt.api.igievent;

import com.tttsaurus.ingameinfo.common.impl.igievent.modcompat.RfToolsDimEvent;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.igievent.RfToolsDimEventListener")
public interface RfToolsDimEventListener
{
    void invoke(RfToolsDimEvent.RfToolsDimData arg0);
}
