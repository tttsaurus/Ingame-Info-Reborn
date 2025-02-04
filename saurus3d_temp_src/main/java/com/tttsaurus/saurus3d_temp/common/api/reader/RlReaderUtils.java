package com.tttsaurus.saurus3d_temp.common.api.reader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class RlReaderUtils
{
    // notice: ResourceLocation only supports lower case
    @Nonnull
    public static String read(String rl, boolean keepNewLineSymbol)
    {
        return read(new ResourceLocation(rl), keepNewLineSymbol);
    }

    @Nonnull
    public static String read(ResourceLocation rl, boolean keepNewLineSymbol)
    {
        InputStream stream = null;
        try
        {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(rl);
            stream = resource.getInputStream();
        }
        catch (Exception ignored) { return ""; }
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
                if (keepNewLineSymbol)
                    builder.append("\n");
            }
            reader.close();
            return builder.toString();
        }
        catch (Exception ignored) { return ""; }
    }
}
