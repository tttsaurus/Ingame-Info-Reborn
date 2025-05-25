package com.tttsaurus.ingameinfo.demo.eg1;

import com.tttsaurus.ingameinfo.common.core.mvvm.view.View;

public class Eg1View extends View
{
    @Override
    public String getIxmlFileName()
    {
        return "eg1";
    }

    @Override
    public String getDefaultIxml()
    {
        return """
                <Def focused = true>
                <VerticalGroup uid = "list">
                    <Text uid = "tps/mspt">
                    <HorizontalGroup>
                        <Text text = "Memory: ">
                        <ProgressBar uid = "memoryBar" width = 40 height = 3 alignment = CENTER pivot = CENTER padding = {"right": 5}>
                        <Text uid = "memory">
                    </Group>
                    <Button uid = "switch" text = "Switch">
                    <Slot uid = "mySlot">
                </Group>
                """;
    }
}
