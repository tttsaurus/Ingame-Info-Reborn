package com.tttsaurus.ingameinfo.common.core.commonutils;

import com.tttsaurus.ingameinfo.InGameInfoReborn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public final class TextFormattingV2
{
    // treat as one-to-one func
    public enum TokenType
    {
        RAW(""),
        BLACK("black"),
        DARK_BLUE("dark_blue"),
        DARK_GREEN("dark_green"),
        DARK_AQUA("dark_aqua"),
        DARK_RED("dark_red"),
        DARK_PURPLE("dark_purple"),
        GOLD("gold"),
        GRAY("gray"),
        DARK_GRAY("dark_gray"),
        BLUE("blue"),
        GREEN("green"),
        AQUA("aqua"),
        RED("red"),
        LIGHT_PURPLE("light_purple"),
        YELLOW("yellow"),
        WHITE("white"),
        OBFUSCATED("obf"),
        BOLD("bold"),
        STRIKETHROUGH("strike_through"),
        UNDERLINE("underline"),
        ITALIC("italic"),
        I18N("i18n"),
        ITEM("item"),
        LINE_BREAK("br");

        public final String indicator;
        TokenType(String indicator)
        {
            this.indicator = indicator;
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

    public static List<NestedToken> tokenize(String text)
    {
        text = text.trim();
        if (text.isEmpty()) return new ArrayList<>();

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
        public static FlattenedToken lineBreakOutput(GhostableItem output)
        {
            return new FlattenedToken(OutputType.LINE_BREAK, null, null);
        }
    }

    public static List<FlattenedToken> flattenize(List<NestedToken> tokens)
    {
        List<FlattenedToken> flattenedTokens = new ArrayList<>();

        for (NestedToken token: tokens)
        {

        }

        return flattenedTokens;
    }
}
