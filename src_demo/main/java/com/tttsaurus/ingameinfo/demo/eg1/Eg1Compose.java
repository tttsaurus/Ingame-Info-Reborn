package com.tttsaurus.ingameinfo.demo.eg1;

import com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.core.mvvm.compose.ComposeBlock;
import com.tttsaurus.ingameinfo.common.core.mvvm.context.ContextKey;
import com.tttsaurus.ingameinfo.common.core.mvvm.context.SharedContext;

public class Eg1Compose extends ComposeBlock
{
    public Eg1Compose(ElementGroup root)
    {
        super(root);
    }

    @Override
    public void compose(double deltaTime, SharedContext sharedContext)
    {
        ui("VerticalGroup").wrap(() ->
        {
            Object num = sharedContext.get(ContextKey.gen("num", int.class));
            if (num != null)
                for (int i = 0; i < (int)num; i++)
                    ui("Text").key(i).prop("text", "switched once");
        });
    }
}
