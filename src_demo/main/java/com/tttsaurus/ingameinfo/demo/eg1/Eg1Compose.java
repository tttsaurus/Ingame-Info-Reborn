package com.tttsaurus.ingameinfo.demo.eg1;

import com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.core.mvvm.compose.ComposeBlock;

public class Eg1Compose extends ComposeBlock
{
    public Eg1Compose(ElementGroup root)
    {
        super(root);
    }

    @Override
    public void compose()
    {
        ui("VerticalGroup").wrap(() ->
        {
            ui("Text").prop("text", "switch tooltip");
        });
    }
}
