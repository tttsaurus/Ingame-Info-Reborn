package com.tttsaurus.ingameinfo.common.api.serialization.json;

import java.util.ArrayList;
import java.util.List;

public final class RawJsonUtils
{
    public static String extractValue(String json, String key) 
    {
        if (json.startsWith("{") && json.endsWith("}"))
            json = json.substring(1, json.length() - 1).trim();

        String keyPattern = "\"" + key + "\"";
        int keyIndex = findKeyAtTopLevel(json, keyPattern);
        if (keyIndex == -1) return "";
        
        int startIndex = keyIndex + keyPattern.length();

        char c = json.charAt(startIndex);
        while (startIndex + 1 < json.length() && (c == ' ' || c == ':'))
            c = json.charAt(++startIndex);

        if (c == '{')
            return extractObjectValue(json, startIndex);
        else if (c == '[')
            return extractArrayValue(json, startIndex);
        else if (c == '"')
            return extractStringValue(json, startIndex);
        else
            return extractSimpleValue(json, startIndex);
    }

    public static List<String> splitArray(String json)
    {
        if (json.startsWith("[") && json.endsWith("]"))
            json = json.substring(1, json.length() - 1).trim();

        List<String> list = new ArrayList<>();

        int startIndex = 0;
        while (startIndex < json.length())
        {
            char c = json.charAt(startIndex);
            while (startIndex + 1 < json.length() && (c == ' ' || c == ','))
                c = json.charAt(++startIndex);

            String element;
            if (c == '{')
            {
                element = extractObjectValue(json, startIndex);
                startIndex += element.length();
            }
            else if (c == '[')
            {
                element = extractArrayValue(json, startIndex);
                startIndex += element.length();
            }
            else if (c == '"')
            {
                element = extractStringValue(json, startIndex);
                startIndex += element.length() + 2;
            }
            else
            {
                element = extractSimpleValue(json, startIndex);
                startIndex += element.length();
            }
            list.add(element);
            startIndex++;
        }

        return list;
    }

    private static int findKeyAtTopLevel(String json, String keyPattern)
    {
        int level = 0;
        int startIndex = 0;

        while (startIndex < json.length())
        {
            char c = json.charAt(startIndex);

            if (c == '{' || c == '[')
                level++;
            else if (c == '}' || c == ']')
                level--;

            if (level == 0 && json.startsWith(keyPattern, startIndex))
                return startIndex;

            startIndex++;
        }

        return -1;
    }

    private static String extractObjectValue(String json, int startIndex)
    {
        int braceCount = 1;
        int i = startIndex + 1;

        while (i < json.length() && braceCount > 0)
        {
            char c = json.charAt(i);
            if (c == '{')
                braceCount++;
            else if (c == '}')
                braceCount--;
            i++;
        }

        return json.substring(startIndex, i);
    }

    private static String extractArrayValue(String json, int startIndex)
    {
        int bracketCount = 1;
        int i = startIndex + 1;

        while (i < json.length() && bracketCount > 0)
        {
            char c = json.charAt(i);
            if (c == '[')
                bracketCount++;
            else if (c == ']')
                bracketCount--;
            i++;
        }

        return json.substring(startIndex, i);
    }

    private static String extractStringValue(String json, int startIndex)
    {
        int endIndex = json.indexOf('"', startIndex + 1);
        return endIndex != -1 ? json.substring(startIndex + 1, endIndex) : "";
    }

    private static String extractSimpleValue(String json, int startIndex)
    {
        int endIndex = startIndex;

        char c = json.charAt(endIndex);
        while (endIndex + 1 <= json.length() &&
               c != ' ' &&
               c != ',' &&
               c != '}')
            if (endIndex + 1 == json.length())
                endIndex++;
            else
                c = json.charAt(++endIndex);

        return json.substring(startIndex, endIndex).trim();
    }
}
