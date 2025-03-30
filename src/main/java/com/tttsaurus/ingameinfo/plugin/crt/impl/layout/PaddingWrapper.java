package com.tttsaurus.ingameinfo.plugin.crt.impl.layout;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Padding;
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

    @ZenMethod
    public static PaddingWrapper newPadding(float top, float bottom, float left, float right)
    {
        return new PaddingWrapper(top, bottom, left, right);
    }
    @ZenMethod
    public static PaddingWrapper newPadding()
    {
        return new PaddingWrapper();
    }

    @ZenMethod
    public void set(float top, float bottom, float left, float right)
    {
        padding.set(top, bottom, left, right);
    }
    @ZenMethod
    public void setTop(float top)
    {
        padding.top = top;
    }
    @ZenMethod
    public void setBottom(float bottom)
    {
        padding.bottom = bottom;
    }
    @ZenMethod
    public void setLeft(float left)
    {
        padding.left = left;
    }
    @ZenMethod
    public void setRight(float right)
    {
        padding.right = right;
    }
}
