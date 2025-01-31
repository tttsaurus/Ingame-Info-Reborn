package com.tttsaurus.saurus3d.common.api.shader;

import org.lwjgl.opengl.GL20;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShaderProgram
{
    private int programID;
    private final Map<UniformField, Integer> uniformFields = new ConcurrentHashMap<>();
    private final List<Integer> shaderIDs = new ArrayList<>();
    private final List<Shader> shaders = new ArrayList<>();

    public int getProgramID() { return programID; }

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
    }

    public void setup()
    {
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
    }

    public int getUniformLocation(String fieldName)
    {
        for (Map.Entry<UniformField, Integer> entry: uniformFields.entrySet())
            if (entry.getKey().getFieldName().equals(fieldName))
                return entry.getValue();
        return GL20.glGetUniformLocation(programID, fieldName);
    }

    public void use()
    {
        GL20.glUseProgram(programID);
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
}
