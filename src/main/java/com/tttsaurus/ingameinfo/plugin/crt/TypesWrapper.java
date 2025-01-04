package com.tttsaurus.ingameinfo.plugin.crt;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenRegister
@ZenClass("mods.ingameinfo.Types")
public class TypesWrapper
{
    protected final Types types;
    private TypesWrapper(Types types)
    {
        this.types = types;
    }

    @ZenProperty
    public static final TypesWrapper Int = new TypesWrapper(Types.Int);

    @ZenProperty
    public static final TypesWrapper Long = new TypesWrapper(Types.Long);

    @ZenProperty
    public static final TypesWrapper Short = new TypesWrapper(Types.Short);

    @ZenProperty
    public static final TypesWrapper Byte = new TypesWrapper(Types.Byte);

    @ZenProperty
    public static final TypesWrapper Double = new TypesWrapper(Types.Double);

    @ZenProperty
    public static final TypesWrapper Float = new TypesWrapper(Types.Float);

    @ZenProperty
    public static final TypesWrapper Char = new TypesWrapper(Types.Char);

    @ZenProperty
    public static final TypesWrapper Boolean = new TypesWrapper(Types.Boolean);

    @ZenProperty
    public static final TypesWrapper String = new TypesWrapper(Types.String);
}
