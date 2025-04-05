package com.tttsaurus.ingameinfo.plugin.crt.impl.types;

import com.tttsaurus.ingameinfo.common.api.item.GhostableItem;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ingameinfo.item.GhostableItem")
public class GhostableItemWrapper
{
    public final GhostableItem ghostableItem;

    public GhostableItemWrapper(GhostableItem ghostableItem)
    {
        this.ghostableItem = ghostableItem;
    }
    public GhostableItemWrapper(String registryName)
    {
        this.ghostableItem = new GhostableItem(registryName);
    }
    public GhostableItemWrapper(ItemStack itemStack)
    {
        this.ghostableItem = new GhostableItem(itemStack);
    }

    @ZenMethod("new")
    public static GhostableItemWrapper newGhostableItem(String registryName)
    {
        return new GhostableItemWrapper(registryName);
    }
    @ZenMethod("new")
    public static GhostableItemWrapper newGhostableItem(IItemStack itemStack)
    {
        return new GhostableItemWrapper(CraftTweakerMC.getItemStack(itemStack));
    }

    @ZenMethod
    public boolean isItemNull()
    {
        return ghostableItem.getItemStack() == null;
    }
    @ZenMethod
    public IItemStack getItemStack()
    {
        if (isItemNull()) return null;
        return CraftTweakerMC.getIItemStack(ghostableItem.getItemStack());
    }
}
