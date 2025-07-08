package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command;

import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;

public class VC_RenderRect extends VisualCommand
{
    protected VC_RenderRect()
    {
        super(params(float.class, float.class, float.class, float.class, int.class));
    }

    @Override
    public void execute(Object... args)
    {
        switch (matchParam(args))
        {
            case 0 ->
            {
                RenderUtils.renderRect((float)args[0], (float)args[1], (float)args[2], (float)args[3], (int)args[4]);
            }
        }
    }
}
