package com.tttsaurus.ingameinfo.demo.eg1;

import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.RenderDecorator;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.RenderOpPhase;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command.VisualCommandSet;
import com.tttsaurus.ingameinfo.common.core.mvvm.view.View;
import com.tttsaurus.ingameinfo.common.impl.gui.render.op.ButtonOp;

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

    @Override
    public void initRenderDecorator(RenderDecorator renderDecorator)
    {
        renderDecorator.register(ButtonOp.class, RenderOpPhase.AFTER_EXE, builder ->
        {
            builder.command(VisualCommandSet.DRAW_RECT, (renderContext, renderOp) ->
            {
                ButtonOp op = (ButtonOp)renderOp;
                return new Object[]{op.rect.x, op.rect.y, op.rect.width, op.rect.height, -1};
            });
        });
    }
}
