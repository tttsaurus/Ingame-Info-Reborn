package com.tttsaurus.saurus3d_temp.common.api.shader;

import javax.annotation.Nullable;

public interface IShaderLoader
{
    @Nullable
    Shader load(String rl, Shader.ShaderType shaderType);
}
