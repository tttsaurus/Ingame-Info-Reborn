package com.tttsaurus.ingameinfo.common.core.render.text;

import com.tttsaurus.ingameinfo.common.core.commonutils.GhostableItem;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.List;

/**
 * A temporary workaround to make text formatting easier to use in Minecraft.
 * It only works for {@link RenderUtils} and
 * relies on {@link RenderUtils#simulateTextWidth(String, float)} & {@link RenderUtils#simulateTextHeight(float)}
 * to function properly.
 */
public final class FormattedText
{
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

    private FormattedText(String text)
    {
        bakedComponents = new ArrayList<>();

        List<FormattedTextParser.FlattenedToken> flattenedTokens = FormattedTextParser.flattenize(FormattedTextParser.tokenize(text));

        float x = 0, y = 0;

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
                    x += RenderUtils.simulateTextWidth(section, 1f);
                    sectionBuilder = new StringBuilder();
                    addTextSection = false;
                }
                bakedComponents.add(new BakedComponent(x, y, token.outputItem));
                x += RenderUtils.simulateTextHeight(1f);
            }
            else if (token.outputType == FormattedTextParser.FlattenedToken.OutputType.LINE_BREAK)
            {
                if (addTextSection)
                {
                    String section = sectionBuilder.toString();
                    bakedComponents.add(new BakedComponent(x, y, section));
                    sectionBuilder = new StringBuilder();
                    addTextSection = false;
                }
                y += RenderUtils.simulateTextHeight(1f);
                x = 0;
            }
        }

        if (addTextSection)
        {
            String section = sectionBuilder.toString();
            bakedComponents.add(new BakedComponent(x, y, section));
        }
    }
}
