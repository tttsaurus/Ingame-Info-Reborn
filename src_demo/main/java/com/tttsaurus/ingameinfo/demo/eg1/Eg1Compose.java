package com.tttsaurus.ingameinfo.demo.eg1;

import com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.core.mvvm.compose.ComposeBlock;

public class Eg1Compose extends ComposeBlock
{
    public Eg1Compose(ElementGroup root)
    {
        super(root);
    }

    int index = 0;

    @Override
    public void compose()
    {
        if (index == 0)
        {
            ui("VerticalGroup").wrap(() ->
            {
                ui("VerticalGroup").key("g").wrap(() ->
                {
                    ui("Text").key("a").prop("text", "test a");
                    ui("Text").key("b").prop("text", "test b");
                    ui("Text").key("c").prop("text", "test c");
                });
            });
        }
        if (index == 1)
        {
            ui("VerticalGroup").wrap(() ->
            {
                ui("Text").prop("text", "one extra");
                ui("VerticalGroup").key("g").wrap(() ->
                {
                    ui("Text").key("c").prop("text", "test a!!");
                    //ui("Text").key("a").prop("text", "test b!!");
                    ui("Text").prop("text", "one extra");
                    ui("Text").key("b").prop("text", "test c!!");
                });
                ui("Text").prop("text", "one extra");
            });
        }
        index++;
    }
}
