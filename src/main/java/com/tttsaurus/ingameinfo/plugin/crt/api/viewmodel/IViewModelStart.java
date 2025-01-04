package com.tttsaurus.ingameinfo.plugin.crt.api.viewmodel;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.mvvm.IViewModelStart")
public interface IViewModelStart
{
    void start();
}
