package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command;

import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.IRenderOp;

public interface IArgsMapping
{
    Object[] genCommandArgs(RenderContext renderContext, IRenderOp renderOp);
}
