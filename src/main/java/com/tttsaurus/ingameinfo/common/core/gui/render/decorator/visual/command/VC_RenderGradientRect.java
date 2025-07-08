package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command;

import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;

public class VC_RenderGradientRect extends VisualCommand
{
    protected VC_RenderGradientRect()
    {
        super(params(float.class, float.class, float.class, float.class, int.class, int.class));
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
                RenderUtils.renderGradientRect((float)args[0], (float)args[1], (float)args[2], (float)args[3], (int)args[4], (int)args[4]);
            }
        }
    }
}
