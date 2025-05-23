package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.core.reflection.TypeUtils;
import com.tttsaurus.ingameinfo.common.core.serialization.IDeserializer;

@SuppressWarnings("all")
public class BuiltinTypesDeserializer<T> implements IDeserializer<T>
{
    private final Class<T> clazz;
    public BuiltinTypesDeserializer(Class<T> clazz) { this.clazz = clazz; }

    @Override
    public T deserialize(String raw)
    {
        {
            // int
            if (TypeUtils.isIntOrWrappedInt(clazz))
                try
                {
                    if (raw.startsWith("0x"))
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
