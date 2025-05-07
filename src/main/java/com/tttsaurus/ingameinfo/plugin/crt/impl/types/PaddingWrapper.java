package com.tttsaurus.ingameinfo.plugin.crt.impl.types;

import com.tttsaurus.ingameinfo.common.core.gui.layout.Padding;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ingameinfo.layout.Padding")
public final class PaddingWrapper
{
    public final Padding padding;
    public PaddingWrapper(Padding padding)
    {
        this.padding = padding;
    }
    public PaddingWrapper()
    {
        this.padding = new Padding(0f, 0f, 0f, 0f);
    }
    public PaddingWrapper(float top, float bottom, float left, float right)
    {
        this.padding = new Padding(top, bottom, left, right);
    }

    @ZenMethod("new")
    public static PaddingWrapper newPadding(float top, float bottom, float left, float right)
    {
        return new PaddingWrapper(top, bottom, left, right);
    }
    @ZenMethod("new")
    public static PaddingWrapper newPadding()
    {
        return new PaddingWrapper();
    }

    @ZenMethod
    public PaddingWrapper set(float top, float bottom, float left, float right)
    {
        padding.set(top, bottom, left, right);
        return this;
    }
    @ZenMethod
    public PaddingWrapper setTop(float top)
    {
        padding.top = top;
        return this;
    }
    @ZenMethod
    public PaddingWrapper setBottom(float bottom)
    {
        padding.bottom = bottom;
        return this;
    }
    @ZenMethod
    public PaddingWrapper setLeft(float left)
    {
        padding.left = left;
        return this;
    }
    @ZenMethod
    public PaddingWrapper setRight(float right)
    {
        padding.right = right;
        return this;
    }
}
