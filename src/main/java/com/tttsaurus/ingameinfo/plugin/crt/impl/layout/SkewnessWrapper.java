package com.tttsaurus.ingameinfo.plugin.crt.impl.layout;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Skewness;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenRegister
@ZenClass("mods.ingameinfo.layout.Skewness")
public final class SkewnessWrapper
{
    public final Skewness skewness;
    public SkewnessWrapper(Skewness skewness)
    {
        this.skewness = skewness;
    }

    @ZenProperty
    public static final SkewnessWrapper NULL = new SkewnessWrapper(Skewness.NULL);

    @ZenProperty
    public static final SkewnessWrapper LEFT = new SkewnessWrapper(Skewness.LEFT);

    @ZenProperty
    public static final SkewnessWrapper RIGHT = new SkewnessWrapper(Skewness.RIGHT);
}
