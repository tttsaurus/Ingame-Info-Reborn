package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command;

import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.RenderOp;

public interface ArgsGenerator
{
    Object[] genCommandArgs(RenderContext renderContext, RenderOp renderOp);
}
