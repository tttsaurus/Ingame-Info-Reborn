package com.tttsaurus.ingameinfo.common.core.commonutils;

import com.tttsaurus.ingameinfo.common.core.serialization.Deserializer;
import com.tttsaurus.ingameinfo.common.impl.serialization.ItemDeserializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Deserializer(ItemDeserializer.class)
public final class GhostableItem
{
    private final String raw;

    private int meta;
    private ResourceLocation resourceLocation;

    private boolean isGhost = false;
    private boolean doesntExist = false;

    private ItemStack itemStack;

    public GhostableItem(@Nonnull ItemStack itemStack)
    {
        this.itemStack = itemStack;
        if (itemStack.getItem().getRegistryName() == null)
            raw = "null";
        else
            raw = itemStack.getItem().getRegistryName().toString() + ":" + itemStack.getMetadata();
    }
    public GhostableItem(String registryName)
    {
        itemStack = null;
        raw = registryName;

        String[] args = registryName.split(":");
        if (args.length <= 1 || args.length > 3)
        {
            doesntExist = true;
            return;
        }

        resourceLocation = new ResourceLocation(args[0], args[1]);
        meta = 0;
        if (args.length == 3)
            try { meta = Integer.parseInt(args[2]); }
            catch (NumberFormatException ignored) { }

        Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
        if (item == null)
        {
            isGhost = true;
            return;
        }

        itemStack = new ItemStack(item, 1, meta);
    }

    private boolean isAbortNextTime = false;
    public void abortNextTime() { isAbortNextTime = true; }

    @Nullable
    public ItemStack getItemStack()
    {
        if (doesntExist) return null;
        if (isGhost)
        {
            Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
            if (item != null)
            {
                isGhost = false;
                itemStack = new ItemStack(item, 1, meta);
            }
            else if (isAbortNextTime)
                doesntExist = true;
        }
        if (itemStack == null)
            return null;
        else
            return itemStack;
    }

    @Override
    public String toString()
    {
        return "GhostableItem{" +
                "raw=" + raw +
                ", isGhost=" + isGhost +
                ", doesntExist=" + doesntExist +
                '}';
    }
}
