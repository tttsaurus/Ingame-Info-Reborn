package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.core.IgiRuntimeLocator;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ingameinfo.gui.IgiGuiManager")
public final class CrtIgiGuiManager
{
    @ZenMethod
    public static void openGui(String mvvmRegistryName)
    {
        IgiRuntimeLocator.get().livePhase.openGui(mvvmRegistryName);
    }

    @ZenMethod
    public static void closeGui(String mvvmRegistryName)
    {
        IgiRuntimeLocator.get().livePhase.closeGui(mvvmRegistryName);
    }
}
