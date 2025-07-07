package com.tttsaurus.ingameinfo.common.core.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;

public interface IRenderOp
{
    default void readTheme(ThemeConfig theme) { }
    void execute(RenderContext context);
}
