package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command;

import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import net.minecraft.item.ItemStack;

public class VC_RenderItem extends VisualCommand
{
    protected VC_RenderItem()
    {
        super(params(ItemStack.class, float.class, float.class, float.class, float.class));
    }

    @Override
    public void execute(Object... args)
    {
        int index = matchParams(args);
        if (index != -1) castArgs(index, args);
        switch (index)
        {
            case 0 ->
            {
                RenderUtils.renderItem((ItemStack)args[0], (float)args[1], (float)args[2], (float)args[3], (float)args[4]);
            }
        }
    }
}
