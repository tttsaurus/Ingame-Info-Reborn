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

    // a rough impl
    // has potential issues but good for now
    public void write(String key, String value)
    {
        try
        {
            RandomAccessFile fileHandler = new RandomAccessFile(file, "rw");

            long start = -1;
            long end = -1;

            long pos1 = fileHandler.getFilePointer();
            String line = fileHandler.readLine();
            while (line != null)
            {
                long pos2 = fileHandler.getFilePointer();

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
                    break;
                }

                pos1 = pos2;
                line = fileHandler.readLine();
            }

            if (start != -1 && end != -1)
            {
                fileHandler.seek(end);
                byte[] remainingContent = new byte[(int)(fileHandler.length() - end)];
                fileHandler.readFully(remainingContent);

                byte[] addition = (value + "\n").getBytes(StandardCharsets.UTF_8);
                byte[] newContent = new byte[remainingContent.length + addition.length];
                System.arraycopy(addition, 0, newContent, 0, addition.length);
                System.arraycopy(remainingContent, 0, newContent, addition.length, remainingContent.length);

                fileHandler.setLength(start);
                fileHandler.seek(start);
                fileHandler.write(newContent);
            }

            fileHandler.close();
        }
        catch (Exception ignored) { }
    }
}
