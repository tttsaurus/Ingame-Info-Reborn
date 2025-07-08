package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command;

import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;

public class VC_RenderRoundedRectOutline extends VisualCommand
{
    protected VC_RenderRoundedRectOutline()
    {
        super(params(float.class, float.class, float.class, float.class, float.class, float.class, int.class, boolean.class));
    }

    @Override
    public void execute(Object... args)
    {
        switch (matchParam(args))
        {
            case 0 ->
            {
                RenderUtils.renderRoundedRectOutline((float)args[0], (float)args[1], (float)args[2], (float)args[3], (float)args[4], (float)args[5], (int)args[6], (boolean)args[7]);
            }
        }
    }
}
