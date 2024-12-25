package com.tttsaurus.ingameinfo.common.api.serialization.ixml;

import net.minecraft.util.Tuple;
import java.util.ArrayList;
import java.util.List;

public final class RawIxmlUtils
{
    public static List<Tuple<String, String>> extractNodes(String ixml)
    {
        ixml = ixml.trim();

        List<Tuple<String, String>> list = new ArrayList<>();

        int startIndex = 0;
        while (startIndex < ixml.length())
        {
            char c = ixml.charAt(startIndex);

            while (startIndex + 1 < ixml.length() && (c == ' ' || c == '<'))
                c = ixml.charAt(++startIndex);

            if (startIndex == ixml.length() - 1) break;

            int endIndex = startIndex;

            while (endIndex + 1 < ixml.length() && !(c == ' ' || c == '>'))
                c = ixml.charAt(++endIndex);

            String key = ixml.substring(startIndex, endIndex);

            int endIndex2 = endIndex;

            while (endIndex2 + 1 < ixml.length() && (c != '>'))
                c = ixml.charAt(++endIndex2);

            String value;
            if (endIndex2 > endIndex)
                value = ixml.substring(endIndex + 1, endIndex2);
            else
                value = "";

            list.add(new Tuple<>(key.trim(), value.trim()));

            startIndex = endIndex2 + 1;
        }

        return list;
    }

    public static List<Tuple<String, String>> splitParams(String param)
    {
        param = param.trim();

        List<Tuple<String, String>> list = new ArrayList<>();

        int startIndex = 0;
        while (startIndex < param.length())
        {
            int endIndex = startIndex;

            char c = param.charAt(endIndex);

            while (endIndex + 1 < param.length() && (c != '='))
                c = param.charAt(++endIndex);

            if (endIndex == param.length() - 1) break;

            String key = param.substring(startIndex, endIndex);

            do c = param.charAt(++endIndex);
            while (endIndex + 1 < param.length() && (c == ' '));

            int endIndex2 = endIndex;

            String value;
            if (c == '\"')
            {
                // not going to support escape char
                endIndex2 = param.indexOf("\"", endIndex2 + 1);
                value = param.substring(endIndex + 1, endIndex2);
                endIndex2++;
            }
            else
            {
                while (endIndex2 + 1 < param.length() && (c != ' '))
                    c = param.charAt(++endIndex2);
                if (endIndex2 == param.length() - 1)
                    value = param.substring(endIndex, endIndex2 + 1);
                else
                    value = param.substring(endIndex, endIndex2);
                value = value.trim();
            }

            list.add(new Tuple<>(key.trim(), value));

            startIndex = endIndex2 + 1;
        }

        return list;
    }
}
