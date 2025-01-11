package com.tttsaurus.ingameinfo.common.impl.mvvm;

import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;

public class TemplateView extends View
{
    @Override
    public String getDefaultIxml()
    {
        return
                """
                <Text uid = "biome">
                """;
    }

    @Override
    public String getIxmlFileName()
    {
        return "template";
    }
}
