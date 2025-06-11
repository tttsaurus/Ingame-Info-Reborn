package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.core.reflection.TypeUtils;
import com.tttsaurus.ingameinfo.common.core.serialization.IDeserializer;
import java.awt.*;

@SuppressWarnings("all")
public class BuiltinTypesDeserializer<T> implements IDeserializer<T>
{
    private final Class<T> clazz;
    public BuiltinTypesDeserializer(Class<T> clazz) { this.clazz = clazz; }

    private static int parseColor0(String hex)
    {
        if (hex.length() == 6)
            return 0xff000000 | Integer.parseInt(hex, 16);
        if (hex.length() == 8)
            return (int)Long.parseLong(hex, 16);
        return 0;
    }
    private static int parseColor1(String rgb)
    {
        String[] items = rgb.split(",");
        if (items.length != 3) return 0;

        int r = Integer.parseInt(items[0].trim());
        int g = Integer.parseInt(items[1].trim());
        int b = Integer.parseInt(items[2].trim());

        return (new Color(r, g, b, 255)).getRGB();
    }
    private static int parseColor2(String argb)
    {
        String[] items = argb.split(",");
        if (items.length != 4) return 0;

        float a = Float.parseFloat(items[0].trim());
        int r = Integer.parseInt(items[1].trim());
        int g = Integer.parseInt(items[2].trim());
        int b = Integer.parseInt(items[3].trim());

        return (new Color(r, g, b, (int)(a * 255f))).getRGB();
    }
    private static int parseColor3(String rgba)
    {
        String[] items = rgba.split(",");
        if (items.length != 4) return 0;

        int r = Integer.parseInt(items[0].trim());
        int g = Integer.parseInt(items[1].trim());
        int b = Integer.parseInt(items[2].trim());
        float a = Float.parseFloat(items[3].trim());

        return (new Color(r, g, b, (int)(a * 255f))).getRGB();
    }

    @Override
    public T deserialize(String raw)
    {
        {
            // int
            if (TypeUtils.isIntOrWrappedInt(clazz))
                try
                {
                    if (raw.startsWith("#"))
                        return (T)(Object)parseColor0(raw.substring(1));
                    else if (raw.startsWith("rgb("))
                        return (T)(Object)parseColor1(raw.substring(4, raw.length() - 1));
                    else if (raw.startsWith("argb("))
                        return (T)(Object)parseColor2(raw.substring(5, raw.length() - 1));
                    else if (raw.startsWith("rgba("))
                        return (T)(Object)parseColor3(raw.substring(5, raw.length() - 1));
                    else if (raw.startsWith("0x"))
                        return (T)(Object)Integer.parseInt(raw.substring(2), 16);
                    else
                        return (T)(Object)Integer.parseInt(raw);
                }
                catch (Exception ignored) { return (T)(Object)0; }
            // long
            else if (TypeUtils.isLongOrWrappedLong(clazz))
                try { return (T)(Object)Long.parseLong(raw); }
                catch (Exception ignored) { return (T)(Object)0l; }
            // short
            else if (TypeUtils.isShortOrWrappedShort(clazz))
                try { return (T)(Object)Short.parseShort(raw); }
                catch (Exception ignored) { return (T)(Object)0; }
            // byte
            else if (TypeUtils.isByteOrWrappedByte(clazz))
                try { return (T)(Object)Byte.parseByte(raw); }
                catch (Exception ignored) { return (T)(Object)0; }
            // double
            else if (TypeUtils.isDoubleOrWrappedDouble(clazz))
                try { return (T)(Object)Double.parseDouble(raw); }
                catch (Exception ignored) { return (T)(Object)0d; }
            // float
            else if (TypeUtils.isFloatOrWrappedFloat(clazz))
                try { return (T)(Object)Float.parseFloat(raw); }
                catch (Exception ignored) { return (T)(Object)0f; }
            // character
            else if (TypeUtils.isCharacterOrWrappedCharacter(clazz))
                try { return (T)(Object)raw; }
                catch (Exception ignored) { return (T)(Object)' '; }
            // boolean
            else if (TypeUtils.isBooleanOrWrappedBoolean(clazz))
                try { return (T)(Object)Boolean.parseBoolean(raw); }
                catch (Exception ignored) { return (T)(Object)false; }
            // String
            else if (clazz.equals(String.class))
                try { return (T)(Object)raw; }
                catch (Exception ignored) { return (T)(Object)""; }
        }
        return null;
    }
}
