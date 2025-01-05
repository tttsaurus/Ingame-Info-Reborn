package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import java.util.HashMap;
import java.util.Map;

@ZenRegister
@ZenClass("mods.ingameinfo.mvvm.View")
public final class CrtView extends View
{
    public String runtimeMvvm;

    // key: mvvm registry name
    private static Map<String, String> ixmlFileNames = new HashMap<>();

    @ZenMethod
    public static void setIxmlFileName(String name) { ixmlFileNames.put(CrtMvvm.currentMvvm, name); }

    @Override
    public String getIxmlFileName()
    {
        return ixmlFileNames.get(runtimeMvvm);
    }
}
