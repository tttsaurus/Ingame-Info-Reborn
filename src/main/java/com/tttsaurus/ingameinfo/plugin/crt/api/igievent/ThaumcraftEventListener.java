package com.tttsaurus.ingameinfo.plugin.crt.api.igievent;

import com.tttsaurus.ingameinfo.common.impl.igievent.modcompat.ThaumcraftEvent;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.igievent.ThaumcraftEventListener")
public interface ThaumcraftEventListener
{
    void invoke(ThaumcraftEvent.ThaumcraftData arg0);
}
