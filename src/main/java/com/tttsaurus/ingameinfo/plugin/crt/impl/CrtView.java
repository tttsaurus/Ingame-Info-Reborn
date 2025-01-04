package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ingameinfo.mvvm.View")
public final class CrtView extends View
{
    private static String ixmlFileName = "";

    @ZenMethod
    public static void setIxmlFileName(String name) { ixmlFileName = name; }

    @Override
    public String getIxmlFileName() { return ixmlFileName; }
}
