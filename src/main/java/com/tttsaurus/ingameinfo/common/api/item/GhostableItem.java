package com.tttsaurus.ingameinfo.common.api.item;

import com.tttsaurus.ingameinfo.common.api.serialization.Deserializer;
import com.tttsaurus.ingameinfo.common.impl.serialization.ItemDeserializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import javax.annotation.Nullable;

@Deserializer(ItemDeserializer.class)
public final class GhostableItem
{
    private int meta;
    private ResourceLocation resourceLocation;

    private boolean isGhost;
    private boolean doesntExist;

    private ItemStack itemStack;

    public GhostableItem(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }
    public GhostableItem(String registryName)
    {
        itemStack = null;

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
        }
        if (itemStack == null)
            return null;
        else
            return itemStack;
    }
}
