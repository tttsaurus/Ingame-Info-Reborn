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
    public static final AlignmentWrapper TOP_MIDDLE = new AlignmentWrapper(Alignment.TOP_MIDDLE);

    @ZenProperty
    public static final AlignmentWrapper TOP_RIGHT = new AlignmentWrapper(Alignment.TOP_RIGHT);

    @ZenProperty
    public static final AlignmentWrapper MIDDLE_LEFT = new AlignmentWrapper(Alignment.MIDDLE_LEFT);

    @ZenProperty
    public static final AlignmentWrapper CENTER = new AlignmentWrapper(Alignment.CENTER);

    @ZenProperty
    public static final AlignmentWrapper MIDDLE_RIGHT = new AlignmentWrapper(Alignment.MIDDLE_RIGHT);

    @ZenProperty
    public static final AlignmentWrapper BOTTOM_LEFT = new AlignmentWrapper(Alignment.BOTTOM_LEFT);

    @ZenProperty
    public static final AlignmentWrapper BOTTOM_MIDDLE = new AlignmentWrapper(Alignment.BOTTOM_MIDDLE);

    @ZenProperty
    public static final AlignmentWrapper BOTTOM_RIGHT = new AlignmentWrapper(Alignment.BOTTOM_RIGHT);
}
