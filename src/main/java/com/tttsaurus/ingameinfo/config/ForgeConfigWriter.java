package com.tttsaurus.ingameinfo.config;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

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

    public void write(String key, String value)
    {
        try
        {
            RandomAccessFile file = new RandomAccessFile(this.file, "rw");

            long start = -1;
            long end = -1;

            long pos1 = file.getFilePointer();
            String line = file.readLine();
            while (line != null)
            {
                long pos2 = file.getFilePointer();

                int index = line.indexOf(key);
                if (index >= 0)
                {
                    index += key.length();
                    char c = line.charAt(index);
                    while (c == '"' || c == '=')
                        c = line.charAt(++index);
                    pos1 += index;
                    start = pos1;
                    end = pos2;

                    // check category
                    // check comment
                    break;
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
}
