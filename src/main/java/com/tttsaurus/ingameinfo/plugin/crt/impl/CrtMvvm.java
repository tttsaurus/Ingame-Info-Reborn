package com.tttsaurus.ingameinfo.plugin.crt.impl;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass("mods.ingameinfo.mvvm.Mvvm")
public class CrtMvvm
{
    public static List<String> mvvms = new ArrayList<>();
    public static String currentMvvm;

    @ZenMethod
    public static void define(String mvvm)
    {
        if (!mvvms.contains(mvvm)) mvvms.add(mvvm);
        currentMvvm = mvvm;
    }
}
