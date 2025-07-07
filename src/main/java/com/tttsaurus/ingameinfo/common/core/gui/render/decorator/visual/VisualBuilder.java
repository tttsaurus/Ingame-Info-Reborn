package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual;

import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command.IArgsMapping;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command.VisualCommand;
import net.minecraft.util.Tuple;
import java.util.ArrayList;
import java.util.List;

public class VisualBuilder
{
    protected final List<Tuple<VisualCommand, IArgsMapping>> commands = new ArrayList<>();
    protected boolean abortRenderOp = false;

    public VisualBuilder abortRenderOp()
    {
        abortRenderOp = true;
        return this;
    }

    public VisualBuilder command(VisualCommand command, IArgsMapping argsMapping)
    {
        commands.add(new Tuple<>(command, argsMapping));
        return this;
    }
}
