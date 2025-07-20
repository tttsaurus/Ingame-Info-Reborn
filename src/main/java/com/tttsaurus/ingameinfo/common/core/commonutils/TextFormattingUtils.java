package com.tttsaurus.ingameinfo.common.core.commonutils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class TextFormattingUtils
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
        ITEM("item");

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

    public static class Token
    {
        public final TokenType type;
        public final String argString;
        public final List<Token> argTokens;

        public Token(String argString)
        {
            this.type = TokenType.RAW;
            this.argString = argString;
            argTokens = null;
        }
        public Token(TokenType type, List<Token> argTokens)
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
                for (Token child : argTokens)
                    builder.append(child.recursiveToString(indent + 1)).append("\n");
                builder.append(pad).append("]");
            }

            builder.append(")");
            return builder.toString();
        }
    }

    private static int tokenizeNested(String text, int startIndex, List<Token> tokens)
    {
        if (startIndex + 1 >= text.length()) return startIndex;

        int index0 = startIndex + 1;
        int index1 = index0;
        char c = text.charAt(index1);
        while (index1 + 1 < text.length() && c != '[')
            c = text.charAt(++index1);

        // syntax error. early escape
        if (index1 + 1 >= text.length()) return index1;

        String tokenIndicator = text.substring(index0, index1);
        TokenType tokenType = getTokenType(tokenIndicator);

        // syntax error. early escape
        if (tokenType == null) return index1 + 1;

        int level = 1;
        index0 = index1 + 1;
        index1 = index0;
        c = text.charAt(index1);
        while (index1 + 1 < text.length() && level != 0)
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
                    else
                        // syntax error. early escape
                        return indexNow + 1;
                }
            }

            if (nextLevel) level++;
            if (c == ']') level--;

            c = text.charAt(++index1);
        }

        String nestedContent = text.substring(index0, index1);

        if (nestedContent.isEmpty())
            tokens.add(new Token(tokenType, new ArrayList<>()));
        else
            tokens.add(new Token(tokenType, tokenize(nestedContent)));

        return index1;
    }

    public static List<Token> tokenize(String text)
    {
        text = text.trim();
        if (text.isEmpty()) return new ArrayList<>();

        List<Token> tokens = new ArrayList<>();

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
                tokens.add(new Token(text.substring(index0)));
                break;
            }
            else
                tokens.add(new Token(text.substring(index0, index1)));

            index = tokenizeNested(text, index1, tokens);
        }

        return tokens;
    }
}
