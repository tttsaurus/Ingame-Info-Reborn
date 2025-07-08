package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command;

import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;

public class VC_RenderRoundedRect extends VisualCommand
{
    protected VC_RenderRoundedRect()
    {
        super(params(float.class, float.class, float.class, float.class, float.class, int.class, boolean.class));
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
                RenderUtils.renderRoundedRect((float)args[0], (float)args[1], (float)args[2], (float)args[3], (float)args[4], (int)args[5], (boolean)args[6]);
            }
        }
    }
}
