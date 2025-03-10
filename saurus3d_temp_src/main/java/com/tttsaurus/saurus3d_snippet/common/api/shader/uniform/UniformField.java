package com.tttsaurus.saurus3d_snippet.common.api.shader.uniform;

import net.minecraft.util.Tuple;

public class UniformField
{
    protected final UniformType type;
    protected final String rawType;
    protected final String fieldName;

    public UniformType getType() { return type; }
    public String getRawType() { return rawType; }
    public String getFieldName() { return fieldName; }

    public UniformField(String rawType, String fieldName)
    {
        Tuple<UniformType, String> res = UniformParseUtils.getUniformType(rawType, fieldName);
        this.type = res.getFirst();
        this.rawType = rawType;
        this.fieldName = res.getSecond();
    }
}
