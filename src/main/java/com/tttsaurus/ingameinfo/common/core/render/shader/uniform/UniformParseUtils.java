package com.tttsaurus.ingameinfo.common.core.render.shader.uniform;

import net.minecraft.util.Tuple;

public final class UniformParseUtils
{
    private static boolean isArray(String fieldName)
    {
        return fieldName.endsWith("]");
    }

    // doesn't allow multi array
    public static Tuple<UniformType, String> getUniformType(String rawType, String fieldName)
    {
        UniformType type = UniformType.UNKNOWN.deepClone();
        type.setSymbol(rawType);

        boolean array = false;
        if (isArray(fieldName))
        {
            int i = fieldName.length() - 2;
            for (; i >= 0; i--)
                if (fieldName.charAt(i) == '[')
                    break;
            if (i > 0)
            {
                String lenArg = fieldName.substring(i + 1, fieldName.length() - 1);
                try
                {
                    int arrayLength = Integer.parseInt(lenArg);
                    array = true;
                    fieldName = fieldName.substring(0, i);
                    type = UniformType.ARRAY.deepClone().setArrayLength(arrayLength);
                }
                catch (Exception ignored) { }
            }
        }

        for (UniformType uType: UniformType.TYPE_PRESET)
        {
            UniformType cloned = uType.deepClone();
            String symbol = cloned.getSymbol();
            if (rawType.equals(symbol))
            {
                if (array)
                    type.setSubType(cloned);
                else
                    type = cloned;
                type.setSymbol(symbol);
            }
            else if (rawType.equals("i" + symbol))
            {
                if (array)
                    type.setSubType(cloned.setVariant(Variant.I)).setVariant(Variant.I);
                else
                    type = cloned.setVariant(Variant.I);
                type.setSymbol(symbol);
            }
            else if (rawType.equals("u" + symbol))
            {
                if (array)
                    type.setSubType(cloned.setVariant(Variant.U)).setVariant(Variant.U);
                else
                    type = cloned.setVariant(Variant.U);
                type.setSymbol(symbol);
            }
        }

        return new Tuple<>(type, fieldName);
    }
}
