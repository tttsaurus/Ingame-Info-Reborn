package com.tttsaurus.saurus3d.common.api.shader;

public class UniformField
{
    private final String type;
    private final String fieldName;

    public String getType() { return type; }
    public String getFieldName() { return fieldName; }

    public UniformField(String type, String fieldName)
    {
        this.type = type;
        this.fieldName = fieldName;
    }
}
