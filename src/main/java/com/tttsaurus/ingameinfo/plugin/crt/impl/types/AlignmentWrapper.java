package com.tttsaurus.ingameinfo.plugin.crt.impl.types;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Alignment;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenRegister
@ZenClass("mods.ingameinfo.layout.Alignment")
public final class AlignmentWrapper
{
    public final Alignment alignment;
    public AlignmentWrapper(Alignment alignment)
    {
        this.alignment = alignment;
    }

    @ZenProperty
    public static final AlignmentWrapper NULL = new AlignmentWrapper(Alignment.NULL);

    @ZenProperty
    public static final AlignmentWrapper TOP_LEFT = new AlignmentWrapper(Alignment.TOP_LEFT);

    @ZenProperty
    public static final AlignmentWrapper TOP_CENTER = new AlignmentWrapper(Alignment.TOP_CENTER);

    @ZenProperty
    public static final AlignmentWrapper TOP_RIGHT = new AlignmentWrapper(Alignment.TOP_RIGHT);

    @ZenProperty
    public static final AlignmentWrapper CENTER_LEFT = new AlignmentWrapper(Alignment.CENTER_LEFT);

    @ZenProperty
    public static final AlignmentWrapper CENTER = new AlignmentWrapper(Alignment.CENTER);

    @ZenProperty
    public static final AlignmentWrapper CENTER_RIGHT = new AlignmentWrapper(Alignment.CENTER_RIGHT);

    @ZenProperty
    public static final AlignmentWrapper BOTTOM_LEFT = new AlignmentWrapper(Alignment.BOTTOM_LEFT);

    @ZenProperty
    public static final AlignmentWrapper BOTTOM_CENTER = new AlignmentWrapper(Alignment.BOTTOM_CENTER);

    @ZenProperty
    public static final AlignmentWrapper BOTTOM_RIGHT = new AlignmentWrapper(Alignment.BOTTOM_RIGHT);
}
