package com.tttsaurus.ingameinfo.common.api.render.shader;

import com.tttsaurus.ingameinfo.common.api.render.shader.uniform.UniformField;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40;

import java.util.List;

public class Shader
{
    public enum ShaderType
    {
        VERTEX(GL20.GL_VERTEX_SHADER),
        FRAGMENT(GL20.GL_FRAGMENT_SHADER),
        TESS_CONTROL(GL40.GL_TESS_CONTROL_SHADER),
        TESS_EVALUATION(GL40.GL_TESS_EVALUATION_SHADER);

        public final int glValue;
        ShaderType(int glValue)
        {
            this.glValue = glValue;
        }
    }

    private String shaderSource;

    private int shaderID;
    private final ShaderType shaderType;
    private boolean valid = true;
    private String errorLog;
    private final List<UniformField> uniformFields;
    private final String fileName;

    public int getShaderID() { return shaderID; }
    public ShaderType getShaderType() { return shaderType; }
    public String getFileName() { return fileName; }
    protected boolean getValidity() { return valid; }
    protected String getErrorLog() { return errorLog; }
    protected List<UniformField> getUniformFields() { return uniformFields; }

    public Shader(String fileName, String shaderSource, ShaderType shaderType)
    {
        this.fileName = fileName;
        this.shaderSource = shaderSource;
        this.shaderType = shaderType;

        uniformFields = ShaderParseUtils.getUniformFields(shaderSource);
    }

    protected void compile()
    {
        shaderID = GL20.glCreateShader(shaderType.glValue);

        GL20.glShaderSource(shaderID, shaderSource);

        GL20.glCompileShader(shaderID);

        shaderSource = null;

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
        {
            errorLog = GL20.glGetShaderInfoLog(shaderID, 1024);
            GL20.glDeleteShader(shaderID);
            shaderID = 0;
            valid = false;
        }
    }
}
