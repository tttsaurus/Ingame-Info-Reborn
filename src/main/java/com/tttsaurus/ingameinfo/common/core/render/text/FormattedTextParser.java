package com.tttsaurus.ingameinfo.common.core.render.text;

import com.tttsaurus.ingameinfo.common.core.commonutils.GhostableItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is an internal static parser that only works for {@link FormattedText}.
 */
public final class FormattedTextParser
{
    // treat as one-to-one func
    public enum TokenType
    {
        RAW(""),
        BLACK("black", TextFormatting.BLACK.toString()),
        DARK_BLUE("dark_blue", TextFormatting.DARK_BLUE.toString()),
        DARK_GREEN("dark_green", TextFormatting.DARK_GREEN.toString()),
        DARK_AQUA("dark_aqua", TextFormatting.DARK_AQUA.toString()),
        DARK_RED("dark_red", TextFormatting.DARK_RED.toString()),
        DARK_PURPLE("dark_purple", TextFormatting.DARK_PURPLE.toString()),
        GOLD("gold", TextFormatting.GOLD.toString()),
        GRAY("gray", TextFormatting.GRAY.toString()),
        DARK_GRAY("dark_gray", TextFormatting.DARK_GRAY.toString()),
        BLUE("blue", TextFormatting.BLUE.toString()),
        GREEN("green", TextFormatting.GREEN.toString()),
        AQUA("aqua", TextFormatting.AQUA.toString()),
        RED("red", TextFormatting.RED.toString()),
        LIGHT_PURPLE("light_purple", TextFormatting.LIGHT_PURPLE.toString()),
        YELLOW("yellow", TextFormatting.YELLOW.toString()),
        WHITE("white", TextFormatting.WHITE.toString()),
        OBFUSCATED("obf", TextFormatting.OBFUSCATED.toString()),
        BOLD("bold", TextFormatting.BOLD.toString()),
        STRIKETHROUGH("strike_through", TextFormatting.STRIKETHROUGH.toString()),
        UNDERLINE("underline", TextFormatting.UNDERLINE.toString()),
        ITALIC("italic", TextFormatting.ITALIC.toString()),
        I18N("i18n"),
        ITEM("item"),
        LINE_BREAK("br");

        public static final List<TokenType> priorityLowest = Arrays.asList(
                OBFUSCATED,
                BOLD,
                STRIKETHROUGH,
                UNDERLINE,
                ITALIC);

        public final String indicator;
        public final String controlArg;
        TokenType(String indicator, String controlArg)
        {
            this.indicator = indicator;
            this.controlArg = controlArg;
        }
        TokenType(String indicator)
        {
            this.indicator = indicator;
            this.controlArg = "";
        }
    }

    @Nullable
    private static TokenType getTokenType(String indicator)
    {
        for (TokenType type: TokenType.values())
            if (type.indicator.equals(indicator) && type != TokenType.RAW)
                return type;
        return null;
    }

    public static class NestedToken
    {
        public final TokenType type;
        public final String argString;
        public final List<NestedToken> argTokens;

        public NestedToken(String argString)
        {
            this.type = TokenType.RAW;
            this.argString = argString;
            argTokens = null;
        }
        public NestedToken(TokenType type, List<NestedToken> argTokens)
        {
            this.type = type;
            this.argTokens = argTokens;
            argString = null;
        }

        @Override
        public String toString()
        {
            return recursiveToString(0);
        }

        private String recursiveToString(int indent)
        {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < indent; i++)
                builder.append("  ");
            String pad = builder.toString();
            builder = new StringBuilder();

            builder.append(pad).append("Token(").append(type);

            if (type == TokenType.RAW)
                builder.append(", value=\"").append(argString).append("\"");
            else if (argTokens != null && !argTokens.isEmpty())
            {
                builder.append(", children=[\n");
                for (NestedToken child : argTokens)
                    builder.append(child.recursiveToString(indent + 1)).append("\n");
                builder.append(pad).append("]");
            }

