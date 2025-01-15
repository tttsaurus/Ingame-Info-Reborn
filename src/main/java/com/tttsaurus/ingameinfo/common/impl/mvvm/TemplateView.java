package com.tttsaurus.ingameinfo.common.impl.mvvm;

import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;

public class TemplateView extends View
{
    @Override
    public String getDefaultIxml()
    {
        return
                """
                <Def debug = false>
                <VerticalGroup>
                    <Text uid = "biome">
                    <Text uid = "tps/mtps">
                    <HorizontalGroup>
                        <Text text = "Memory: ">
                        <ProgressBar uid = "memoryBar" width = 40 height = 3 alignment = CENTER pivot = CENTER padding = {"right": 5}>
                        <Text uid = "memory">
                    </Group>
                    <Text uid = "fps">
                    <Text uid = "igiFps">
                </Group>
                """;
    }

    @Override
    public String getIxmlFileName()
    {
        return "template";
    }
}
