package com.tttsaurus.saurus3d_snippet.common.api.shader.uniform;

public enum Variant
{
    // float
    DEFAULT(""),

    // int
    I("i"),

    // uint
    U("u");

    private final String prefix;
    Variant(String prefix)
    {
        this.prefix = prefix;
    }

    public String getPrefix() { return prefix; }
}
