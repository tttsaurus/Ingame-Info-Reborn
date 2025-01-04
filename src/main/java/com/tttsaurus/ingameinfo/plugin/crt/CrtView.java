package com.tttsaurus.ingameinfo.plugin.crt;

import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.mvvm.View")
public final class CrtView extends View
{
    @Override
    public String getIxmlFileName()
    {
        return "";
    }
}
