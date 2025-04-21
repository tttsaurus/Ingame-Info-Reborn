package com.tttsaurus.saurus3d_snippet.common.api.shader;

import com.tttsaurus.ingameinfo.common.api.reflection.TypeUtils;
import com.tttsaurus.ingameinfo.common.api.render.GlResourceManager;
import com.tttsaurus.ingameinfo.common.api.render.IGlDisposable;
import com.tttsaurus.saurus3d_snippet.common.api.CommonBuffers;
import com.tttsaurus.saurus3d_snippet.common.api.shader.uniform.UniformField;
import com.tttsaurus.saurus3d_snippet.common.api.shader.uniform.UniformType;
import com.tttsaurus.saurus3d_snippet.common.api.shader.uniform.UniformTypeKind;
import com.tttsaurus.saurus3d_snippet.common.api.shader.uniform.Variant;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.*;
import javax.annotation.Nullable;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShaderProgram implements Comparable<ShaderProgram>, IGlDisposable
{
    private boolean setup;
    private double cpuTimeMs;
    private double gpuTimeMs;
    private int prevProgramID;
    private int programID;
    private final Map<UniformField, Integer> uniformFields = new ConcurrentHashMap<>();
    private final List<Integer> shaderIDs = new ArrayList<>();
    private final List<Shader> shaders = new ArrayList<>();

    public boolean getSetup() { return setup; }
    public int getProgramID() { return programID; }

    // call after setup()
    public String getSetupDebugReport()
    {
        StringBuilder builder = new StringBuilder();

        builder.append("\n===Shader Program Setup Report===\n").append("Shaders:\n");
        for (Shader shader: shaders)
        {
            builder
                    .append(String.format("  - [%s] %s", shader.getShaderType().toString(), shader.getFileName())).append("\n")
                    .append("    - Validity: ").append(shader.getValidity() ? "valid" : "invalid").append("\n");
            if (!shader.getValidity())
                builder.append("    - Error Log: ").append(shader.getErrorLog()).append("\n");
        }

        builder.append("\nActive Shaders:\n");
        for (int shaderID: shaderIDs)
        {
            Shader shader = getShaderByID(shaderID);
            if (shader != null)
            {
                builder
                        .append(String.format("  - [%s] %s", shader.getShaderType().toString(), shader.getFileName())).append("\n")
                        .append("    - Shader GL ID: ").append(shader.getShaderID()).append("\n")
                        .append("    - Uniform Fields:").append((shader.getUniformFields().isEmpty() ? " (Empty)" : "")).append("\n");
                for (UniformField field: shader.getUniformFields())
                    builder.append(String.format("      - [%s] %s", field.getType(), field.getFieldName())).append("\n");
            }
        }

        builder
                .append("\nShader Program:\n")
                .append("  - Program GL ID: ").append(programID).append("\n")
                .append("  - Program Uniform Fields:").append((uniformFields.isEmpty() ? " (Empty)" : "")).append("\n");
        for (Map.Entry<UniformField, Integer> entry: uniformFields.entrySet())
            builder.append(String.format("    - [%s] %s, GL Loc: %d", entry.getKey().getType(), entry.getKey().getFieldName(), entry.getValue())).append("\n");

        builder.append("\nCPU Time Taken: ").append(cpuTimeMs).append(" ms").append("\n");
        builder.append("GPU Time Taken: ").append(gpuTimeMs).append(" ms").append("\n");

        builder.append("\n===End of the Setup Report===");

        return builder.toString();
    }

    @Nullable
    private Shader getShaderByID(int shaderID)
    {
        for (Shader shader: shaders)
            if (shaderID == shader.getShaderID())
                return shader;
        return null;
    }

    public ShaderProgram(Shader... shaders)
    {
        this.shaders.addAll(Arrays.asList(shaders));
        setup = false;
    }

    public void setup()
    {
        if (setup) return;

        int gpuTimeQueryID = GL15.glGenQueries();
        GL15.glBeginQuery(GL33.GL_TIME_ELAPSED, gpuTimeQueryID);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (Shader shader: shaders)
        {
            shader.compile();
            if (shader.getValidity())
                shaderIDs.add(shader.getShaderID());
        }
        for (int shaderID: shaderIDs)
        {
            Shader shader = getShaderByID(shaderID);
            if (shader != null)
            {
                List<String> nameCache = new ArrayList<>();
                for (UniformField field : shader.getUniformFields())
                {
                    if (nameCache.contains(field.getFieldName())) continue;
                    nameCache.add(field.getFieldName());
                    uniformFields.put(field, -1);
                }
            }
        }

        programID = GL20.glCreateProgram();
        for (int shaderID: shaderIDs)
            GL20.glAttachShader(programID, shaderID);

        GL20.glLinkProgram(programID);

        Iterator<Map.Entry<UniformField, Integer>> iterator = uniformFields.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<UniformField, Integer> entry = iterator.next();
            int uniformLocation = GL20.glGetUniformLocation(programID, entry.getKey().getFieldName());

            if (uniformLocation != -1)
                entry.setValue(uniformLocation);
            else
                iterator.remove();
        }

        stopWatch.stop();
        cpuTimeMs = stopWatch.getTime();

        GL15.glEndQuery(GL33.GL_TIME_ELAPSED);
        GL15.glGetQueryObject(gpuTimeQueryID, GL15.GL_QUERY_RESULT, CommonBuffers.intBuffer);
        gpuTimeMs = CommonBuffers.intBuffer.get(0) / 1.0E6d;

        setup = true;

        GlResourceManager.addDisposable(this);
    }

    public int getUniformLocation(UniformField field)
    {
        Integer loc = uniformFields.get(field);
        if (loc == null)
            return -1;
        else
            return loc;
    }
    public int getUniformLocation(String fieldName)
    {
        for (Map.Entry<UniformField, Integer> entry: uniformFields.entrySet())
            if (entry.getKey().getFieldName().equals(fieldName))
                return entry.getValue();
        return GL20.glGetUniformLocation(programID, fieldName);
    }
    @Nullable
    public UniformField getUniform(String fieldName)
    {
        for (Map.Entry<UniformField, Integer> entry: uniformFields.entrySet())
            if (entry.getKey().getFieldName().equals(fieldName))
                return entry.getKey();
        return null;
    }

    private static float getFloat(Class<?> clazz, Object value)
    {
        float v = 0;
        if (TypeUtils.isFloatOrWrappedFloat(clazz))
            v = (float)value;
        else if (TypeUtils.isIntOrWrappedInt(clazz))
            v = (float)((int)value);
        else if (TypeUtils.isDoubleOrWrappedDouble(clazz))
            v = (float)((double)value);
        return v;
    }
    private static double getDouble(Class<?> clazz, Object value)
    {
        double v = 0;
        if (TypeUtils.isDoubleOrWrappedDouble(clazz))
            v = (double)value;
        else if (TypeUtils.isIntOrWrappedInt(clazz))
            v = (double)((int)value);
        else if (TypeUtils.isFloatOrWrappedFloat(clazz))
            v = (double)((float)value);
        return v;
    }
    private static int getInt(Class<?> clazz, Object value)
    {
        int v = 0;
        if (TypeUtils.isIntOrWrappedInt(clazz))
            v = (int)value;
        return v;
    }
    private static int getUint(Class<?> clazz, Object value)
    {
        int v = 0;
        if (TypeUtils.isIntOrWrappedInt(clazz))
            v = (int)value;
        else if (TypeUtils.isLongOrWrappedLong(clazz))
            v = (int)(((long)value) & 0xFFFFFFFFL);
        return v;
    }
    private static int getBool(Class<?> clazz, Object value)
    {
        int v = 0;
        if (TypeUtils.isBooleanOrWrappedBoolean(clazz))
            v = ((boolean)value) ? 1 : 0;
        else if (TypeUtils.isIntOrWrappedInt(clazz))
            v = ((int)value) > 0 ? 1 : 0;
        return v;
    }

    // call after use()
    public void setUniform(String fieldName, Object... values)
    {
        UniformField field = null;
        int loc = 0;
        for (Map.Entry<UniformField, Integer> entry: uniformFields.entrySet())
            if (entry.getKey().getFieldName().equals(fieldName))
            {
                field = entry.getKey();
                loc = entry.getValue();
            }
        if (field == null) return;

        UniformType type = field.getType();

        if (type.getKind() == UniformTypeKind.SCALAR)
        {
            Object value = values[0];
            Class<?> clazz = value.getClass();

            if (type.getSymbol().equals(UniformType.SYMBOL_FLOAT))
                GL20.glUniform1f(loc, getFloat(clazz, value));
            else if (type.getSymbol().equals(UniformType.SYMBOL_DOUBLE))
                GL40.glUniform1d(loc, getDouble(clazz, value));
            else if (type.getSymbol().equals(UniformType.SYMBOL_INT))
                GL20.glUniform1i(loc, getInt(clazz, value));
            else if (type.getSymbol().equals(UniformType.SYMBOL_UINT))
                GL30.glUniform1ui(loc, getUint(clazz, value));
            else if (type.getSymbol().equals(UniformType.SYMBOL_BOOL))
                GL20.glUniform1i(loc, getBool(clazz, value));
        }
        else if (type.getKind() == UniformTypeKind.VECTOR)
        {
            if (type.getSymbol().equals(UniformType.SYMBOL_VEC2))
            {
                Object value0 = values[0];
                Class<?> clazz0 = value0.getClass();
                Object value1 = values[1];
                Class<?> clazz1 = value1.getClass();

                if (type.getVariant() == Variant.DEFAULT)
                    GL20.glUniform2f(loc, getFloat(clazz0, value0), getFloat(clazz1, value1));
                else if (type.getVariant() == Variant.I)
                    GL20.glUniform2i(loc, getInt(clazz0, value0), getInt(clazz1, value1));
                else if (type.getVariant() == Variant.U)
                    GL30.glUniform2ui(loc, getUint(clazz0, value0), getUint(clazz1, value1));
            }
            else if (type.getSymbol().equals(UniformType.SYMBOL_VEC3))
            {
                Object value0 = values[0];
                Class<?> clazz0 = value0.getClass();
                Object value1 = values[1];
                Class<?> clazz1 = value1.getClass();
                Object value2 = values[2];
                Class<?> clazz2 = value2.getClass();

                if (type.getVariant() == Variant.DEFAULT)
                    GL20.glUniform3f(loc, getFloat(clazz0, value0), getFloat(clazz1, value1), getFloat(clazz2, value2));
                else if (type.getVariant() == Variant.I)
                    GL20.glUniform3i(loc, getInt(clazz0, value0), getInt(clazz1, value1), getInt(clazz2, value2));
                else if (type.getVariant() == Variant.U)
                    GL30.glUniform3ui(loc, getUint(clazz0, value0), getUint(clazz1, value1), getUint(clazz2, value2));
            }
            else if (type.getSymbol().equals(UniformType.SYMBOL_VEC4))
            {
                Object value0 = values[0];
                Class<?> clazz0 = value0.getClass();
                Object value1 = values[1];
                Class<?> clazz1 = value1.getClass();
                Object value2 = values[2];
                Class<?> clazz2 = value2.getClass();
                Object value3 = values[3];
                Class<?> clazz3 = value3.getClass();

                if (type.getVariant() == Variant.DEFAULT)
                    GL20.glUniform4f(loc, getFloat(clazz0, value0), getFloat(clazz1, value1), getFloat(clazz2, value2), getFloat(clazz3, value3));
                else if (type.getVariant() == Variant.I)
                    GL20.glUniform4i(loc, getInt(clazz0, value0), getInt(clazz1, value1), getInt(clazz2, value2), getInt(clazz3, value3));
                else if (type.getVariant() == Variant.U)
                    GL30.glUniform4ui(loc, getUint(clazz0, value0), getUint(clazz1, value1), getUint(clazz2, value2), getUint(clazz3, value3));
            }
        }
        else if (type.getKind() == UniformTypeKind.MATRIX)
        {
            Object value = values[0];

            if (value instanceof FloatBuffer buffer)
            {
                if (type.getSymbol().equals(UniformType.SYMBOL_MAT2))
                    GL20.glUniformMatrix2(loc, false, buffer);
                else if (type.getSymbol().equals(UniformType.SYMBOL_MAT3))
                    GL20.glUniformMatrix3(loc, false, buffer);
                else if (type.getSymbol().equals(UniformType.SYMBOL_MAT4))
                    GL20.glUniformMatrix4(loc, false, buffer);
            }
        }
        else if (type.getKind() == UniformTypeKind.SAMPLER)
        {
            Object value = values[0];
            Class<?> clazz = value.getClass();

            GL20.glUniform1i(loc, getInt(clazz, value));
        }
    }

    public void use()
    {
        prevProgramID = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        GL20.glUseProgram(programID);
    }
    public void unuse()
    {
        GL20.glUseProgram(prevProgramID);
    }

    public void dispose()
    {
        for (int shaderID: shaderIDs)
        {
            GL20.glDetachShader(programID, shaderID);
            GL20.glDeleteShader(shaderID);
        }
        GL20.glDeleteProgram(programID);
    }

    @Override
    public int compareTo(@NotNull ShaderProgram o)
    {
        return Integer.compare(programID, o.programID);
    }
}
