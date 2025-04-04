package com.tttsaurus.ingameinfo.plugin.crt.impl.types;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Pivot;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenRegister
@ZenClass("mods.ingameinfo.layout.Pivot")
public final class PivotWrapper
{
    public final Pivot pivot;
    public PivotWrapper(Pivot pivot)
    {
        this.pivot = pivot;
    }

    @ZenProperty
    public static final PivotWrapper TOP_LEFT = new PivotWrapper(Pivot.TOP_LEFT);

    @ZenProperty
    public static final PivotWrapper TOP_CENTER = new PivotWrapper(Pivot.TOP_CENTER);

    @ZenProperty
    public static final PivotWrapper TOP_RIGHT = new PivotWrapper(Pivot.TOP_RIGHT);

    @ZenProperty
    public static final PivotWrapper CENTER_LEFT = new PivotWrapper(Pivot.CENTER_LEFT);

    @ZenProperty
    public static final PivotWrapper CENTER = new PivotWrapper(Pivot.CENTER);

    @ZenProperty
    public static final PivotWrapper CENTER_RIGHT = new PivotWrapper(Pivot.CENTER_RIGHT);

    @ZenProperty
    public static final PivotWrapper BOTTOM_LEFT = new PivotWrapper(Pivot.BOTTOM_LEFT);

    @ZenProperty
    public static final PivotWrapper BOTTOM_CENTER = new PivotWrapper(Pivot.BOTTOM_CENTER);

    @ZenProperty
    public static final PivotWrapper BOTTOM_RIGHT = new PivotWrapper(Pivot.BOTTOM_RIGHT);
}
