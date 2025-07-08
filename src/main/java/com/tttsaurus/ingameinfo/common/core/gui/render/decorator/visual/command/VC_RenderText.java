package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command;

import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;

public class VC_RenderText extends VisualCommand
{
    protected VC_RenderText()
    {
        super(params(String.class, float.class, float.class, float.class, int.class, boolean.class));
    }

    @Override
    public void execute(Object... args)
    {
        switch (matchParam(args))
        {
            case 0 ->
            {
                RenderUtils.renderText((String)args[0], (float)args[1], (float)args[2], (float)args[3], (int)args[4], (boolean)args[5]);
            }
        }
    }
}
