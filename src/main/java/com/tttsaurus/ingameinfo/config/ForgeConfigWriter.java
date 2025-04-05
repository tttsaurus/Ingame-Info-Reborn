package com.tttsaurus.ingameinfo.config;

import java.io.File;
import java.io.RandomAccessFile;
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

    private void replace(RandomAccessFile file, long start, long end, String str)
    {
        try
        {
            file.seek(end);
            byte[] remainingContent = new byte[(int)(file.length() - end)];
            file.readFully(remainingContent);

            byte[] addition = (str + "\n").getBytes(StandardCharsets.UTF_8);
            byte[] newContent = new byte[remainingContent.length + addition.length];
            System.arraycopy(addition, 0, newContent, 0, addition.length);
            System.arraycopy(remainingContent, 0, newContent, addition.length, remainingContent.length);

            file.setLength(start);
            file.seek(start);
            file.write(newContent);
        }
        catch (Exception ignored) { }
    }

    private void replaceOneLineValue(String category, String key, String value)
    {
        try
        {
            RandomAccessFile file = new RandomAccessFile(this.file, "rw");

            long start = -1;
            long end = -1;

            List<String> categories = new ArrayList<>();

            // pos1 is the start of this line
            // pos2 is the end of this line
            long pos1 = file.getFilePointer();
            String line = file.readLine();
            while (line != null)
            {
                long pos2 = file.getFilePointer();

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
                        categories.remove(categories.size() - 1);

                    // locate value
                    index = line.indexOf(key);
                    if (index >= 0 && String.join(".", categories).equals(category))
                    {
                        index += key.length();
                        char c = line.charAt(index);
                        while (c == '"' || c == '=')
                            c = line.charAt(++index);

                        start = pos1 + index;
                        end = pos2;

                        break;
                    }
                }

                pos1 = pos2;
                line = file.readLine();
            }

            if (start != -1 && end != -1)
                replace(file, start, end, value);

            file.close();
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