            builder.append(")");
            return builder.toString();
        }
    }

    private static int tokenizeNested(String text, int startIndex, List<NestedToken> tokens, AtomicBoolean abortFlag)
    {
        if (startIndex + 1 >= text.length()) return startIndex;

        int index0 = startIndex + 1;
        int index1 = index0;
        char c = text.charAt(index1);
        while (index1 + 1 < text.length() && c != '[')
            c = text.charAt(++index1);

        // syntax error. early escape
        if (index1 + 1 >= text.length())
        {
            abortFlag.set(true);
            return -1;
        }

        String tokenIndicator = text.substring(index0, index1);
        TokenType tokenType = getTokenType(tokenIndicator);

        // syntax error. early escape
        if (tokenType == null)
        {
            abortFlag.set(true);
            return -1;
        }

        int level = 1;
        index0 = index1 + 1;
        index1 = index0;
        c = text.charAt(index1);
        while (true)
        {
            boolean nextLevel = false;
            if (c == '@')
            {
                int indexNow = index1 + 1;
                if (indexNow < text.length())
                {
                    char cNow = text.charAt(indexNow);
                    while (indexNow + 1 < text.length() && cNow != '[')
                        cNow = text.charAt(++indexNow);
                    if (getTokenType(text.substring(index1 + 1, indexNow)) != null)
                        nextLevel = true;
                    // syntax error. early escape
                    else
                    {
                        abortFlag.set(true);
                        return -1;
                    }
                }
            }

            if (nextLevel) level++;
            if (c == ']') level--;

            if (level == 0) break;
            if (index1 + 1 >= text.length()) break;

            c = text.charAt(++index1);
        }

        // syntax error. early escape
        if (level != 0)
        {
            abortFlag.set(true);
            return -1;
        }

        String nestedContent = text.substring(index0, index1);

        if (nestedContent.isEmpty())
            tokens.add(new NestedToken(tokenType, new ArrayList<>()));
        else
            tokens.add(new NestedToken(tokenType, tokenize(nestedContent)));

        return index1 + 1;
    }

    protected static List<NestedToken> tokenize(String text)
    {
        text = text.trim();
        if (text.isEmpty()) return new ArrayList<>();
        if (text.length() == 1) return Collections.singletonList(new NestedToken(text));

        List<NestedToken> tokens = new ArrayList<>();

        int index = 0;
        while (index + 1 < text.length())
        {
            int index0 = index;
            int index1 = index0;
            char c = text.charAt(index1);

            while (index1 + 1 < text.length() && c != '@')
                c = text.charAt(++index1);

            if (index1 + 1 >= text.length())
            {
                tokens.add(new NestedToken(text.substring(index0)));
                break;
            }
            else
                tokens.add(new NestedToken(text.substring(index0, index1)));

            AtomicBoolean abortFlag = new AtomicBoolean(false);
            index = tokenizeNested(text, index1, tokens, abortFlag);
            if (abortFlag.get())
                break;

            if (index + 1 == text.length())
                tokens.add(new NestedToken(text.substring(index)));
        }

        return tokens;
    }

    public static class FlattenedToken
    {
        public enum OutputType
        {
            STRING,
            ITEM,
            LINE_BREAK
        }

        public final OutputType outputType;
        public final String outputString;
        public final GhostableItem outputItem;
        public final List<TokenType> types;

        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < types.size(); i++)
            {
                builder.append(types.get(i).toString());
                if (i != types.size() - 1)
                    builder.append(" -> ");
            }

            String output = "";
            if (outputType == OutputType.STRING)
                output = outputString;
            else if (outputType == OutputType.ITEM)
                output = outputItem.toString();
            else if (outputType == OutputType.LINE_BREAK)
                output = OutputType.LINE_BREAK.toString();

            return "FlattenedToken(" +
                    "types=" + builder.toString() +
                    ", output=" + output +
                    ')';
        }

        private FlattenedToken(OutputType outputType, String outputString, GhostableItem outputItem)
        {
            this.outputType = outputType;
            this.outputString = outputString;
            this.outputItem = outputItem;
            types = new ArrayList<>();
        }

        public static FlattenedToken stringOutput(String output)
        {
            return new FlattenedToken(OutputType.STRING, output, null);
        }
        public static FlattenedToken itemOutput(GhostableItem output)
        {
            return new FlattenedToken(OutputType.ITEM, null, output);
        }
        public static FlattenedToken lineBreakOutput()
        {
            return new FlattenedToken(OutputType.LINE_BREAK, null, null);
        }
    }

    private static List<FlattenedToken> flattenizeNested(NestedToken token)
    {
        if (token.type == TokenType.RAW)
        {
            if (token.argString.isEmpty())
                return new ArrayList<>();
            else
                return Collections.singletonList(FlattenedToken.stringOutput(token.argString));
        }
        if (token.type == TokenType.I18N)
        {
            if (token.argTokens == null || token.argTokens.isEmpty())
                return new ArrayList<>();
            if (token.argTokens.get(0).type != TokenType.RAW)
                return new ArrayList<>();

            return Collections.singletonList(FlattenedToken.stringOutput(I18n.format(token.argTokens.get(0).argString)));
        }
        if (token.type == TokenType.ITEM)
        {
            if (token.argTokens == null || token.argTokens.isEmpty())
                return Collections.singletonList(FlattenedToken.itemOutput(new GhostableItem("")));
            if (token.argTokens.get(0).type != TokenType.RAW)
                return Collections.singletonList(FlattenedToken.itemOutput(new GhostableItem("")));

            GhostableItem item = new GhostableItem(token.argTokens.get(0).argString);
            item.abortNextTime();
            return Collections.singletonList(FlattenedToken.itemOutput(item));
        }
        if (token.type == TokenType.LINE_BREAK)
            return Collections.singletonList(FlattenedToken.lineBreakOutput());

        List<FlattenedToken> flattenedTokens = new ArrayList<>();

        if (token.argTokens != null && !token.argTokens.isEmpty())
        {
            for (NestedToken child: token.argTokens)
            {
                List<FlattenedToken> flattenedChildren = flattenizeNested(child);
                for (FlattenedToken flattenedChild: flattenedChildren)
                    flattenedChild.types.add(0, token.type);
                flattenedTokens.addAll(flattenedChildren);
            }
        }

        return flattenedTokens;
    }

    protected static List<FlattenedToken> flattenize(List<NestedToken> tokens)
    {
        List<FlattenedToken> flattenedTokens = new ArrayList<>();

        for (NestedToken token: tokens)
            flattenedTokens.addAll(flattenizeNested(token));

        for (FlattenedToken token: flattenedTokens)
            token.types.sort(Comparator.comparingInt(e ->
            {
                int index = TokenType.priorityLowest.indexOf(e);
                return index == -1 ? Integer.MAX_VALUE : index;
            }).reversed());

        return flattenedTokens;
    }
}
