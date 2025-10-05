package com.tttsaurus.ingameinfo.config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ForgeConfigWriter
{
    private final File file;

    public ForgeConfigWriter(File file)
    {
        this.file = file;
    }

    private void replaceOneLineValue(String category, String key, String value)
    {
        try
        {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)))
            {
                String line;
                while ((line = reader.readLine()) != null)
                    lines.add(line);
            }

            List<String> categories = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++)
            {
                String line = lines.get(i);

                // skip comments
                if (!line.trim().startsWith("#"))
                {
                    // locate category
                    int index = line.indexOf("{");
                    if (index >= 0)
                    {
                        String cat = line.substring(0, index).trim();
                        categories.add(cat);
                    }

                    if (line.contains("}"))
                        if (!categories.isEmpty())
                            categories.remove(categories.size() - 1);

                    // locate value
                    index = line.indexOf(key);
                    if (index >= 0 && String.join(".", categories).equals(category))
                    {
                        int valueStart = index + key.length();
                        while (valueStart < line.length() && (line.charAt(valueStart) == '"' || line.charAt(valueStart) == '=' || line.charAt(valueStart) == ' '))
                            valueStart++;

                        String newLine = line.substring(0, valueStart) + value;
                        lines.set(i, newLine);
                        break;
                    }
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)))
            {
                for (String line : lines)
                {
                    writer.write(line);
                    writer.write(System.lineSeparator());
                }
            }
        }
        catch (Exception ignored) { }
    }

    public void replaceInt(String category, String key, int value)
    {
        replaceOneLineValue(category, key, String.valueOf(value));
    }

    public void replaceFloat(String category, String key, float value)
    {
        replaceOneLineValue(category, key, String.valueOf(value));
    }

    public void replaceBoolean(String category, String key, boolean value)
    {
        replaceOneLineValue(category, key, value ? "true" : "false");
    }

    public void replaceString(String category, String key, String value)
    {
        replaceOneLineValue(category, key, value);
    }
}
