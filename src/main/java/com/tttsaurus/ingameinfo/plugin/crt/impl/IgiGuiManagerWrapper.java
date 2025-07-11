package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.core.gui.IgiGuiManager;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenRegister
@ZenClass("mods.ingameinfo.gui.IgiGuiManager")
public final class IgiGuiManagerWrapper
{
    @ZenProperty
    public static String OWNER_NAME = IgiGuiManager.OWNER_NAME;

    @ZenMethod
    public static void openGui(String mvvmRegistryName)
    {
        IgiGuiManager.openGui(mvvmRegistryName);
    }

    @ZenMethod
    public static void closeGui(String mvvmRegistryName)
    {
        IgiGuiManager.closeGui(mvvmRegistryName);
    }
}
