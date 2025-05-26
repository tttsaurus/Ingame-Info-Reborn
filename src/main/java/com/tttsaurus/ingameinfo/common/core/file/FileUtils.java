package com.tttsaurus.ingameinfo.common.core.file;

import java.io.File;
import java.io.RandomAccessFile;

public final class FileUtils
{
    private static String parentPath = "config/ingameinfo/";

    private static File makeDir(String path)
    {
        File directory = new File(parentPath + path);
        if (!directory.exists()) directory.mkdirs();
        return directory;
    }

    public static File makeDir(String... dir)
    {
        if (dir.length == 0)
            return makeDir("");
        else
            return makeDir(String.join("/", dir));
    }

    public static File makeFile(String fileName, String... dir)
    {
        makeDir(dir);

        String filePath;
        if (dir.length == 0)
            filePath = parentPath + fileName;
        else
            filePath = parentPath + String.join("/", dir) + "/" + fileName;

        try
        {
            RandomAccessFile raf;
            raf = new RandomAccessFile(filePath, "rw");
            raf.close();
        }
        catch (Exception ignored) { }

        return new File(filePath);
    }

    public static File getFile(String fileName, String... dir)
    {
        makeDir(dir);

        String filePath;
        if (dir.length == 0)
            filePath = parentPath + fileName;
        else
            filePath = parentPath + String.join("/", dir) + "/" + fileName;

        return new File(filePath);
    }
}
