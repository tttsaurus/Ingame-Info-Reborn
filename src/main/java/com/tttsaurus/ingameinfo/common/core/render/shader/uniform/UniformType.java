package com.tttsaurus.ingameinfo.common.core.render.shader.uniform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// especially for glsl 330

// should only get this from UniformParseUtils
public class UniformType
{
    protected static final UniformType UNKNOWN = new UniformType("NO_SYMBOL", UniformTypeKind.UNKNOWN);
    protected static final UniformType ARRAY = new UniformType("NO_SYMBOL", UniformTypeKind.ARRAY);

    public static final String SYMBOL_FLOAT = "float";
    public static final String SYMBOL_DOUBLE = "double";
    public static final String SYMBOL_INT = "int";
    public static final String SYMBOL_UINT = "unit";
    public static final String SYMBOL_BOOL = "bool";

    public static final String SYMBOL_VEC2 = "vec2";
    public static final String SYMBOL_VEC3 = "vec3";
    public static final String SYMBOL_VEC4 = "vec4";

    public static final String SYMBOL_MAT2 = "mat2";
    public static final String SYMBOL_MAT3 = "mat3";
    public static final String SYMBOL_MAT4 = "mat4";

    public static final String SYMBOL_SAMPLER_1D = "sampler1D";
    public static final String SYMBOL_SAMPLER_2D = "sampler2D";
    public static final String SYMBOL_SAMPLER_3D = "sampler3D";
    public static final String SYMBOL_SAMPLER_CUBE = "samplerCube";
    public static final String SYMBOL_SAMPLER_2D_ARRAY = "sampler2DArray";
    public static final String SYMBOL_SAMPLER_CUBE_ARRAY = "samplerCubeArray";
    public static final String SYMBOL_SAMPLER_1D_SHADOW = "sampler1DShadow";
    public static final String SYMBOL_SAMPLER_2D_SHADOW = "sampler2DShadow";
    public static final String SYMBOL_SAMPLER_CUBE_SHADOW = "samplerCubeShadow";
    public static final String SYMBOL_SAMPLER_2D_MS = "sampler2DMS";
    public static final String SYMBOL_SAMPLER_2D_ARRAY_SHADOW = "sampler2DArrayShadow";

    public static final String SYMBOL_ATOMIC_UINT = "atomic_uint";

    public static final String SYMBOL_IMAGE_2D = "image2D";
    public static final String SYMBOL_IMAGE_3D = "image3D";
    public static final String SYMBOL_IMAGE_2D_ARRAY = "image2DArray";
    public static final String SYMBOL_IMAGE_CUBE = "imageCube";
    public static final String SYMBOL_IMAGE_2D_ARRAY_SHADOW = "image2DArrayShadow";

    protected static final List<UniformType> TYPE_PRESET = new ArrayList<>(Arrays.asList(

            new UniformType(SYMBOL_FLOAT, UniformTypeKind.SCALAR),
            new UniformType(SYMBOL_DOUBLE, UniformTypeKind.SCALAR),
            new UniformType(SYMBOL_INT, UniformTypeKind.SCALAR),
            new UniformType(SYMBOL_UINT, UniformTypeKind.SCALAR),
            new UniformType(SYMBOL_BOOL, UniformTypeKind.SCALAR),

            new UniformType(SYMBOL_VEC2, UniformTypeKind.VECTOR),
            new UniformType(SYMBOL_VEC3, UniformTypeKind.VECTOR),
            new UniformType(SYMBOL_VEC4, UniformTypeKind.VECTOR),

            new UniformType(SYMBOL_MAT2, UniformTypeKind.MATRIX),
            new UniformType(SYMBOL_MAT3, UniformTypeKind.MATRIX),
            new UniformType(SYMBOL_MAT4, UniformTypeKind.MATRIX),

            new UniformType(SYMBOL_SAMPLER_1D, UniformTypeKind.SAMPLER),
            new UniformType(SYMBOL_SAMPLER_2D, UniformTypeKind.SAMPLER),
            new UniformType(SYMBOL_SAMPLER_3D, UniformTypeKind.SAMPLER),
            new UniformType(SYMBOL_SAMPLER_CUBE, UniformTypeKind.SAMPLER),
            new UniformType(SYMBOL_SAMPLER_2D_ARRAY, UniformTypeKind.SAMPLER),
            new UniformType(SYMBOL_SAMPLER_CUBE_ARRAY, UniformTypeKind.SAMPLER),
            new UniformType(SYMBOL_SAMPLER_1D_SHADOW, UniformTypeKind.SAMPLER),
            new UniformType(SYMBOL_SAMPLER_2D_SHADOW, UniformTypeKind.SAMPLER),
            new UniformType(SYMBOL_SAMPLER_CUBE_SHADOW, UniformTypeKind.SAMPLER),
            new UniformType(SYMBOL_SAMPLER_2D_MS, UniformTypeKind.SAMPLER),
            new UniformType(SYMBOL_SAMPLER_2D_ARRAY_SHADOW, UniformTypeKind.SAMPLER),

            new UniformType(SYMBOL_ATOMIC_UINT, UniformTypeKind.ATOMIC_COUNTER),

            new UniformType(SYMBOL_IMAGE_2D, UniformTypeKind.IMAGE),
            new UniformType(SYMBOL_IMAGE_3D, UniformTypeKind.IMAGE),
            new UniformType(SYMBOL_IMAGE_2D_ARRAY, UniformTypeKind.IMAGE),
            new UniformType(SYMBOL_IMAGE_CUBE, UniformTypeKind.IMAGE),
            new UniformType(SYMBOL_IMAGE_2D_ARRAY_SHADOW, UniformTypeKind.IMAGE)
    ));

    private String symbol;
    private UniformTypeKind kind;
    private Variant variant = Variant.DEFAULT;
    private UniformType subType = null;
    private int arrayLength = 0;

    protected UniformType(String symbol, UniformTypeKind kind)
    {
        this.symbol = symbol;
        this.kind = kind;
    }

    protected UniformType setSymbol(String symbol) { this.symbol = symbol; return this; }
    public String getSymbol() { return symbol; }

    protected UniformType setKind(UniformTypeKind kind) { this.kind = kind; return this; }
    public UniformTypeKind getKind() { return kind; }

    protected UniformType setVariant(Variant variant) { this.variant = variant; return this; }
    public Variant getVariant() { return variant; }

    protected UniformType setSubType(UniformType subType) { this.subType = subType; return this; }
    public UniformType getSubType() { return subType; }

    protected UniformType setArrayLength(int arrayLength) { this.arrayLength = arrayLength; return this; }
    public int getArrayLength() { return arrayLength; }

    public UniformType deepClone()
    {
        UniformType cloned = new UniformType(symbol, kind);
        cloned.variant = variant;
        cloned.arrayLength = arrayLength;
        if (subType == null)
            cloned.subType = null;
        else
            cloned.subType = subType.deepClone();
        return cloned;
    }

    @Override
    public String toString()
    {
        UniformType temp;

        StringBuilder builder = new StringBuilder();
        builder.append(variant.getPrefix()).append(symbol);

        temp = this;

        if (temp.kind == UniformTypeKind.ARRAY && temp.subType != null)
        {
            builder.append("[").append(temp.arrayLength).append("]");
            temp = temp.subType;
        }

        builder.append(" (").append(temp.kind.toString()).append(")");

        return builder.toString();
    }
}
