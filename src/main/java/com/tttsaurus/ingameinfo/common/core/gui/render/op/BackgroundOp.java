package com.tttsaurus.ingameinfo.common.core.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.gui.GuiResources;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;

public class BackgroundOp implements IRenderOp
{
    public String backgroundStyle;
    public Rect rect;

    public BackgroundOp(String backgroundStyle, Rect rect)
    {
        this.backgroundStyle = backgroundStyle;
        this.rect = rect;
    }

    @Override
    public void execute(RenderContext context)
    {
        if (backgroundStyle.isEmpty()) return;

        switch (backgroundStyle)
        {
            case "box" ->
            {
                RenderUtils.renderRect(rect.x, rect.y, rect.width, rect.height, context.theme.backgroundStyles.box.parsedColor);
            }
            case "box-with-outline" ->
            {
                RenderUtils.renderRect(rect.x, rect.y, rect.width, rect.height, context.theme.backgroundStyles.boxWithOutline.parsedColor);
                RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, context.theme.backgroundStyles.boxWithOutline.parsedOutlineColor);
            }
            case "rounded-box" ->
            {
                RenderUtils.renderRoundedRect(rect.x, rect.y, rect.width, rect.height, context.theme.backgroundStyles.roundedBox.cornerRadius, context.theme.backgroundStyles.roundedBox.parsedColor, context.polygonSmoothHint);
            }
            case "rounded-box-with-outline" ->
            {
                RenderUtils.renderRoundedRect(rect.x, rect.y, rect.width, rect.height, context.theme.backgroundStyles.roundedBoxWithOutline.cornerRadius, context.theme.backgroundStyles.roundedBoxWithOutline.parsedColor, context.polygonSmoothHint);
                RenderUtils.renderRoundedRectOutline(rect.x, rect.y, rect.width, rect.height, context.theme.backgroundStyles.roundedBoxWithOutline.cornerRadius, 1.0f, context.theme.backgroundStyles.roundedBoxWithOutline.parsedOutlineColor, context.lineSmoothHint);
            }
            case "mc-vanilla" ->
            {
                RenderUtils.renderImagePrefab(rect.x, rect.y, rect.width, rect.height, GuiResources.get("vanilla_background"), context.theme.backgroundStyles.mcVanilla.parsedColor);
            }
        }
    }
}
