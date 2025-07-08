package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command;

import com.tttsaurus.ingameinfo.common.core.render.ImagePrefab;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;

public class VC_RenderImagePrefab extends VisualCommand
{
    protected VC_RenderImagePrefab()
    {
        super(
                params(float.class, float.class, float.class, float.class, ImagePrefab.class),
                params(float.class, float.class, float.class, float.class, ImagePrefab.class, int.class));
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
                RenderUtils.renderImagePrefab((float)args[0], (float)args[1], (float)args[2], (float)args[3], (ImagePrefab)args[4]);
            }
            case 1 ->
            {
                RenderUtils.renderImagePrefab((float)args[0], (float)args[1], (float)args[2], (float)args[3], (ImagePrefab)args[4], (int)args[5]);
            }
        }
    }
}
