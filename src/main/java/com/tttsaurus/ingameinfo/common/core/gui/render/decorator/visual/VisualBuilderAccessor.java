package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual;

import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command.IArgsGenerator;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command.VisualCommand;
import net.minecraft.util.Tuple;
import java.util.List;

public class VisualBuilderAccessor
{
    private VisualBuilder visualBuilder;

    protected VisualBuilderAccessor() { }

    public void setVisualBuilder(VisualBuilder visualBuilder)
    {
        this.visualBuilder = visualBuilder;
    }

    public boolean getAbortRenderOp()
    {
        return visualBuilder.abortRenderOp;
    }

    public List<Tuple<VisualCommand, IArgsGenerator>> getCommands()
    {
        return visualBuilder.commands;
    }
}
