package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command;

import com.tttsaurus.ingameinfo.common.core.render.AlphaBlendMode;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.text.FormattedText;

public class VC_RenderFormattedText extends VisualCommand
{
    protected VC_RenderFormattedText()
    {
        super(
                params(FormattedText.class, float.class, float.class, float.class, int.class, boolean.class),
                params(FormattedText.class, float.class, float.class, float.class, int.class, boolean.class, AlphaBlendMode.class));
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
                RenderUtils.renderFormattedText((FormattedText)args[0], (float)args[1], (float)args[2], (float)args[3], (int)args[4], (boolean)args[5]);
            }
            case 1 ->
            {
                RenderUtils.renderFormattedText((FormattedText)args[0], (float)args[1], (float)args[2], (float)args[3], (int)args[4], (boolean)args[5], (AlphaBlendMode)args[6]);
            }
        }
    }
}
