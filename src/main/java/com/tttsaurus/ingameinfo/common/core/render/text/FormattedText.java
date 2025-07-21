package com.tttsaurus.ingameinfo.common.core.render.text;

import com.tttsaurus.ingameinfo.common.core.commonutils.GhostableItem;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>This is a temporary workaround to make text formatting easier to use in Minecraft.</p>
 * <p>It only works for {@link RenderUtils} and has rendering and I18n stuff embedded for the baking purpose.</p>
 * In order to function properly, it requires:
 * <li>{@link RenderUtils#fontRenderer} to be ready</li>
 * <li>{@link net.minecraft.client.resources.I18n} to be ready</li>
 *
 * @see RenderUtils#bakeFormattedText(String)
 */
public final class FormattedText
{
    private static final float BAKING_TEXT_SIZE = 1f;

    public static class BakedComponent
    {
        public enum Type
        {
            STRING,
            ITEM
        }

        public final float x;
        public final float y;
        public final Type type;
        public final String text;
        public final GhostableItem item;

        public BakedComponent(float x, float y, String text)
        {
            this.x = x;
            this.y = y;
            type = Type.STRING;
            this.text = text;
            item = null;
        }
        public BakedComponent(float x, float y, GhostableItem item)
        {
            this.x = x;
            this.y = y;
            type = Type.ITEM;
            text = null;
            this.item = item;
        }
    }

    public final List<BakedComponent> bakedComponents;

    public final float width;
    public final float height;

    private FormattedText(String text)
    {
        bakedComponents = new ArrayList<>();

        if (text.isEmpty())
        {
            width = 0f;
            height = RenderUtils.simulateTextHeight(BAKING_TEXT_SIZE);
            return;
        }

        List<FormattedTextParser.FlattenedToken> flattenedTokens = FormattedTextParser.flattenize(FormattedTextParser.tokenize(text));

        float x = 0, y = 0, width = 0, height = RenderUtils.simulateTextHeight(BAKING_TEXT_SIZE);

        StringBuilder sectionBuilder = new StringBuilder();
        boolean addTextSection = false;

        for (FormattedTextParser.FlattenedToken token: flattenedTokens)
        {
            if (token.outputType == FormattedTextParser.FlattenedToken.OutputType.STRING)
            {
                for (FormattedTextParser.TokenType type: token.types)
                    sectionBuilder.append(type.controlArg);
                sectionBuilder.append(token.outputString);
                sectionBuilder.append(TextFormatting.RESET.toString());
                addTextSection = true;
            }
            else if (token.outputType == FormattedTextParser.FlattenedToken.OutputType.ITEM)
            {
                if (addTextSection)
                {
                    String section = sectionBuilder.toString();
                    bakedComponents.add(new BakedComponent(x, y, section));
                    x += RenderUtils.simulateTextWidth(section, BAKING_TEXT_SIZE);
                    sectionBuilder = new StringBuilder();
                    addTextSection = false;
                }
                bakedComponents.add(new BakedComponent(x, y, token.outputItem));
                x += RenderUtils.simulateTextHeight(BAKING_TEXT_SIZE);
                width = Math.max(width, x);
            }
            else if (token.outputType == FormattedTextParser.FlattenedToken.OutputType.LINE_BREAK)
            {
                if (addTextSection)
                {
                    String section = sectionBuilder.toString();
                    bakedComponents.add(new BakedComponent(x, y, section));
                    x += RenderUtils.simulateTextWidth(section, BAKING_TEXT_SIZE);
                    sectionBuilder = new StringBuilder();
                    addTextSection = false;
                    width = Math.max(width, x);
                }
                y += RenderUtils.simulateTextHeight(BAKING_TEXT_SIZE);
                x = 0;
                height = Math.max(height, y + RenderUtils.simulateTextHeight(BAKING_TEXT_SIZE));
            }
        }

        if (addTextSection)
        {
            String section = sectionBuilder.toString();
            bakedComponents.add(new BakedComponent(x, y, section));
            x += RenderUtils.simulateTextWidth(section, BAKING_TEXT_SIZE);
            width = Math.max(width, x);
        }

        this.width = width;
        this.height = height;
    }
}
