package com.tttsaurus.ingameinfo.plugin.crt.api.viewmodel;

import com.tttsaurus.ingameinfo.plugin.crt.impl.CrtViewModel;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.ingameinfo.mvvm.IViewModelStart")
public interface ViewModelStart
{
    void start(CrtViewModel this0);
}
